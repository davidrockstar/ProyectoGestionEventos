package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.service.IUsuarioService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.UsuarioServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PerfilUsuarioController {

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();
    private Usuario usuarioActual;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label lblNombreActual;
    @FXML
    private Label lblRol;
    @FXML
    private Label lblCorreoActual;
    @FXML
    private TextField campoNuevoNombre;
    @FXML
    private TextField campoNuevoEmail;
    @FXML
    private PasswordField campoContrasenaActual;
    @FXML
    private PasswordField campoContrasenaNueva;
    @FXML
    private PasswordField campoConfirmarContrasena;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        lblNombreActual.setText(usuario.getNombre());
        lblRol.setText(usuario.getRol().toString());
        lblCorreoActual.setText(usuario.getEmail());
    }

    @FXML
    private void initialize() {
        // Añadir listener para la tecla ESC al panel raíz
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                onCerrarClick(null);
            }
        });
    }

    @FXML
    private void onGuardarCambios() {
        String nuevoNombre = campoNuevoNombre.getText();
        String nuevoEmail = campoNuevoEmail.getText();
        String contrasenaActual = campoContrasenaActual.getText();
        String contrasenaNueva = campoContrasenaNueva.getText();
        String confirmarContrasena = campoConfirmarContrasena.getText();

        if (contrasenaActual.isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar su contraseña actual para guardar los cambios.", Alert.AlertType.WARNING);
            return;
        }

        if (!contrasenaNueva.isEmpty() || !confirmarContrasena.isEmpty()) {
            if (!contrasenaNueva.equals(confirmarContrasena)) {
                mostrarAlerta("Error", "Las nuevas contraseñas no coinciden.", Alert.AlertType.WARNING);
                return;
            }
        }

        try {
            // Si los campos de nuevo nombre o email están vacíos, usar los datos actuales
            String nombreFinal = nuevoNombre.isEmpty() ? usuarioActual.getNombre() : nuevoNombre;
            String emailFinal = nuevoEmail.isEmpty() ? usuarioActual.getEmail() : nuevoEmail;

            usuarioService.actualizarPerfil(usuarioActual.getIdUsuario(), nombreFinal, emailFinal, contrasenaActual, contrasenaNueva);
            
            // Actualizar la información mostrada en la vista
            lblNombreActual.setText(nombreFinal);
            lblCorreoActual.setText(emailFinal);

            // Limpiar los campos de texto después de guardar
            campoNuevoNombre.clear();
            campoNuevoEmail.clear();
            campoContrasenaActual.clear();
            campoContrasenaNueva.clear();
            campoConfirmarContrasena.clear();

            mostrarAlerta("Éxito", "Perfil actualizado con éxito.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onCerrarClick(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
