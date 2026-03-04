package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.controller.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;

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
        String usuario = cbUsuario.getValue();
        String contrasena = txtContrasena.getText();
        
        // Use "admin" for "Administrador" and "cliente" for "Usuario"
        String id = "cliente";
        if ("Administrador".equals(usuario)) {
            id = "admin";
        }

        String resultado = loginController.procesarLogin(id, contrasena);

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
                mostrarAlerta("Datos Incorrectos", "La contraseña no es válida.", Alert.AlertType.ERROR);
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
