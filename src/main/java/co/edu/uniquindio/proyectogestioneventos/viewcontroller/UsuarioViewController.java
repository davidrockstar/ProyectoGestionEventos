package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.controller.ExplorarEventosController;
import co.edu.uniquindio.proyectogestioneventos.controller.HistorialComprasController;
import co.edu.uniquindio.proyectogestioneventos.controller.PerfilUsuarioController;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class UsuarioViewController {

    private Usuario usuarioActual;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
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

    @FXML
    void onExplorarEventosClick() {
        abrirVentana("/co/edu/uniquindio/proyectogestioneventos/usuario/ExplorarEventosView.fxml", "Explorar Eventos");
    }

    @FXML
    void onMisComprasClick() {
        abrirVentana("/co/edu/uniquindio/proyectogestioneventos/usuario/MisComprasView.fxml", "Mis Compras");
    }

    @FXML
    void onHistorialComprasClick() {
        abrirVentana("/co/edu/uniquindio/proyectogestioneventos/usuario/HistorialComprasView.fxml", "Historial de Compras");
    }

    @FXML
    void onPerfilUsuarioClick() {
        abrirVentana("/co/edu/uniquindio/proyectogestioneventos/usuario/PerfilUsuarioView.fxml", "Gestionar Perfil");
    }

    private void abrirVentana(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
