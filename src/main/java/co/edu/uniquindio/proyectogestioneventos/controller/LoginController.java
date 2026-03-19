package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Administrador;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.service.IUsuarioService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.UsuarioServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtContrasena;

    @FXML
    void onLoginClick(ActionEvent event) {
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Error de Validación", "El correo y la contraseña son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        Optional<Usuario> usuarioOptional = usuarioService.autenticarUsuario(correo, contrasena);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            MyApplication.setUsuarioLogueado(usuario); // Guardar el usuario logueado

            if (usuario instanceof Administrador) {
                MyApplication.cambiarEscenaAdministrador("AdminDashboardView.fxml", usuario);
            } else {
                MyApplication.cambiarEscenaUsuario("usuarioView.fxml", usuario);
            }
        } else {
            mostrarAlerta("Error de Autenticación", "Correo o contraseña incorrectos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onGoToRegisterClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/cliente/RegistroView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registro de Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(MyApplication.getMainStage());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la pantalla de registro.", Alert.AlertType.ERROR);
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
