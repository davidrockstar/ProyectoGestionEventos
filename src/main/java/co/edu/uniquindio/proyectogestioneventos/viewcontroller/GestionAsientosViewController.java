package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.model.Asiento;
import co.edu.uniquindio.proyectogestioneventos.model.Recinto;
import co.edu.uniquindio.proyectogestioneventos.model.Zona;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoAsiento;
import co.edu.uniquindio.proyectogestioneventos.service.IAsientoService;
import co.edu.uniquindio.proyectogestioneventos.service.IRecintoService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.AsientoServiceImpl;
import co.edu.uniquindio.proyectogestioneventos.service.impl.RecintoServiceImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class GestionAsientosViewController {

    @FXML private TableView<Asiento> tablaAsientos;
    @FXML private TableColumn<Asiento, String> colId, colFila, colNumero, colEstado;
    @FXML private ComboBox<Recinto> cbRecinto;
    @FXML private ComboBox<Zona> cbZona;

    private final IAsientoService asientoService = new AsientoServiceImpl();
    private final IRecintoService recintoService = new RecintoServiceImpl();

    @FXML
    private void initialize() {
        configurarTabla();
        configurarCombos();
        cargarRecintos();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idAsiento"));
        colFila.setCellValueFactory(new PropertyValueFactory<>("fila"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void configurarCombos() {
        cbRecinto.setConverter(new StringConverter<Recinto>() {
            @Override public String toString(Recinto r) { return r != null ? r.getNombre() : ""; }
            @Override public Recinto fromString(String s) { return null; }
        });
        cbZona.setConverter(new StringConverter<Zona>() {
            @Override public String toString(Zona z) { return z != null ? z.getNombre() : ""; }
            @Override public Zona fromString(String s) { return null; }
        });
    }

    private void cargarRecintos() {
        cbRecinto.setItems(FXCollections.observableArrayList(recintoService.listarRecintos()));
    }

    @FXML
    void onRecintoSeleccionado() {
        Recinto r = cbRecinto.getValue();
        if (r != null) {
            cbZona.setItems(FXCollections.observableArrayList(r.getListaZonas()));
            tablaAsientos.getItems().clear();
        }
    }

    @FXML
    void onZonaSeleccionada() {
        actualizarTabla();
    }

    private void actualizarTabla() {
        Recinto r = cbRecinto.getValue();
        Zona z = cbZona.getValue();
        if (r != null && z != null) {
            var asientos = asientoService.listarAsientos(r.getIdRecinto(), z.getIdZona());
            if (asientos.isEmpty()) {
                try {
                    asientoService.generarDatosPrueba(r.getIdRecinto(), z.getIdZona());
                    asientos = asientoService.listarAsientos(r.getIdRecinto(), z.getIdZona());
                } catch (Exception e) {
                    mostrarAlerta("Error", "Error al generar asientos", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
            tablaAsientos.setItems(FXCollections.observableArrayList(asientos));
        }
    }

    @FXML
    void onHabilitarAsientoClick(ActionEvent event) {
        procesarCambioEstado(EstadoAsiento.DISPONIBLE, "Habilitar Asiento");
    }

    @FXML
    void onBloquearAsientoClick(ActionEvent event) {
        procesarCambioEstado(EstadoAsiento.BLOQUEADO, "Bloquear Asiento");
    }

    @FXML
    void onLiberarAsientoClick(ActionEvent event) {
        procesarCambioEstado(EstadoAsiento.DISPONIBLE, "Liberar Asiento");
    }

    private void procesarCambioEstado(EstadoAsiento nuevoEstado, String titulo) {
        Asiento asientoSeleccionado = tablaAsientos.getSelectionModel().getSelectedItem();
        Recinto r = cbRecinto.getValue();
        Zona z = cbZona.getValue();

        if (asientoSeleccionado != null && r != null && z != null) {
            try {
                asientoService.cambiarEstadoAsiento(r.getIdRecinto(), z.getIdZona(), asientoSeleccionado.getIdAsiento(), nuevoEstado);
                actualizarTabla();
                tablaAsientos.getSelectionModel().select(asientoSeleccionado);
            } catch (Exception e) {
                mostrarAlerta("Error", titulo, e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Advertencia", titulo, "Por favor, seleccione un recinto, zona y asiento.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onConsultarDisponibilidadClick(ActionEvent event) {
        Zona z = cbZona.getValue();
        if (z != null) {
            double ocupacion = z.calcularOcupacion();
            mostrarAlerta("Disponibilidad", "Estado de la Zona", 
                String.format("La zona %s tiene un %.2f%% de ocupación.", z.getNombre(), ocupacion), 
                Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Consultar Disponibilidad", "Seleccione una zona.", Alert.AlertType.WARNING);
        }
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

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        mostrarAlerta(titulo, null, contenido, tipo);
    }
}
