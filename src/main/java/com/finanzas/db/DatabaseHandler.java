package com.finanzas.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabaseHandler {
    // Nombre de la base de datos
    private static final String DB_NAME = "finanzas_db";
    
    // Obtiene la ruta correcta para la base de datos en Mac
    private static String getDbPath() {
        String userHome = System.getProperty("user.home");
        // Crea la carpeta en Application Support para seguir las convenciones de Mac
        Path dbPath = Paths.get(userHome, "Library", "Application Support", "GestorFinanzas");
        // Crea los directorios si no existen
        dbPath.toFile().mkdirs();
        return dbPath.resolve(DB_NAME).toString();
    }
    
    // URL de conexión a la base de datos
    private static final String CONNECTION_URL = "jdbc:h2:" + getDbPath();
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Obtiene una conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
    }

    // Inicializa la base de datos con las tablas necesarias
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Crear tabla de transacciones
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS transacciones (
                    id IDENTITY PRIMARY KEY,
                    fecha TIMESTAMP NOT NULL,
                    monto DECIMAL(10,2) NOT NULL,
                    tipo VARCHAR(50) NOT NULL,
                    categoria VARCHAR(100) NOT NULL,
                    descripcion VARCHAR(255),
                    CONSTRAINT tipo_check CHECK (tipo IN ('INGRESO', 'GASTO'))
                )
            """);
            
            // Crear tabla de categorías
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS categorias (
                    id IDENTITY PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL UNIQUE,
                    tipo VARCHAR(50) NOT NULL,
                    CONSTRAINT categoria_tipo_check CHECK (tipo IN ('INGRESO', 'GASTO'))
                )
            """);
            
            // Crear índices para mejorar el rendimiento
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_transacciones_fecha ON transacciones(fecha)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_transacciones_tipo ON transacciones(tipo)");
            
            // Insertar categorías predefinidas si la tabla está vacía
            stmt.execute("""
                MERGE INTO categorias (nombre, tipo) 
                KEY(nombre)
                VALUES 
                ('Salario', 'INGRESO'),
                ('Inversiones', 'INGRESO'),
                ('Negocio', 'INGRESO'),
                ('Freelance', 'INGRESO'),
                ('Otros Ingresos', 'INGRESO'),
                ('Alimentación', 'GASTO'),
                ('Transporte', 'GASTO'),
                ('Servicios', 'GASTO'),
                ('Entretenimiento', 'GASTO'),
                ('Salud', 'GASTO'),
                ('Educación', 'GASTO'),
                ('Vivienda', 'GASTO'),
                ('Ropa', 'GASTO'),
                ('Ahorro/Inversión', 'GASTO'),
                ('Otros Gastos', 'GASTO')
            """);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al inicializar la base de datos", e);
        }
    }

    // Método para verificar si la base de datos está disponible
    public static boolean checkDatabaseConnection() {
        try (Connection conn = getConnection()) {
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para limpiar la base de datos (útil para testing)
    public static void clearDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute("DELETE FROM transacciones");
            stmt.execute("DELETE FROM categorias");
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al limpiar la base de datos", e);
        }
    }

    // Método para cerrar la base de datos (útil al cerrar la aplicación)
    public static void closeDatabase() {
        try {
            // H2 tiene un método especial para cerrar todas las conexiones
            DriverManager.getConnection(CONNECTION_URL + ";SHUTDOWN=TRUE", USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}