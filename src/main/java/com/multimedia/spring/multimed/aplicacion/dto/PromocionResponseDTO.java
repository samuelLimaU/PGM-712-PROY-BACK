package com.multimedia.spring.multimed.aplicacion.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PromocionResponseDTO {

    public Long id;
    public String titulo;
    public String descripcion;
    public String imagenUrl;
    public String tipo;
    public BigDecimal valor;
    public LocalDate fechaInicio;
    public LocalDate fechaFin;
    public Boolean activo;
    public List<Long> productoIds;
}
