module com.finanzas {
    // Módulos requeridos para JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    
    // Para la base de datos H2
    requires java.sql;
    requires com.h2database;

    // Permite que JavaFX acceda a tus clases
    opens com.finanzas to javafx.fxml;
    opens com.finanzas.controlador to javafx.fxml;
    opens com.finanzas.modelo to javafx.base;
    
    // Exporta tus paquetes para que sean accesibles
    exports com.finanzas;
    exports com.finanzas.controlador;
    exports com.finanzas.modelo;
}