package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import co.edu.uniquindio.proyectogestioneventos.model.enums.Rol;
import co.edu.uniquindio.proyectogestioneventos.service.ICompraService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.CompraServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

public class HistorialComprasController {

    private static final Logger LOGGER = Logger.getLogger(HistorialComprasController.class.getName());
    private final ICompraService compraService = new CompraServiceImpl();
    private Usuario usuarioActual;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Compra> tablaCompras;
    @FXML
    private DatePicker campoFecha;
    @FXML
    private TextField campoEvento;
    @FXML
    private ComboBox<EstadoCompra> cbEstado;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        initialize(); // Cargar datos después de setear el usuario
    }

    @FXML
    private void initialize() {
        if (usuarioActual != null) {
            cbEstado.setItems(FXCollections.observableArrayList(EstadoCompra.values()));
            cargarHistorial();
        }
        // Añadir listener para la tecla ESC al panel raíz
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                onVolverClick();
            }
        });
    }

    private void cargarHistorial() {
        if (usuarioActual != null) {
            List<Compra> historial = compraService.obtenerHistorialCompras(usuarioActual, null, null, null);
            tablaCompras.getItems().setAll(historial);
            // Aquí se necesitaría configurar las celdas de las columnas para mostrar los datos de Compra
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
