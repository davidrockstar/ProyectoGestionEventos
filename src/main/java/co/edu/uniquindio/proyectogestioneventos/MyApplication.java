package co.edu.uniquindio.proyectogestioneventos;

import co.edu.uniquindio.proyectogestioneventos.viewcontroller.AdministradorViewController;
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

    private static Stage mainStage; // Hacer private para forzar el uso del getter
    private static Usuario usuarioLogueado;

    @Override
    public void start(Stage stage) throws IOException {
        DataUtil.inicializarDatos();
        mainStage = stage;
        goToLogin();
        mainStage.show();
    }

    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    // Nuevo método setter para usuarioLogueado
    public static void setUsuarioLogueado(Usuario usuario) {
        MyApplication.usuarioLogueado = usuario;
    }

    // Nuevo método getter para mainStage
    public static Stage getMainStage() {
        return mainStage;
    }

    public static void goToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("login/loginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            mainStage.setTitle("Gestión de Eventos - Iniciar Sesión");
            mainStage.setScene(scene);
            setUsuarioLogueado(null); // Usar el setter
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("No se pudo cargar la vista de login.");
        }
    }

    public static void cambiarEscenaUsuario(String fxmlName, Usuario usuario) {
        try {
            setUsuarioLogueado(usuario); // Usar el setter
            FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("usuario/cliente/" + fxmlName));
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
            throw new RuntimeException(e);
        }
    }

    public static void cambiarEscenaAdministrador(String fxmlName, Usuario usuario) {
        try {
            setUsuarioLogueado(usuario); // Usar el setter
            FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("usuario/administrador/" + fxmlName));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            Object controller = fxmlLoader.getController();
            if (controller instanceof AdministradorViewController) {
                ((AdministradorViewController) controller).setUsuario(usuario);
            } else if (controller instanceof UsuarioViewController) { // Añadido para manejar el caso de que el admin use la vista de usuario
                ((UsuarioViewController) controller).setUsuario(usuario);
            }

            mainStage.setTitle("Gestión de Eventos - Panel de Administrador");
            mainStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("No se pudo cargar la vista: " + fxmlName);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
