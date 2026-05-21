package com.multimedia.spring.multimed.infraestructura.controller;

import com.multimedia.spring.multimed.aplicacion.dto.PageResponse;
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
    public ResponseEntity<?> listar(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        
        if (page != null && size != null) {
            PageResponse<ProductoResponseDTO> pagedResponse = productoService.listarActivosPaginados(page, size);
            List<ProductoResponseDTO> conPromociones = promocionService.aplicarPromociones(pagedResponse.getContent());
            pagedResponse.setContent(conPromociones);
            return ResponseEntity.ok(pagedResponse);
        }

        // Fallback: listar todos los activos
        List<ProductoResponseDTO> activos = productoService.listar()
                .stream()
                .filter(p -> Boolean.TRUE.equals(p.activo))
                .collect(Collectors.toList());
        
        List<ProductoResponseDTO> conPromociones = promocionService.aplicarPromociones(activos);
        return ResponseEntity.ok(conPromociones);
    }
}
