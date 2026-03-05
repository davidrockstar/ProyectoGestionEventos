package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.controller.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginViewController {

    private final LoginController loginController = new LoginController();

    @FXML
    private Button btnIngresar;

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

    private void iniciarSesion() {
        String tipoUsuarioSeleccionado = cbUsuario.getValue();
        String contrasena = txtContrasena.getText();

        String idUsuario = null;
        if ("Administrador".equals(tipoUsuarioSeleccionado)) {
            idUsuario = "admin"; // ID del administrador de prueba
        } else if ("Usuario".equals(tipoUsuarioSeleccionado)) {
            idUsuario = "cliente1"; // ID del cliente de prueba
        }

        String resultado = loginController.procesarLogin(idUsuario, contrasena);

        switch (resultado) {
            case "ADMINISTRADOR":
                MyApplication.cambiarEscena("administrador");
                break;
            case "CLIENTE":
                MyApplication.cambiarEscena("usuario");
                break;
            case "ERROR_USUARIO_VACIO":
                mostrarAlerta("Error de Validación", "Seleccione un tipo de usuario.", Alert.AlertType.WARNING);
                break;
            case "ERROR_PASSWORD_VACIO":
                mostrarAlerta("Error de Validación", "El campo de contraseña no puede estar vacío.", Alert.AlertType.WARNING);
                break;
            case "ERROR_CREDENCIALES":
                mostrarAlerta("Datos Incorrectos", "El usuario o la contraseña no son válidos.", Alert.AlertType.ERROR);
                break;
            case "ERROR_TIPO_USUARIO_DESCONOCIDO":
                mostrarAlerta("Error Interno", "El tipo de usuario no se pudo determinar.", Alert.AlertType.ERROR);
                break;
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
