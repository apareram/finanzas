package com.finanzas.modelo;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/*
* Clase que representa una transacción
*/
public class Transaccion {
    private Long id;
    private LocalDateTime fecha;
    private BigDecimal monto;
    private TipoTransaccion tipo;
    private String categoria;
    private String descripcion;

    // Constructor
    public Transaccion(BigDecimal monto, TipoTransaccion tipo, String categoria, String descripcion) {
        this.fecha = LocalDateTime.now();
        this.monto = monto;
        this.tipo = tipo;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public void setTipo(TipoTransaccion tipo) {
        this.tipo = tipo;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}