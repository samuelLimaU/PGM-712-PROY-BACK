package com.multimedia.spring.multimed.infraestructura.controller;

import com.multimedia.spring.multimed.aplicacion.dto.ProductoResponseDTO;
import com.multimedia.spring.multimed.aplicacion.service.ProductoService;
import com.multimedia.spring.multimed.aplicacion.service.PromocionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/catalogo")
public class CatalogoController {

    private final ProductoService productoService;
    private final PromocionService promocionService;

    public CatalogoController(ProductoService productoService, PromocionService promocionService) {
        this.productoService = productoService;
        this.promocionService = promocionService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listar() {
        // 1. Obtener todos los productos y filtrar los activos
        List<ProductoResponseDTO> activos = productoService.listar()
                .stream()
                .filter(p -> Boolean.TRUE.equals(p.activo))
                .collect(Collectors.toList());
        
        // 2. Aplicar lógica de promociones si existen
        List<ProductoResponseDTO> conPromociones = promocionService.aplicarPromociones(activos);
        
        return ResponseEntity.ok(conPromociones);
    }
}
