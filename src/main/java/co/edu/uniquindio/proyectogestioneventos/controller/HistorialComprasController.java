package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import co.edu.uniquindio.proyectogestioneventos.model.Taquilla;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import co.edu.uniquindio.proyectogestioneventos.model.enums.Rol;
import co.edu.uniquindio.proyectogestioneventos.service.ICompraService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.CompraServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HistorialComprasController {

    private static final Logger LOGGER = Logger.getLogger(HistorialComprasController.class.getName());
    private final ICompraService compraService = new CompraServiceImpl();
    private Usuario usuarioActual;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Compra> tablaCompras;
    @FXML private TableColumn<Compra, String> colId, colEvento, colFecha, colEstado;
    @FXML private TableColumn<Compra, Double> colTotal;
    @FXML
    private DatePicker campoFecha;
    @FXML
    private TextField campoEvento;
    @FXML
    private ComboBox<EstadoCompra> cbEstado;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    @FXML
    private void initialize() {
        if (tablaCompras == null || colId == null) {
            return; // Prevenir inicialización si el FXML no está listo
        }

        cbEstado.setItems(FXCollections.observableArrayList(EstadoCompra.values()));

        configurarColumnas();

        // Cargar datos del usuario logueado INMEDIATAMENTE
        Usuario logueado = MyApplication.getUsuarioLogueado();
        setUsuario(logueado);
        cargarHistorial(logueado);

        // Listener para actualizaciones en tiempo real
        Taquilla.getInstance().getCompras().addListener((ListChangeListener<Compra>) c -> {
            cargarHistorial(MyApplication.getUsuarioLogueado());
        });

        // Añadir listener para la tecla ESC al panel raíz
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                onVolverClick();
            }
        });
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idCompra"));
        colEvento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvento().getNombre()));
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaCreacion().toLocalDate().toString()));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado().toString()));
    }

    private void cargarHistorial(Usuario usuario) {
        if (usuario != null) {
            List<Compra> historial = Taquilla.getInstance().getCompras().stream()
                    .filter(c -> c.getUsuario() != null && c.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                    .collect(Collectors.toList());
            tablaCompras.setItems(FXCollections.observableArrayList(historial));
        }
    }

    @FXML
    private void onFiltrarClick() {
        LocalDate fecha = campoFecha.getValue();
        String nombreEvento = campoEvento.getText();
        EstadoCompra estado = cbEstado.getValue();
        List<Compra> historialFiltrado = compraService.obtenerHistorialCompras(usuarioActual, fecha, nombreEvento, estado);
        tablaCompras.getItems().setAll(historialFiltrado);
    }

    @FXML
    private void onVerDetalleClick() {
        Compra compraSeleccionada = tablaCompras.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            abrirVentana("Detalle de la Compra", "DetalleCompraView.fxml", (DetalleCompraController controller) -> controller.setCompra(compraSeleccionada));
        }
    }

    @FXML
    private void onDescargarReporteClick() {
        abrirVentana("Generar Reportes", "ReportesView.fxml", (ReportesController controller) -> controller.setCompras(tablaCompras.getItems()));
    }

    private <T> void abrirVentana(String title, String fxmlFile, Consumer<T> controllerConsumer) {
        try {
            Usuario usuario = MyApplication.getUsuarioLogueado();
            String basePath = (usuario != null && usuario.getRol() == Rol.ADMINISTRADOR) ? "administrador/" : "cliente/";
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/" + basePath + fxmlFile));
            Parent root = loader.load();
            T controller = loader.getController();
            if (controllerConsumer != null) {
                controllerConsumer.accept(controller);
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(rootPane.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar la vista: " + fxmlFile, e);
        }
    }

    @FXML
    private void onVolverClick() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
