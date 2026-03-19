package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.model.Incidencia;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class IncidenciasAdminViewController {

    @FXML
    private TableView<Incidencia> tablaIncidencias;
    @FXML
    private ComboBox<String> cbTipoIncidencia; // Asumiendo tipos de incidencia como String por ahora
    @FXML
    private TextArea txtDescripcionIncidencia;
    @FXML
    private TextField txtEntidadAfectada;

    @FXML
    private void initialize() {
        // Inicializar ComboBox de tipos de incidencia
        cbTipoIncidencia.setItems(FXCollections.observableArrayList("Técnica", "Pago", "Acceso", "Comportamiento", "Otro"));
        // Aquí se inicializarían las columnas de la tabla y se cargarían las incidencias
        // Por ahora, solo un placeholder
        System.out.println("IncidenciasAdminView inicializada.");
    }

    @FXML
    void onRegistrarIncidenciaClick(ActionEvent event) {
        String tipo = cbTipoIncidencia.getValue();
        String descripcion = txtDescripcionIncidencia.getText();
        String entidadAfectada = txtEntidadAfectada.getText();

        if (tipo == null || descripcion.isEmpty() || entidadAfectada.isEmpty()) {
            mostrarAlerta("Error de Validación", "Registrar Incidencia", "Todos los campos son obligatorios para registrar una incidencia.", Alert.AlertType.WARNING);
            return;
        }

        // Lógica para registrar la incidencia (RF-017)
        mostrarAlerta("Información", "Registrar Incidencia", "Funcionalidad para registrar incidencia no implementada.", Alert.AlertType.INFORMATION);
        // Limpiar campos después de registrar
        cbTipoIncidencia.getSelectionModel().clearSelection();
        txtDescripcionIncidencia.clear();
        txtEntidadAfectada.clear();
    }

    @FXML
    void onVerDetalleIncidenciaClick(ActionEvent event) {
        Incidencia incidenciaSeleccionada = tablaIncidencias.getSelectionModel().getSelectedItem();
        if (incidenciaSeleccionada != null) {
            mostrarAlerta("Información", "Ver Detalle Incidencia", "Funcionalidad para ver detalle de incidencia no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Ver Detalle Incidencia", "Por favor, seleccione una incidencia para ver su detalle.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onFiltrarIncidenciasClick(ActionEvent event) {
        mostrarAlerta("Información", "Filtrar Incidencias", "Funcionalidad para filtrar incidencias no implementada.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) tablaIncidencias.getScene().getWindow();
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
