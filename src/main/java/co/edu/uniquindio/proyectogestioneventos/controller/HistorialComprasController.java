package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import co.edu.uniquindio.proyectogestioneventos.service.ICompraService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.CompraServiceImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class HistorialComprasController {

    private final ICompraService compraService = new CompraServiceImpl();
    private Usuario usuarioActual;

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
    }

    private void cargarHistorial() {
        if (usuarioActual != null) {
            List<Compra> historial = compraService.obtenerHistorialCompras(usuarioActual, null, null, null);
            tablaCompras.getItems().setAll(historial);
            // Aquí se necesitaría configurar las celdas de las columnas para mostrar los datos de Compra
        }
    }

    @FXML
    private void onFiltrarClick(ActionEvent event) {
        LocalDate fecha = campoFecha.getValue();
        String nombreEvento = campoEvento.getText();
        EstadoCompra estado = cbEstado.getValue();
        List<Compra> historialFiltrado = compraService.obtenerHistorialCompras(usuarioActual, fecha, nombreEvento, estado);
        tablaCompras.getItems().setAll(historialFiltrado);
    }

    @FXML
    private void onVerDetalleClick(ActionEvent event) {
        Compra compraSeleccionada = tablaCompras.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/view/DetalleCompraView.fxml"));
                Parent root = loader.load();
                DetalleCompraController controller = loader.getController();
                controller.setCompra(compraSeleccionada);

                Stage stage = new Stage();
                stage.setTitle("Detalle de la Compra");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(((Stage)((javafx.scene.control.Button)event.getSource()).getScene().getWindow()));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onDescargarReporteClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/view/ReportesView.fxml"));
            Parent root = loader.load();
            ReportesController controller = loader.getController();
            controller.setCompras(tablaCompras.getItems()); // Pasar las compras actuales para el reporte

            Stage stage = new Stage();
            stage.setTitle("Generar Reportes");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Stage)((javafx.scene.control.Button)event.getSource()).getScene().getWindow()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
