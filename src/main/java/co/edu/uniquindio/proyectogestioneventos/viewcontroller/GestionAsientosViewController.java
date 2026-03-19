package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.model.Asiento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class GestionAsientosViewController {

    @FXML
    private TableView<Asiento> tablaAsientos;

    @FXML
    private void initialize() {
        // Aquí se inicializarían las columnas de la tabla y se cargarían los asientos
        // Por ahora, solo un placeholder
        System.out.println("GestionAsientosView inicializada.");
    }

    @FXML
    void onHabilitarAsientoClick(ActionEvent event) {
        Asiento asientoSeleccionado = tablaAsientos.getSelectionModel().getSelectedItem();
        if (asientoSeleccionado != null) {
            mostrarAlerta("Información", "Habilitar Asiento", "Funcionalidad para habilitar asiento no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Habilitar Asiento", "Por favor, seleccione un asiento.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onBloquearAsientoClick(ActionEvent event) {
        Asiento asientoSeleccionado = tablaAsientos.getSelectionModel().getSelectedItem();
        if (asientoSeleccionado != null) {
            mostrarAlerta("Información", "Bloquear Asiento", "Funcionalidad para bloquear asiento no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Bloquear Asiento", "Por favor, seleccione un asiento.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onLiberarAsientoClick(ActionEvent event) {
        Asiento asientoSeleccionado = tablaAsientos.getSelectionModel().getSelectedItem();
        if (asientoSeleccionado != null) {
            mostrarAlerta("Información", "Liberar Asiento", "Funcionalidad para liberar asiento no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Liberar Asiento", "Por favor, seleccione un asiento.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onConsultarDisponibilidadClick(ActionEvent event) {
        mostrarAlerta("Información", "Consultar Disponibilidad", "Funcionalidad para consultar disponibilidad no implementada.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) tablaAsientos.getScene().getWindow();
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
