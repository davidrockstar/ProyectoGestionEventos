package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.enums.Rol;
import co.edu.uniquindio.proyectogestioneventos.service.IUsuarioService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.UsuarioServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistroViewController {

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtTelefono;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private PasswordField txtConfirmarContrasena;

    @FXML
    void onRegistrarUsuarioClick(ActionEvent event) {
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String telefono = txtTelefono.getText();
        String contrasena = txtContrasena.getText();
        String confirmarContrasena = txtConfirmarContrasena.getText();

        if (nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            mostrarAlerta("Error de Validación", "Todos los campos son obligatorios.", Alert.AlertType.WARNING);
            return;
        }
        if (!contrasena.equals(confirmarContrasena)) {
            mostrarAlerta("Error de Validación", "Las contraseñas no coinciden.", Alert.AlertType.WARNING);
            return;
        }

        try {
            usuarioService.registrarUsuario(nombre, correo, telefono, contrasena, Rol.CLIENTE);
            mostrarAlerta("Registro Exitoso", "Usuario registrado correctamente.", Alert.AlertType.INFORMATION);
            // Cerrar ventana de registro
            Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            mostrarAlerta("Error de Registro", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onVolverLoginClick(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close(); // Cierra la ventana de registro y vuelve al login
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
