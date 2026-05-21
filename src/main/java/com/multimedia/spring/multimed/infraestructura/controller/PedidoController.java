package com.multimedia.spring.multimed.infraestructura.controller;

import com.multimedia.spring.multimed.aplicacion.dto.*;
import com.multimedia.spring.multimed.aplicacion.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Long> crearPedido(@RequestBody PedidoRequestDTO request) {
        return ResponseEntity.ok(pedidoService.crearPedido(request));
    }

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        
        if (page != null && size != null) {
            return ResponseEntity.ok(pedidoService.listarPedidosPaginados(page, size));
        }
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedido(id));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstado(@PathVariable Long id, @RequestBody EstadoPedidoRequestDTO request) {
        pedidoService.actualizarEstado(id, request.getEstado());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/pago")
    public ResponseEntity<Void> registrarPago(@PathVariable Long id, @RequestBody PagoRequestDTO request) {
        pedidoService.registrarPago(id, request.getMetodo(), request.getNotas());
        return ResponseEntity.ok().build();
    }
}
