package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.model.Zona;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class GestionZonasViewController {

    @FXML
    private TableView<Zona> tablaZonas;

    @FXML
    private void initialize() {
        // Aquí se inicializarían las columnas de la tabla y se cargarían las zonas
        // Por ahora, solo un placeholder
        System.out.println("GestionZonasView inicializada.");
    }

    // Método para recibir el recinto si se abre desde GestionRecintosView
    // public void setRecinto(Recinto recinto) {
    //     // Cargar zonas del recinto específico
    // }

    @FXML
    void onCreateZonaClick(ActionEvent event) {
        mostrarAlerta("Información", "Crear Zona", "Funcionalidad para crear zona no implementada.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onEditZonaClick(ActionEvent event) {
        Zona zonaSeleccionada = tablaZonas.getSelectionModel().getSelectedItem();
        if (zonaSeleccionada != null) {
            mostrarAlerta("Información", "Editar Zona", "Funcionalidad para editar zona no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Editar Zona", "Por favor, seleccione una zona para editar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onDeleteZonaClick(ActionEvent event) {
        Zona zonaSeleccionada = tablaZonas.getSelectionModel().getSelectedItem();
        if (zonaSeleccionada != null) {
            mostrarAlerta("Información", "Eliminar Zona", "Funcionalidad para eliminar zona no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Eliminar Zona", "Por favor, seleccione una zona para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) tablaZonas.getScene().getWindow();
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
