package com.multimedia.spring.multimed.aplicacion.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductoResponseDTO {

    public Long id;
    public String nombre;
    public String descripcion;
    public BigDecimal precio;
    public Integer stock;
    public String imagenUrl;
    public Boolean activo;
    public LocalDateTime createdAt;

    // Campos de Promoción 
    public Boolean promocionActiva = false;
    public BigDecimal precioOferta;
    public String tipoPromocion; // BANNER, DESCUENTO_PORCENTAJE, DESCUENTO_FIJO
    public String tituloPromocion;
}
