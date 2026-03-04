package co.edu.uniquindio.proyectogestioneventos;

import co.edu.uniquindio.proyectogestioneventos.datautil.DataUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyApplication extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        DataUtil.inicializarDatos();
        mainStage = stage;
        goToLogin();
        mainStage.show();
    }

    public static void goToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("login/loginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            mainStage.setTitle("Gestión de Eventos - Iniciar Sesión");
            mainStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("No se pudo cargar la vista de login.");
        }
    }

    public static void cambiarEscena(String fxml) {
        try {
            String viewName = fxml.substring(0, 1).toUpperCase() + fxml.substring(1);
            FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource(fxml + "/" + fxml + "View.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            mainStage.setTitle("Gestión de Eventos - Panel de " + viewName);
            mainStage.setScene(scene);
        } catch (IOException e) {
            // It's good practice to log the exception.
            e.printStackTrace();
            System.err.println("No se pudo cargar la vista: " + fxml);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
