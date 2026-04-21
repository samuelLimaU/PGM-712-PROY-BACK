package com.multimedia.spring.multimed.aplicacion.service;

import com.multimedia.spring.multimed.aplicacion.dto.DetallePedidoResponseDTO;
import com.multimedia.spring.multimed.aplicacion.dto.ItemPedidoDTO;
import com.multimedia.spring.multimed.aplicacion.dto.PedidoRequestDTO;
import com.multimedia.spring.multimed.aplicacion.dto.PedidoResponseDTO;
import com.multimedia.spring.multimed.dominio.models.*;
import com.multimedia.spring.multimed.dominio.repository.*;
import com.multimedia.spring.multimed.infraestructura.jpa.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final SpringDataClienteInfoRepository clienteInfoRepository;
    private final SpringDataPedidoRepository pedidoRepository;
    private final SpringDataPedidoDetalleRepository pedidoDetalleRepository;
    private final SpringDataPagoRepository pagoRepository;
    private final ProductoRepository productoRepository;
    private final SpringDataUsuarioRepository usuarioRepository;

    public PedidoService(SpringDataClienteInfoRepository clienteInfoRepository,
                         SpringDataPedidoRepository pedidoRepository,
                         SpringDataPedidoDetalleRepository pedidoDetalleRepository,
                         SpringDataPagoRepository pagoRepository,
                         ProductoRepository productoRepository,
                         SpringDataUsuarioRepository usuarioRepository) {
        this.clienteInfoRepository = clienteInfoRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoDetalleRepository = pedidoDetalleRepository;
        this.pagoRepository = pagoRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Long crearPedido(PedidoRequestDTO request) {
        // 1. Guardar Info del Cliente
        EntityClienteInfo cliente = new EntityClienteInfo();
        if (request.getUsuarioId() != null) {
            cliente.setUsuario(usuarioRepository.findById(request.getUsuarioId()).orElse(null));
        }
        cliente.setNombre(request.getNombre());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());
        cliente.setCiudad(request.getCiudad());
        cliente.setReferencia(request.getReferencia());
        cliente = clienteInfoRepository.save(cliente);

        // 2. Calcular total y validar stock
        BigDecimal total = BigDecimal.ZERO;
        List<EntityPedidoDetalle> detalles = new ArrayList<>();

        for (ItemPedidoDTO item : request.getItems()) {
            Producto prodDom = productoRepository.buscarPorId(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));

            if (prodDom.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + prodDom.getNombre());
            }

            // Descontar Stock
            prodDom.setStock(prodDom.getStock() - item.getCantidad());
            productoRepository.guardar(prodDom);

            // Calcular subtotal
            BigDecimal subtotal = prodDom.getPrecio().multiply(new BigDecimal(item.getCantidad()));
            total = total.add(subtotal);

            // Preparar detalle
            EntityPedidoDetalle detalle = new EntityPedidoDetalle();
            // Necesitamos la entidad para el detalle
            EntityProducto entityProd = new EntityProducto();
            entityProd.setId(prodDom.getId());
            
            detalle.setProducto(entityProd);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(prodDom.getPrecio());
            detalles.add(detalle);
        }

        // 3. Guardar Pedido
        EntityPedido pedido = new EntityPedido();
        pedido.setCliente(cliente);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setTotal(total);
        pedido.setNotas(request.getNotas());
        pedido = pedidoRepository.save(pedido);

        // 4. Guardar Detalles
        for (EntityPedidoDetalle detalle : detalles) {
            detalle.setPedido(pedido);
            pedidoDetalleRepository.save(detalle);
        }

        // 5. Inicializar Pago
        EntityPago pago = new EntityPago();
        pago.setPedido(pedido);
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setMonto(total);
        pago.setFecha(LocalDateTime.now());
        pagoRepository.save(pago);

        return pedido.getId();
    }

    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO obtenerPedido(Long id) {
        EntityPedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return mapToResponseDTO(pedido);
    }

    @Transactional
    public void actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        EntityPedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(nuevoEstado);
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void registrarPago(Long pedidoId, MetodoPago metodo, String notas) {
        EntityPago pago = pagoRepository.findAll().stream()
                .filter(p -> p.getPedido().getId().equals(pedidoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Registro de pago no encontrado"));
        
        pago.setMetodo(metodo);
        pago.setEstado(EstadoPago.COMPLETADO);
        pago.setFechaPago(LocalDateTime.now());
        pago.setNotas(notas);
        pagoRepository.save(pago);
    }

    private PedidoResponseDTO mapToResponseDTO(EntityPedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setClienteNombre(pedido.getCliente().getNombre());
        dto.setClienteTelefono(pedido.getCliente().getTelefono());
        dto.setDireccion(pedido.getCliente().getDireccion());
        dto.setFecha(pedido.getFecha());
        dto.setEstado(pedido.getEstado());
        dto.setTotal(pedido.getTotal());
        dto.setNotas(pedido.getNotas());

        if (pedido.getDetalles() != null) {
            List<DetallePedidoResponseDTO> detalles = pedido.getDetalles().stream().map(d -> {
                DetallePedidoResponseDTO detDto = new DetallePedidoResponseDTO();
                detDto.setProductoNombre(d.getProducto().getNombre());
                detDto.setCantidad(d.getCantidad());
                detDto.setPrecioUnitario(d.getPrecioUnitario());
                detDto.setSubtotal(d.getPrecioUnitario().multiply(new BigDecimal(d.getCantidad())));
                return detDto;
            }).collect(Collectors.toList());
            dto.setDetalles(detalles);
        }
        return dto;
    }
}
