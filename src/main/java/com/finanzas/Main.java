package com.finanzas;

import com.finanzas.db.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Inicializar la base de datos
            DatabaseHandler.initializeDatabase();
            System.out.println("Base de datos inicializada correctamente");

            // Cargar el FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vistas/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            
            // Configurar la ventana principal
            primaryStage.setTitle("Gestor de Finanzas");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void stop() {
        // Cerrar la base de datos al salir
        DatabaseHandler.closeDatabase();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
