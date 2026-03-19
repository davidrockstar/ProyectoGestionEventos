package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.controller.PerfilUsuarioController;
import co.edu.uniquindio.proyectogestioneventos.model.Administrador;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
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
        abrirVentana("explorarEventosView.fxml", "Explorar Eventos");
    }

    @FXML
    void onMisComprasClick() {
        abrirVentana("MisComprasView.fxml", "Mis Compras");
    }

    @FXML
    void onHistorialComprasClick() {
        abrirVentana("historialComprasView.fxml", "Historial de Compras");
    }

    @FXML
    void onPerfilUsuarioClick() {
        try {
            String basePath = (usuarioActual instanceof Administrador) ? "administrador/" : "cliente/";
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/" + basePath + "perfilUsuarioView.fxml"));
            Parent root = loader.load();

            PerfilUsuarioController controller = loader.getController();
            controller.setUsuario(this.usuarioActual);

            Stage stage = new Stage();
            stage.setTitle("Gestionar Perfil");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirVentana(String fxmlName, String title) {
        try {
            String basePath = (usuarioActual instanceof Administrador) ? "administrador/" : "cliente/";
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/" + basePath + fxmlName));
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
