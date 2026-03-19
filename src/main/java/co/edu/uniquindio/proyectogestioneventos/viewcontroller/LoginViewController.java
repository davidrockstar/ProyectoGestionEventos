package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.model.enums.Rol;
import co.edu.uniquindio.proyectogestioneventos.service.IUsuarioService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.UsuarioServiceImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginViewController {

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private ComboBox<Rol> cbxRol;

    @FXML
    void initialize() {
        cbxRol.setItems(FXCollections.observableArrayList(Rol.values()));
    }

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
            stage.initOwner(MyApplication.getMainStage());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la pantalla de registro.", Alert.AlertType.ERROR);
        }
    }

    private void iniciarSesion() {
        String correo = txtUsuario.getText();
        String contrasena = txtContrasena.getText();
        Rol rol = cbxRol.getValue();

        if (correo.isEmpty() || contrasena.isEmpty() || rol == null) {
            mostrarAlerta("Error de Validación", "Todos los campos son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        Optional<Usuario> usuarioOptional = usuarioService.autenticarUsuario(correo, contrasena);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (rol == Rol.ADMINISTRADOR && usuario.getRol() == Rol.ADMINISTRADOR) {
                MyApplication.cambiarEscenaAdministrador("administradorView.fxml", usuario);
            } else if (rol == Rol.CLIENTE && usuario.getRol() == Rol.CLIENTE) {
                MyApplication.cambiarEscenaUsuario("usuarioView.fxml", usuario);
            }
            else {
                mostrarAlerta("Error de Autenticación", "Correo, contraseña o rol incorrectos.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Error de Autenticación", "Correo, contraseña o rol incorrectos.", Alert.AlertType.ERROR);
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
