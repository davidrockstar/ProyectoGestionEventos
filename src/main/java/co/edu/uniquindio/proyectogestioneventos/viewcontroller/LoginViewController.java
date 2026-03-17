package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Administrador;
import co.edu.uniquindio.proyectogestioneventos.model.Cliente;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.service.IUsuarioService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.UsuarioServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginViewController {

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private ComboBox<String> cbUsuario;

    @FXML
    void onIngresar(ActionEvent event) {
        iniciarSesion();
    }

    @FXML
    void onPasswordEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            iniciarSesion();
        }
    }

    @FXML
    void onRegistrarseClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/view/RegistroView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registro de Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(MyApplication.mainStage);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la pantalla de registro.", Alert.AlertType.ERROR);
        }
    }

    private void iniciarSesion() {
        String tipoUsuarioSeleccionado = cbUsuario.getValue();
        String contrasena = txtContrasena.getText();

        if (tipoUsuarioSeleccionado == null) {
            mostrarAlerta("Error de Validación", "Seleccione un tipo de usuario.", Alert.AlertType.WARNING);
            return;
        }

        if (contrasena.isEmpty()) {
            mostrarAlerta("Error de Validación", "El campo de contraseña no puede estar vacío.", Alert.AlertType.WARNING);
            return;
        }

        String email = null;
        if ("Administrador".equals(tipoUsuarioSeleccionado)) {
            email = "admin@eventos.com"; // Email del administrador de prueba
        } else if ("Usuario".equals(tipoUsuarioSeleccionado)) {
            email = "ana@email.com"; // Email del cliente de prueba
        }

        Optional<Usuario> usuarioOptional = usuarioService.autenticarUsuario(email, contrasena);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (usuario instanceof Administrador) {
                // Lógica para la escena del administrador
                System.out.println("Login de administrador exitoso.");
            } else if (usuario instanceof Cliente) {
                MyApplication.cambiarEscenaUsuario("usuarioView", usuario);
            }
        } else {
            mostrarAlerta("Datos Incorrectos", "El correo o la contraseña no son válidos.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
