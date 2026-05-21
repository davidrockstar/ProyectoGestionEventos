package proyectogestioneventos.viewcontroller;

import proyectogestioneventos.model.Asiento;
import proyectogestioneventos.model.Recinto;
import proyectogestioneventos.model.Zona;
import proyectogestioneventos.model.enums.EstadoAsiento;
import proyectogestioneventos.service.IAsientoService;
import proyectogestioneventos.service.IRecintoService;
import proyectogestioneventos.service.impl.AsientoServiceImpl;
import proyectogestioneventos.service.impl.RecintoServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class GestionAsientosViewController {

    @FXML private TableView<Asiento> tablaAsientos;
    @FXML private TableColumn<Asiento, Long> colId; // Cambiado a Long
    @FXML private TableColumn<Asiento, String> colFila, colCodigo, colEstado; // Añadido colCodigo
    @FXML private TableColumn<Asiento, Integer> colNumero;

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
        colId.setCellValueFactory(new PropertyValueFactory<>("idAsiento")); // Ahora es Long
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo")); // Nuevo atributo
        colFila.setCellValueFactory(new PropertyValueFactory<>("fila"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado() != null ?
                        cellData.getValue().getEstado().toString() : "N/A"));
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
            // Limpiar la selección de zona y la tabla de asientos antes de cargar nuevas opciones
            cbZona.getSelectionModel().clearSelection();
            tablaAsientos.getItems().clear();

            cbZona.setItems(FXCollections.observableArrayList(r.getListaZonas()));
            if (!cbZona.getItems().isEmpty()) {
                cbZona.getSelectionModel().selectFirst(); // Seleccionar la primera zona para cargar sus asientos
                // Llamar explícitamente a actualizarTabla() para asegurar el refresco
                actualizarTabla();
            }
        } else {
            cbZona.getItems().clear();
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
        Asiento seleccionado = tablaAsientos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Habilitar Asiento", "Por favor, seleccione un asiento de la tabla.", Alert.AlertType.WARNING);
            return;
        }

        // Habilitar se usa principalmente para reactivar asientos INHABILITADOS
        if (seleccionado.getEstado() == EstadoAsiento.INHABILITADO) {
            procesarCambioEstado(EstadoAsiento.DISPONIBLE, "Habilitar Asiento");
        } else {
            mostrarAlerta("Información", "El asiento ya se encuentra habilitado o tiene una reserva/venta activa.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void onBloquearAsientoClick(ActionEvent event) {
        Asiento seleccionado = tablaAsientos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Bloquear Asiento", "Seleccione un asiento.", Alert.AlertType.WARNING);
            return;
        }

        // Validación: Solo se pueden inhabilitar asientos que no tengan compromisos comerciales
        if (seleccionado.getEstado() == EstadoAsiento.OCUPADO || seleccionado.getEstado() == EstadoAsiento.RESERVADO) {
            mostrarAlerta("Error", "Acción no permitida", "No se puede inhabilitar un asiento con estado: " + seleccionado.getEstado(), Alert.AlertType.ERROR);
            return;
        }

        procesarCambioEstado(EstadoAsiento.INHABILITADO, "Inhabilitar Asiento");
    }

    @FXML
    void onLiberarAsientoClick(ActionEvent event) {
        Asiento seleccionado = tablaAsientos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Liberar Asiento", "Seleccione un asiento para liberar.", Alert.AlertType.WARNING);
            return;
        }

        if (seleccionado.getEstado() == EstadoAsiento.OCUPADO) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, 
                "¿Desea liberar un asiento que ya está OCUPADO? Esta acción no reembolsa el dinero automáticamente.", 
                ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirmar Liberación");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    procesarCambioEstado(EstadoAsiento.DISPONIBLE, "Liberar Asiento");
                }
            });
        } else if (seleccionado.getEstado() == EstadoAsiento.INHABILITADO || seleccionado.getEstado() == EstadoAsiento.RESERVADO) {
            // Las inhabilitaciones y reservas se liberan directamente a DISPONIBLE
            procesarCambioEstado(EstadoAsiento.DISPONIBLE, "Liberar Asiento");
        } else {
            mostrarAlerta("Información", "El asiento ya está disponible.", Alert.AlertType.INFORMATION);
        }
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
