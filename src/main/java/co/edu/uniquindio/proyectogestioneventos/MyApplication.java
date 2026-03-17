package co.edu.uniquindio.proyectogestioneventos;

import co.edu.uniquindio.proyectogestioneventos.controller.HistorialComprasController;
import co.edu.uniquindio.proyectogestioneventos.controller.PerfilUsuarioController;
import co.edu.uniquindio.proyectogestioneventos.viewcontroller.UsuarioViewController;
import co.edu.uniquindio.proyectogestioneventos.datautil.DataUtil;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyApplication extends Application {

    public static Stage mainStage;
    private static Usuario usuarioLogueado;

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
            usuarioLogueado = null; // Limpiar usuario logueado al volver al login
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("No se pudo cargar la vista de login.");
        }
    }

    public static void cambiarEscenaUsuario(String fxmlName, Usuario usuario) {
        try {
            usuarioLogueado = usuario;
            FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("usuario/" + fxmlName + ".fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            Object controller = fxmlLoader.getController();
            if (controller instanceof UsuarioViewController) {
                ((UsuarioViewController) controller).setUsuario(usuario);
            }

            mainStage.setTitle("Gestión de Eventos - Panel de Usuario");
            mainStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("No se pudo cargar la vista: " + fxmlName);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
