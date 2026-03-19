package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
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

public class AdminDashboardViewController {

    private Usuario adminActual;

    public void setUsuario(Usuario admin) {
        this.adminActual = admin;
        // Aquí podrías actualizar la UI con información del administrador si fuera necesario
    }

    @FXML
    void onGestionarUsuariosClick(ActionEvent event) {
        abrirVentana("GestionUsuariosView.fxml", "Gestionar Usuarios");
    }

    @FXML
    void onGestionarEventosClick(ActionEvent event) {
        abrirVentana("GestionEventosView.fxml", "Gestionar Eventos");
    }

    @FXML
    void onGestionarRecintosClick(ActionEvent event) {
        abrirVentana("GestionRecintosView.fxml", "Gestionar Recintos");
    }

    @FXML
    void onGestionarZonasClick(ActionEvent event) {
        abrirVentana("GestionZonasView.fxml", "Gestionar Zonas");
    }

    @FXML
    void onGestionarAsientosClick(ActionEvent event) {
        abrirVentana("GestionAsientosView.fxml", "Gestionar Asientos");
    }

    @FXML
    void onGestionarComprasClick(ActionEvent event) {
        abrirVentana("GestionComprasAdminView.fxml", "Gestionar Compras");
    }

    @FXML
    void onGestionarIncidenciasClick(ActionEvent event) {
        abrirVentana("IncidenciasAdminView.fxml", "Gestionar Incidencias");
    }

    @FXML
    void onPanelMetricasClick(ActionEvent event) {
        abrirVentana("PanelMetricasAdminView.fxml", "Panel de Métricas");
    }

    @FXML
    void onCerrarSesionClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de Cierre de Sesión");
        alert.setHeaderText("¿Está seguro de que desea cerrar la sesión?");
        alert.setContentText("Será redirigido a la pantalla de inicio de sesión.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MyApplication.goToLogin();
        }
    }

    private void abrirVentana(String fxmlName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/administrador/" + fxmlName));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la ventana", "Hubo un error al intentar abrir " + title + ".", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
