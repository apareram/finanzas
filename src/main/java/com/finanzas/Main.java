package com.finanzas;

import com.finanzas.db.DatabaseHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Mostrar splash screen
        Stage splashStage = new Stage(StageStyle.UNDECORATED);
        ImageView splash = new ImageView(new Image(getClass().getResourceAsStream("/images/splash.png")));
        splash.setFitWidth(400);
        splash.setFitHeight(300);
        splashStage.setScene(new Scene(new StackPane(splash), 400, 300));
        splashStage.show();

        // Inicializar la aplicación en segundo plano
        Platform.runLater(() -> {
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
                
                // Cerrar el splash y mostrar la ventana principal
                splashStage.close();
                primaryStage.show();
                
            } catch (Exception e) {
                System.err.println("Error al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();
                splashStage.close();
            }
        });
    }

    @Override
    public void stop() {
        DatabaseHandler.closeDatabase();
    }

    public static void main(String[] args) {
        launch(args);
    }
}