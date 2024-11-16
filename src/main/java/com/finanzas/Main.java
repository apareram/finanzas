package com.finanzas;

import com.finanzas.db.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inicializar la base de datos
        DatabaseHandler.initializeDatabase();

        // Cargar el FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vistas/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        
        // Configurar la ventana principal
        primaryStage.setTitle("Gestor de Finanzas");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Opcional: hacer la ventana de tamaño fijo
        primaryStage.show();
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