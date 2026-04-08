package com.multimedia.spring.multimed.infraestructura.controller;

import com.multimedia.spring.multimed.aplicacion.dto.ProductoResponseDTO;
import com.multimedia.spring.multimed.aplicacion.service.ProductoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/catalogo")
public class CatalogoController {

    private final ProductoService productoService;

    public CatalogoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listar() {
        List<ProductoResponseDTO> activos = productoService.listar()
                .stream()
                .filter(p -> Boolean.TRUE.equals(p.activo))
                .collect(Collectors.toList());
        return ResponseEntity.ok(activos);
    }
}