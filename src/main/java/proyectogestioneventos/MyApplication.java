package proyectogestioneventos;

import proyectogestioneventos.viewcontroller.AdminDashboardViewController;
import proyectogestioneventos.viewcontroller.UsuarioViewController;
import proyectogestioneventos.datautil.DataUtil;
import proyectogestioneventos.model.Usuario;
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
            FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("/proyectogestioneventos/login/loginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            mainStage.setTitle("Gestión de Eventos - Iniciar Sesión");
            mainStage.setScene(scene);
            setUsuarioLogueado(null); // Usar el setter
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cambiarEscenaUsuario(String fxmlName, Usuario usuario) {
        try {
            setUsuarioLogueado(usuario); // Usar el setter
            FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("/proyectogestioneventos/usuario/cliente/" + fxmlName));
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
            throw new RuntimeException(e);
        }
    }

    public static void cambiarEscenaAdministrador(String fxmlName, Usuario usuario) {
        try {
            setUsuarioLogueado(usuario); // Usar el setter
            FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("/proyectogestioneventos/usuario/administrador/" + fxmlName));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            Object controller = fxmlLoader.getController();
            // El controlador para AdminDashboardView.fxml es AdminDashboardViewController
            // AdministradorViewController es un controlador genérico o una clase base, no el controlador específico del dashboard
            if (controller instanceof AdminDashboardViewController) {
                ((AdminDashboardViewController) controller).setUsuario(usuario);
            }

            mainStage.setTitle("Gestión de Eventos - Panel de Administrador");
            mainStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
