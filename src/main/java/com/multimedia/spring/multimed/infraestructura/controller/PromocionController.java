package com.multimedia.spring.multimed.infraestructura.controller;

import com.multimedia.spring.multimed.aplicacion.dto.PromocionRequestDTO;
import com.multimedia.spring.multimed.aplicacion.dto.PromocionResponseDTO;
import com.multimedia.spring.multimed.aplicacion.service.PromocionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/promociones")
public class PromocionController {

    private final PromocionService promocionService;

    public PromocionController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    // ── POST /api/promociones (solo ADMIN) ──────────────────────────
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<PromocionResponseDTO> crear(@RequestBody PromocionRequestDTO dto) {
        PromocionResponseDTO response = promocionService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ── GET /api/promociones (público) ──────────────────────────────
    @GetMapping
    public ResponseEntity<List<PromocionResponseDTO>> listar() {
        return ResponseEntity.ok(promocionService.listar());
    }

    // ── GET /api/promociones/activas (público) ───────────────────────
    @GetMapping("/activas")
    public ResponseEntity<List<PromocionResponseDTO>> listarActivas() {
        return ResponseEntity.ok(promocionService.listarActivas());
    }

    // ── GET /api/promociones/{id} (público) ─────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<PromocionResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(promocionService.buscarPorId(id));
    }

    // ── PUT /api/promociones/{id} (solo ADMIN) ───────────────────────
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<PromocionResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody PromocionRequestDTO dto) {
        return ResponseEntity.ok(promocionService.actualizar(id, dto));
    }

    // ── DELETE /api/promociones/{id} (solo ADMIN) ───────────────────
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        promocionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ── Manejo de excepciones de validación ──────────────────────────
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleValidacion(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }
}
