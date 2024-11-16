package com.finanzas.modelo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Resumen {
    private BigDecimal totalIngresos = BigDecimal.ZERO;
    private BigDecimal totalGastos = BigDecimal.ZERO;
    private BigDecimal balance = BigDecimal.ZERO;
    private Map<String, BigDecimal> gastosPorCategoria = new HashMap<>();
    private Map<String, BigDecimal> ingresosPorCategoria = new HashMap<>();

    // Getters y setters
    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigDecimal totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public BigDecimal getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(BigDecimal totalGastos) {
        this.totalGastos = totalGastos;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Map<String, BigDecimal> getGastosPorCategoria() {
        return gastosPorCategoria;
    }

    public void setGastosPorCategoria(Map<String, BigDecimal> gastosPorCategoria) {
        this.gastosPorCategoria = gastosPorCategoria;
    }

    public Map<String, BigDecimal> getIngresosPorCategoria() {
        return ingresosPorCategoria;
    }

    public void setIngresosPorCategoria(Map<String, BigDecimal> ingresosPorCategoria) {
        this.ingresosPorCategoria = ingresosPorCategoria;
    }
}