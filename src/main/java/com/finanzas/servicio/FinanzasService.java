package com.finanzas.servicio;

import com.finanzas.modelo.*;
import com.finanzas.db.DatabaseHandler;

import java.sql.*;
import java.math.BigDecimal;
import java.util.*;

public class FinanzasService {
    
    // Categorías predefinidas
    public static final List<String> CATEGORIAS_INGRESO = Arrays.asList(
        "Salario", 
        "Inversiones", 
        "Negocio", 
        "Freelance", 
        "Otros Ingresos"
    );
    
    public static final List<String> CATEGORIAS_GASTO = Arrays.asList(
        "Alimentación",
        "Transporte",
        "Servicios",
        "Entretenimiento",
        "Salud",
        "Educación",
        "Vivienda",
        "Ropa",
        "Ahorro/Inversión",
        "Otros Gastos"
    );

    // Agregar una nueva transacción
    public void agregarTransaccion(Transaccion transaccion) {
        String sql = """
            INSERT INTO transacciones (fecha, monto, tipo, categoria, descripcion)
            VALUES (?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(transaccion.getFecha()));
            stmt.setBigDecimal(2, transaccion.getMonto());
            stmt.setString(3, transaccion.getTipo().name());
            stmt.setString(4, transaccion.getCategoria());
            stmt.setString(5, transaccion.getDescripcion());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la transacción", e);
        }
    }

    // Obtener resumen mensual
    public Resumen obtenerResumenMensual(int mes, int año) {
        String sql = """
            SELECT tipo, categoria, SUM(monto) as total 
            FROM transacciones 
            WHERE MONTH(fecha) = ? AND YEAR(fecha) = ?
            GROUP BY tipo, categoria
        """;
        
        Resumen resumen = new Resumen();
        Map<String, BigDecimal> gastosPorCategoria = new HashMap<>();
        Map<String, BigDecimal> ingresosPorCategoria = new HashMap<>();
        BigDecimal totalIngresos = BigDecimal.ZERO;
        BigDecimal totalGastos = BigDecimal.ZERO;

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, mes);
            stmt.setInt(2, año);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                String categoria = rs.getString("categoria");
                BigDecimal total = rs.getBigDecimal("total");
                
                if (TipoTransaccion.INGRESO.name().equals(tipo)) {
                    ingresosPorCategoria.put(categoria, total);
                    totalIngresos = totalIngresos.add(total);
                } else {
                    gastosPorCategoria.put(categoria, total);
                    totalGastos = totalGastos.add(total);
                }
            }
            
            resumen.setTotalIngresos(totalIngresos);
            resumen.setTotalGastos(totalGastos);
            resumen.setBalance(totalIngresos.subtract(totalGastos));
            resumen.setGastosPorCategoria(gastosPorCategoria);
            resumen.setIngresosPorCategoria(ingresosPorCategoria);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el resumen mensual", e);
        }
        
        return resumen;
    }

    // Obtener resumen anual
    public Resumen obtenerResumenAnual(int año) {
        String sql = """
            SELECT tipo, categoria, SUM(monto) as total 
            FROM transacciones 
            WHERE YEAR(fecha) = ?
            GROUP BY tipo, categoria
        """;
        
        // Similar a obtenerResumenMensual pero sin filtro de mes
        // ... (implementación similar)
        return obtenerResumenPorPeriodo(sql, año);
    }

    // Obtener últimas transacciones
    public List<Transaccion> obtenerUltimasTransacciones(int cantidad) {
        String sql = """
            SELECT * FROM transacciones 
            ORDER BY fecha DESC 
            LIMIT ?
        """;
        
        List<Transaccion> transacciones = new ArrayList<>();
        
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cantidad);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaccion t = new Transaccion(
                    rs.getBigDecimal("monto"),
                    TipoTransaccion.valueOf(rs.getString("tipo")),
                    rs.getString("categoria"),
                    rs.getString("descripcion")
                );
                t.setId(rs.getLong("id"));
                t.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                transacciones.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener las últimas transacciones", e);
        }
        
        return transacciones;
    }

    // Obtener total por categoría
    public Map<String, BigDecimal> obtenerTotalPorCategoria(int mes, int año, TipoTransaccion tipo) {
        String sql = """
            SELECT categoria, SUM(monto) as total 
            FROM transacciones 
            WHERE MONTH(fecha) = ? AND YEAR(fecha) = ? AND tipo = ?
            GROUP BY categoria
        """;
        
        Map<String, BigDecimal> totales = new HashMap<>();
        
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, mes);
            stmt.setInt(2, año);
            stmt.setString(3, tipo.name());
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                totales.put(
                    rs.getString("categoria"),
                    rs.getBigDecimal("total")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener totales por categoría", e);
        }
        
        return totales;
    }

    // Eliminar transacción
    public void eliminarTransaccion(Long id) {
        String sql = "DELETE FROM transacciones WHERE id = ?";
        
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la transacción", e);
        }
    }

    // Método auxiliar para resúmenes por periodo
    private Resumen obtenerResumenPorPeriodo(String sql, Object... params) {
        Resumen resumen = new Resumen();
        Map<String, BigDecimal> gastosPorCategoria = new HashMap<>();
        Map<String, BigDecimal> ingresosPorCategoria = new HashMap<>();
        BigDecimal totalIngresos = BigDecimal.ZERO;
        BigDecimal totalGastos = BigDecimal.ZERO;

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                String categoria = rs.getString("categoria");
                BigDecimal total = rs.getBigDecimal("total");
                
                if (TipoTransaccion.INGRESO.name().equals(tipo)) {
                    ingresosPorCategoria.put(categoria, total);
                    totalIngresos = totalIngresos.add(total);
                } else {
                    gastosPorCategoria.put(categoria, total);
                    totalGastos = totalGastos.add(total);
                }
            }
            
            resumen.setTotalIngresos(totalIngresos);
            resumen.setTotalGastos(totalGastos);
            resumen.setBalance(totalIngresos.subtract(totalGastos));
            resumen.setGastosPorCategoria(gastosPorCategoria);
            resumen.setIngresosPorCategoria(ingresosPorCategoria);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el resumen", e);
        }
        
        return resumen;
    }
}