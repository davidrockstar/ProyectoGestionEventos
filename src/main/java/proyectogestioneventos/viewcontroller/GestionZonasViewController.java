package proyectogestioneventos.viewcontroller;

import proyectogestioneventos.model.Recinto;
import proyectogestioneventos.model.Zona;
import proyectogestioneventos.service.IRecintoService;
import proyectogestioneventos.service.IZonaService;
import proyectogestioneventos.service.impl.RecintoServiceImpl;
import proyectogestioneventos.service.impl.ZonaServiceImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.Optional;

public class GestionZonasViewController {

    @FXML private TableView<Zona> tablaZonas;
    @FXML private TableColumn<Zona, String> colIdZona, colNombre;
    @FXML private TableColumn<Zona, Integer> colCapacidad;
    @FXML private TableColumn<Zona, Double> colPrecioBase;
    @FXML private ComboBox<Recinto> cbRecinto;

    private final IZonaService zonaService = new ZonaServiceImpl();
    private final IRecintoService recintoService = new RecintoServiceImpl();

    @FXML
    private void initialize() {
        configurarTabla();
        configurarComboRecinto();
        cargarRecintos();
    }

    private void configurarTabla() {
        colIdZona.setCellValueFactory(new PropertyValueFactory<>("idZona"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        colPrecioBase.setCellValueFactory(new PropertyValueFactory<>("precioBase"));
    }

    private void configurarComboRecinto() {
        cbRecinto.setConverter(new StringConverter<Recinto>() {
            @Override public String toString(Recinto r) { return r != null ? r.getNombre() : ""; }
            @Override public Recinto fromString(String string) { return null; }
        });
    }

    private void cargarRecintos() {
        cbRecinto.setItems(FXCollections.observableArrayList(recintoService.listarRecintos()));
        if (!cbRecinto.getItems().isEmpty()) {
            cbRecinto.getSelectionModel().selectFirst();
            onRecintoSeleccionado();
        }
    }

    @FXML
    void onRecintoSeleccionado() {
        actualizarTabla();
    }

    private void actualizarTabla() {
        Recinto seleccionado = cbRecinto.getValue();
        if (seleccionado != null) {
            var zonas = zonaService.listarZonas(seleccionado.getIdRecinto());
            if (zonas.isEmpty()) {
                try {
                    zonaService.generarDatosPrueba(seleccionado.getIdRecinto());
                    zonas = zonaService.listarZonas(seleccionado.getIdRecinto());
                } catch (Exception ignored) {}
            }
            tablaZonas.setItems(FXCollections.observableArrayList(zonas));
        }
    }

    @FXML
    void onCreateZonaClick(ActionEvent event) {
        if (cbRecinto.getValue() == null) {
            mostrarAlerta("Error", "Debe seleccionar un recinto primero.", Alert.AlertType.WARNING);
            return;
        }
        mostrarDialogoZona(null);
    }

    @FXML
    void onEditZonaClick(ActionEvent event) {
        Zona zonaSeleccionada = tablaZonas.getSelectionModel().getSelectedItem();
        if (zonaSeleccionada != null) {
            mostrarDialogoZona(zonaSeleccionada);
        } else {
            mostrarAlerta("Advertencia", "Editar Zona", "Por favor, seleccione una zona para editar.", Alert.AlertType.WARNING);
        }
    }

    private void mostrarDialogoZona(Zona zonaAEditar) {
        boolean esEdicion = (zonaAEditar != null);
        Recinto recinto = cbRecinto.getValue();

        Dialog<Zona> dialog = new Dialog<>();
        dialog.setTitle(esEdicion ? "Editar Zona" : "Crear Zona");
        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNombre = new TextField(esEdicion ? zonaAEditar.getNombre() : "");
        TextField txtCapacidad = new TextField(esEdicion ? String.valueOf(zonaAEditar.getCapacidad()) : "");
        TextField txtPrecio = new TextField(esEdicion ? String.valueOf(zonaAEditar.getPrecioBase()) : "");

        grid.add(new Label("Nombre:"), 0, 0); grid.add(txtNombre, 1, 0);
        grid.add(new Label("Capacidad:"), 0, 1); grid.add(txtCapacidad, 1, 1);
        grid.add(new Label("Precio Base:"), 0, 2); grid.add(txtPrecio, 1, 2);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                try {
                    int cap = Integer.parseInt(txtCapacidad.getText());
                    double pre = Double.parseDouble(txtPrecio.getText());
                    if (esEdicion) {
                        return zonaService.actualizarZona(recinto.getIdRecinto(), zonaAEditar.getIdZona(), txtNombre.getText(), cap, pre);
                    } else {
                        return zonaService.crearZona(recinto.getIdRecinto(), txtNombre.getText(), cap, pre);
                    }
                } catch (NumberFormatException e) {
                    mostrarAlerta("Error", "Capacidad y Precio deben ser numéricos.", Alert.AlertType.ERROR);
                } catch (Exception e) {
                    mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<Zona> result = dialog.showAndWait();
        result.ifPresent(z -> actualizarTabla());
    }

    @FXML
    void onDeleteZonaClick(ActionEvent event) {
        Zona zonaSeleccionada = tablaZonas.getSelectionModel().getSelectedItem();
        if (zonaSeleccionada != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de eliminar esta zona?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(res -> {
                if (res == ButtonType.YES) {
                    try {
                        zonaService.eliminarZona(cbRecinto.getValue().getIdRecinto(), zonaSeleccionada.getIdZona());
                        actualizarTabla();
                    } catch (Exception e) {
                        mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            });
        } else {
            mostrarAlerta("Advertencia", "Eliminar Zona", "Por favor, seleccione una zona para eliminar.", Alert.AlertType.WARNING);
        }
    }

    public void setRecinto(Recinto recinto) {
        if (recinto != null) {
            cbRecinto.getSelectionModel().select(recinto);
            onRecintoSeleccionado();
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

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        mostrarAlerta(titulo, null, contenido, tipo);
    }
}
