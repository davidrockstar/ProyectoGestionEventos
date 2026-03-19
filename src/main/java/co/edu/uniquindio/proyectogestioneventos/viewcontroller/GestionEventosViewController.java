package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class GestionEventosViewController {

    @FXML
    private TableView<Evento> tablaEventos;

    @FXML
    private void initialize() {
        // Aquí se inicializarían las columnas de la tabla y se cargarían los eventos
        // Por ahora, solo un placeholder
        System.out.println("GestionEventosView inicializada.");
    }

    @FXML
    void onCreateEventoClick(ActionEvent event) {
        mostrarAlerta("Información", "Crear Evento", "Funcionalidad para crear evento no implementada.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onEditEventoClick(ActionEvent event) {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            mostrarAlerta("Información", "Editar Evento", "Funcionalidad para editar evento no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Editar Evento", "Por favor, seleccione un evento para editar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onDeleteEventoClick(ActionEvent event) {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            mostrarAlerta("Información", "Eliminar Evento", "Funcionalidad para eliminar evento no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Eliminar Evento", "Por favor, seleccione un evento para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onPublicarEventoClick(ActionEvent event) {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            mostrarAlerta("Información", "Publicar Evento", "Funcionalidad para publicar evento no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Publicar Evento", "Por favor, seleccione un evento para publicar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onPausarEventoClick(ActionEvent event) {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            mostrarAlerta("Información", "Pausar Evento", "Funcionalidad para pausar evento no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Pausar Evento", "Por favor, seleccione un evento para pausar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onCancelarEventoClick(ActionEvent event) {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            mostrarAlerta("Información", "Cancelar Evento", "Funcionalidad para cancelar evento no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Cancelar Evento", "Por favor, seleccione un evento para cancelar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) tablaEventos.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
