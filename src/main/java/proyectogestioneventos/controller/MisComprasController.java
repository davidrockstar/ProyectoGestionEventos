package proyectogestioneventos.controller;

import proyectogestioneventos.MyApplication;
import proyectogestioneventos.model.Taquilla;
import proyectogestioneventos.model.Compra;
import proyectogestioneventos.model.Usuario;
import proyectogestioneventos.service.ICompraService;
import proyectogestioneventos.service.impl.CompraServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MisComprasController {

    private final ICompraService compraService = new CompraServiceImpl();
    private Usuario usuarioActual;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Compra> tablaMisCompras;
    @FXML private TableColumn<Compra, String> colId, colEvento, colFecha, colEstado;
    @FXML private TableColumn<Compra, Double> colTotal;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarComprasActivas();
    }

    private void cargarComprasActivas() {
        if (usuarioActual == null) {
            usuarioActual = MyApplication.getUsuarioLogueado();
        }

        if (usuarioActual != null) {
            List<Compra> filtradas = Taquilla.getInstance().getCompras().stream()
                    .filter(c -> c.getUsuario() != null && c.getUsuario().getIdUsuario().equals(usuarioActual.getIdUsuario()))
                    .collect(Collectors.toList());
            tablaMisCompras.setItems(FXCollections.observableArrayList(filtradas));
        }
    }

    @FXML
    private void initialize() {
        configurarColumnas();
        
        // Cargar datos del usuario logueado al iniciar
        setUsuario(MyApplication.getUsuarioLogueado());

        // Listener único para actualizaciones en tiempo real desde el modelo global
        Taquilla.getInstance().getCompras().addListener((ListChangeListener<Compra>) c -> {
            cargarComprasActivas();
        });
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idCompra"));
        colEvento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvento().getNombre()));
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaCreacion().toLocalDate().toString()));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado().toString()));
    }

    @FXML
    void onModificarCompraClick(ActionEvent event) {
        // Este botón se eliminó de la vista FXML para simplificar el flujo
    }

    @FXML
    void onCancelarCompraClick(ActionEvent event) {
        Compra compraSeleccionada = tablaMisCompras.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
                "¿Está seguro de cancelar esta compra? Los asientos se liberarán inmediatamente.", 
                ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    compraService.cancelarCompra(compraSeleccionada);
                    cargarComprasActivas(); // Refrescar tabla
                }
            });
        }
    }

    @FXML
    void onPagarCompraClick(ActionEvent event) {
        // Este botón se eliminó de la vista FXML para simplificar el flujo
    }

    @FXML
    void onVerDetalleCompraClick(ActionEvent event) {
        Compra compraSeleccionada = tablaMisCompras.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyectogestioneventos/usuario/cliente/DetalleCompraView.fxml"));
                Parent root = loader.load();
                
                DetalleCompraController controller = loader.getController();
                controller.setCompra(compraSeleccionada);

                Stage stage = new Stage();
                stage.setTitle("Detalle de Compra");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(rootPane.getScene().getWindow());
                stage.showAndWait();
                
                cargarComprasActivas(); // Refrescar por si se pagó dentro del detalle
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
