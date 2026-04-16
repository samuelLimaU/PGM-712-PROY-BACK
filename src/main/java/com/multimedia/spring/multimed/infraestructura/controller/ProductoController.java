package com.multimedia.spring.multimed.infraestructura.controller;

import com.multimedia.spring.multimed.aplicacion.dto.ProductoRequestDTO;
import com.multimedia.spring.multimed.aplicacion.dto.ProductoResponseDTO;
import com.multimedia.spring.multimed.aplicacion.service.FileStorageService;
import com.multimedia.spring.multimed.aplicacion.service.ProductoService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.nio.file.Path;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final FileStorageService fileStorageService;

    public ProductoController(ProductoService productoService, FileStorageService fileStorageService) {
        this.productoService = productoService;
        this.fileStorageService = fileStorageService;
    }

    // POST agregado /productos
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoResponseDTO> crear(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("stock") Integer stock,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        ProductoRequestDTO dto = new ProductoRequestDTO();
        dto.nombre = nombre;
        dto.descripcion = descripcion;
        dto.precio = precio;
        dto.stock = stock;

        String imagenUrl = null;
        if (imagen != null && !imagen.isEmpty()) {
            imagenUrl = fileStorageService.guardar(imagen);
        }

        ProductoResponseDTO response = productoService.crear(dto, imagenUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT aun no testeado /productos/{id}
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("stock") Integer stock,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        ProductoRequestDTO dto = new ProductoRequestDTO();
        dto.nombre = nombre;
        dto.descripcion = descripcion;
        dto.precio = precio;
        dto.stock = stock;

        String imagenUrl = null;
        if (imagen != null && !imagen.isEmpty()) {
            imagenUrl = fileStorageService.guardar(imagen);
        }

        ProductoResponseDTO response = productoService.actualizar(id, dto, imagenUrl);
        return ResponseEntity.ok(response);
    }

    // GET funcional de momento /productos
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    // GET funcional de momento /productos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    // DELETE aun no testeado /productos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Manejo de excepciones
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleValidacion(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @GetMapping("/debug-path")
    public String debugPath() {
        return "user.dir = " + System.getProperty("user.dir");
    }

    @GetMapping("/test-imagen")
    public void testImagen(HttpServletResponse response) throws Exception {
        Path path = Paths.get("uploads/productos/8e4f8e11-3776-4477-ab71-638b39665326.jpeg");
        response.setContentType("image/jpeg");
        Files.copy(path, response.getOutputStream());
    }
}
