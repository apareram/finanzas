package com.finanzas.modelo;
/*
* Enumeración que representa el tipo de transacción (ingreso o gasto)
 */
public enum TipoTransaccion {
    INGRESO("Ingreso"),
    GASTO("Gasto");

    private final String descripcion;

    TipoTransaccion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}