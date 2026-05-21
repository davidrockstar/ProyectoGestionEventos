package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import co.edu.uniquindio.proyectogestioneventos.model.Recinto;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEvento;
import co.edu.uniquindio.proyectogestioneventos.service.IEventoService;
import co.edu.uniquindio.proyectogestioneventos.service.IRecintoService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.EventoServiceImpl;
import co.edu.uniquindio.proyectogestioneventos.service.impl.RecintoServiceImpl;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class GestionEventosViewController {

    @FXML
    private TableView<Evento> tablaEventos;
    @FXML private TableColumn<Evento, String> colIdEvento;
    @FXML private TableColumn<Evento, String> colNombre;
    @FXML private TableColumn<Evento, String> colCategoria;
    @FXML private TableColumn<Evento, String> colCiudad;
    @FXML private TableColumn<Evento, String> colFechaHora;
    @FXML private TableColumn<Evento, String> colEstado;

    private final IEventoService eventoService = new EventoServiceImpl();
    private final IRecintoService recintoService = new RecintoServiceImpl();

    @FXML
    private void initialize() {
        configurarTabla();
        actualizarTabla();
    }

    private void configurarTabla() {
        colIdEvento.setCellValueFactory(new PropertyValueFactory<>("idEvento"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        
        // Formatear la fecha para que se vea correctamente en la tabla
        colFechaHora.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado().toString()));
    }

    private void actualizarTabla() {
        tablaEventos.setItems(FXCollections.observableArrayList(eventoService.listarTodosEventos()));
    }

    @FXML
    void onCreateEventoClick(ActionEvent event) {
        mostrarDialogoEvento(null);
    }

    @FXML
    void onEditEventoClick(ActionEvent event) {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            mostrarDialogoEvento(eventoSeleccionado);
        } else {
            mostrarAlerta("Advertencia", "Editar Evento", "Por favor, seleccione un evento para editar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onDeleteEventoClick(ActionEvent event) {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar Eliminación");
            confirm.setHeaderText("Eliminar Evento: " + eventoSeleccionado.getNombre());
            confirm.setContentText("¿Está seguro de eliminar este evento? Esta acción no se puede deshacer.");

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    eventoService.eliminarEvento(eventoSeleccionado.getIdEvento());
                    actualizarTabla();
                    mostrarAlerta("Éxito", "Evento Eliminado", "El evento ha sido eliminado correctamente.", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    mostrarAlerta("Error", "Error al eliminar evento", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarAlerta("Advertencia", "Eliminar Evento", "Por favor, seleccione un evento para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onPublicarEventoClick(ActionEvent event) {
        cambiarEstadoEventoSeleccionado(EstadoEvento.PUBLICADO, "Publicar Evento");
    }

    @FXML
    void onCancelarEventoClick(ActionEvent event) {
        cambiarEstadoEventoSeleccionado(EstadoEvento.CANCELADO, "Cancelar Evento");
    }

    private void cambiarEstadoEventoSeleccionado(EstadoEvento nuevoEstado, String tituloAccion) {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar " + tituloAccion);
            confirm.setHeaderText("Cambiar estado de " + eventoSeleccionado.getNombre() + " a " + nuevoEstado.toString());
            confirm.setContentText("¿Está seguro de cambiar el estado de este evento?");

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    eventoService.cambiarEstadoEvento(eventoSeleccionado.getIdEvento(), nuevoEstado);
                    actualizarTabla();
                    mostrarAlerta("Éxito", tituloAccion, "El estado del evento ha sido actualizado a " + nuevoEstado.toString() + ".", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    mostrarAlerta("Error", "Error al cambiar estado", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarAlerta("Advertencia", tituloAccion, "Por favor, seleccione un evento para cambiar su estado.", Alert.AlertType.WARNING);
        }
    }

    private void mostrarDialogoEvento(Evento eventoAEditar) {
        boolean esEdicion = (eventoAEditar != null);

        Dialog<Evento> dialog = new Dialog<>();
        dialog.setTitle(esEdicion ? "Editar Evento" : "Crear Nuevo Evento");
        dialog.setHeaderText("Ingrese los detalles del evento");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNombre = new TextField(esEdicion ? eventoAEditar.getNombre() : "");
        TextArea txtDescripcion = new TextArea(esEdicion ? eventoAEditar.getDescripcion() : "");
        TextField txtCategoria = new TextField(esEdicion ? eventoAEditar.getCategoria() : "");
        TextField txtCiudad = new TextField(esEdicion ? eventoAEditar.getCiudad() : "");
        DatePicker dpFecha = new DatePicker(esEdicion ? eventoAEditar.getFechaHora().toLocalDate() : null);
        TextField txtHora = new TextField(esEdicion ? eventoAEditar.getFechaHora().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
        ComboBox<Recinto> comboRecinto = new ComboBox<>(FXCollections.observableArrayList(recintoService.listarRecintos()));
        
        if (esEdicion && eventoAEditar.getRecinto() != null) {
            comboRecinto.setValue(eventoAEditar.getRecinto());
        }

        // Configurar StringConverter para ComboBox de Recinto
        comboRecinto.setConverter(new StringConverter<Recinto>() {
            @Override
            public String toString(Recinto recinto) {
                return recinto != null ? recinto.getNombre() : "";
            }

            @Override
            public Recinto fromString(String string) {
                return recintoService.listarRecintos().stream()
                        .filter(r -> r.getNombre().equals(string))
                        .findFirst().orElse(null);
            }
        });

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1);
        grid.add(txtDescripcion, 1, 1);
        grid.add(new Label("Categoría:"), 0, 2);
        grid.add(txtCategoria, 1, 2);
        grid.add(new Label("Ciudad:"), 0, 3);
        grid.add(txtCiudad, 1, 3);
        grid.add(new Label("Fecha:"), 0, 4);
        grid.add(dpFecha, 1, 4);
        grid.add(new Label("Hora (HH:mm):"), 0, 5);
        grid.add(txtHora, 1, 5);
        grid.add(new Label("Recinto:"), 0, 6);
        grid.add(comboRecinto, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                try {
                    // Validaciones
                    if (txtNombre.getText().isEmpty() || txtDescripcion.getText().isEmpty() ||
                        txtCategoria.getText().isEmpty() || txtCiudad.getText().isEmpty() ||
                        dpFecha.getValue() == null || txtHora.getText().isEmpty() ||
                        comboRecinto.getValue() == null) {
                        throw new Exception("Todos los campos son obligatorios.");
                    }

                    LocalDate fecha = dpFecha.getValue();
                    LocalTime hora;
                    try {
                        hora = LocalTime.parse(txtHora.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                    } catch (DateTimeParseException e) {
                        throw new Exception("Formato de hora inválido. Use HH:mm (ej. 14:30).");
                    }
                    LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);
                    Recinto recintoSeleccionado = comboRecinto.getValue();

                    if (esEdicion) {
                        return eventoService.actualizarEvento(
                                eventoAEditar.getIdEvento(),
                                txtNombre.getText(),
                                txtCategoria.getText(),
                                txtDescripcion.getText(),
                                txtCiudad.getText(),
                                fechaHora,
                                recintoSeleccionado
                        );
                    } else {
                        return eventoService.crearEvento(
                                txtNombre.getText(),
                                txtCategoria.getText(),
                                txtDescripcion.getText(),
                                txtCiudad.getText(),
                                fechaHora,
                                recintoSeleccionado
                        );
                    }
                } catch (Exception e) {
                    mostrarAlerta("Error", "Error al procesar evento", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<Evento> result = dialog.showAndWait();
        result.ifPresent(e -> actualizarTabla());
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

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        mostrarAlerta(titulo, null, contenido, tipo);
    }
}
