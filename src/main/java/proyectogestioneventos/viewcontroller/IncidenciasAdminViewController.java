package proyectogestioneventos.viewcontroller;

import proyectogestioneventos.model.*;
import proyectogestioneventos.model.Evento;
import proyectogestioneventos.model.Incidencia;
import proyectogestioneventos.model.Usuario;
import proyectogestioneventos.model.enums.TipoIncidencia;
import proyectogestioneventos.model.enums.EstadoIncidencia;
import proyectogestioneventos.service.IEventoService;
import proyectogestioneventos.service.IIncidenciaService;
import proyectogestioneventos.service.IUsuarioService;
import proyectogestioneventos.service.impl.EventoServiceImpl;
import proyectogestioneventos.service.impl.IncidenciaServiceImpl;
import proyectogestioneventos.service.impl.UsuarioServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.format.DateTimeFormatter;

public class IncidenciasAdminViewController {

    @FXML private AnchorPane rootPane;
    @FXML private TableView<Incidencia> tablaIncidencias; // Mantener TableView
    @FXML private TableColumn<Incidencia, Long> colId; // Cambiado a Long
    @FXML private TableColumn<Incidencia, String> colUsuario, colEvento, colTipo, colDescripcion, colFecha, colEstado; // Mantener String para otros
    @FXML private ComboBox<TipoIncidencia> cbTipoIncidencia;
    @FXML private ComboBox<Usuario> cbUsuario;
    @FXML private ComboBox<Evento> cbEvento;
    @FXML private TextArea txtDescripcionIncidencia;

    private final IIncidenciaService incidenciaService = new IncidenciaServiceImpl();
    private final IUsuarioService usuarioService = new UsuarioServiceImpl();
    private final IEventoService eventoService = new EventoServiceImpl();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    private void initialize() {
        configurarTabla();
        configurarCombos();
        cbTipoIncidencia.setItems(FXCollections.observableArrayList(TipoIncidencia.values()));
        actualizarTabla();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idIncidencia")); // Ahora es Long
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        
        // Corrección: Usar lambdas para columnas de Enums y tipos no String
        colTipo.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTipo() != null ? cellData.getValue().getTipo().toString() : "N/A"));
        
        colEstado.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEstado() != null ? cellData.getValue().getEstado().toString() : "N/A"));
        
        colFecha.setCellValueFactory(cellData -> // Usar fechaReporte
            new SimpleStringProperty(cellData.getValue().getFechaReporte() != null ? cellData.getValue().getFechaReporte().format(formatter) : "N/A"));

        colUsuario.setCellValueFactory(cellData -> // Usar reportante
            new SimpleStringProperty(cellData.getValue().getReportante() != null ? cellData.getValue().getReportante().getNombre() : "N/A"));

        colEvento.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEvento() != null ? cellData.getValue().getEvento().getNombre() : "N/A"));
    }

    private void configurarCombos() {
        cbUsuario.setItems(FXCollections.observableArrayList(usuarioService.listarUsuarios()));
        cbUsuario.setConverter(new StringConverter<Usuario>() {
            @Override public String toString(Usuario u) { return u != null ? u.getNombre() : ""; }
            @Override public Usuario fromString(String string) { return null; }
        });

        cbEvento.setItems(FXCollections.observableArrayList(eventoService.listarTodosEventos()));
        cbEvento.setConverter(new StringConverter<Evento>() {
            @Override public String toString(Evento e) { return e != null ? e.getNombre() : ""; }
            @Override public Evento fromString(String string) { return null; }
        });
    }

    private void actualizarTabla() {
        incidenciaService.generarDatosPrueba();
        tablaIncidencias.setItems(FXCollections.observableArrayList(incidenciaService.listarIncidencias()));
    }

    @FXML
    void onRegistrarIncidenciaClick(ActionEvent event) {
        TipoIncidencia tipo = cbTipoIncidencia.getValue();
        Usuario usuario = cbUsuario.getValue();
        Evento evento = cbEvento.getValue();
        String descripcion = txtDescripcionIncidencia.getText();

        if (tipo == null || usuario == null || evento == null || descripcion.isEmpty()) {
            mostrarAlerta("Error", "Validación", "Todos los campos (incluyendo Usuario y Evento) son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            incidenciaService.registrarIncidencia(tipo, descripcion, usuario, evento);
            mostrarAlerta("Éxito", "Registro", "Incidencia registrada correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
            actualizarTabla();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al registrar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        cbTipoIncidencia.getSelectionModel().clearSelection();
        cbUsuario.getSelectionModel().clearSelection();
        cbEvento.getSelectionModel().clearSelection();
        txtDescripcionIncidencia.clear();
    }

    @FXML
    void onResolverIncidenciaClick(ActionEvent event) {
        Incidencia seleccionada = tablaIncidencias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Selección", "Debe seleccionar una incidencia de la tabla.", Alert.AlertType.WARNING);
            return;
        }

        try {
            incidenciaService.cambiarEstado(seleccionada.getIdIncidencia(), EstadoIncidencia.RESUELTA);
            mostrarAlerta("Éxito", "Estado Actualizado", "La incidencia ha sido marcada como RESUELTA.", Alert.AlertType.INFORMATION);
            actualizarTabla();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se puede resolver", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onCerrarIncidenciaClick(ActionEvent event) {
        Incidencia seleccionada = tablaIncidencias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Selección", "Debe seleccionar una incidencia de la tabla.", Alert.AlertType.WARNING);
            return;
        }

        try {
            incidenciaService.cambiarEstado(seleccionada.getIdIncidencia(), EstadoIncidencia.CERRADA);
            mostrarAlerta("Éxito", "Estado Actualizado", "La incidencia ha sido marcada como CERRADA.", Alert.AlertType.INFORMATION);
            actualizarTabla();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se puede cerrar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onVerDetalleIncidenciaClick(ActionEvent event) {
        Incidencia incidenciaSeleccionada = tablaIncidencias.getSelectionModel().getSelectedItem();
        if (incidenciaSeleccionada != null) {
            String detalle = String.format(
                "ID: %s\nEstado: %s\nFecha: %s\nTipo: %s\n\nUsuario: %s\nEvento: %s\n\nDescripción:\n%s",
                incidenciaSeleccionada.getIdIncidencia(),
                incidenciaSeleccionada.getEstado(),
                incidenciaSeleccionada.getFechaReporte().format(formatter), // Usar fechaReporte
                incidenciaSeleccionada.getTipo(),
                incidenciaSeleccionada.getReportante().getNombre(), // Usar reportante
                incidenciaSeleccionada.getEvento().getNombre(),
                incidenciaSeleccionada.getDescripcion()
            );
            mostrarAlerta("Detalle de Incidencia", null, detalle, Alert.AlertType.INFORMATION);
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
