package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Optional;

public class AdministradorViewController {

    @FXML
    private Label welcomeLabel;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        welcomeLabel.setText("Bienvenido, " + usuario.getNombre());
    }

    @FXML
    void onCerrarSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de Cierre de Sesión");
        alert.setHeaderText("¿Está seguro de que desea cerrar la sesión?");
        alert.setContentText("Será redirigido a la pantalla de inicio de sesión.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MyApplication.goToLogin();
        }
    }
}
