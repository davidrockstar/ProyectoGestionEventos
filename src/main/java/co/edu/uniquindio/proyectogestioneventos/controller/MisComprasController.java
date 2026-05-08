package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import co.edu.uniquindio.proyectogestioneventos.model.enums.Rol;
import co.edu.uniquindio.proyectogestioneventos.service.ICompraService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.CompraServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
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
        if (usuarioActual != null) {
            List<Compra> historial = compraService.obtenerHistorialCompras(usuarioActual, null, null, null);
            List<Compra> activas = historial.stream()
                    .filter(c -> c.getEstado() == EstadoCompra.CREADA || 
                                 c.getEstado() == EstadoCompra.PENDIENTE || 
                                 c.getEstado() == EstadoCompra.INCIDENCIA)
                    .collect(Collectors.toList());
            tablaMisCompras.setItems(FXCollections.observableArrayList(activas));
        }
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idCompra"));
        colEvento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvento().getNombre()));
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaCreacion().toLocalDate().toString()));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado().toString()));

        // Añadir listener para la tecla ESC al panel raíz
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                onVolverClick(null);
            }
        });
    }

    @FXML
    void onModificarCompraClick(ActionEvent event) {
        Compra compraSeleccionada = tablaMisCompras.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            System.out.println("Modificar compra: " + compraSeleccionada.getIdCompra());
            // Lógica para abrir pantalla de modificación de compra (RF-006)
        }
    }

    @FXML
    void onCancelarCompraClick(ActionEvent event) {
        Compra compraSeleccionada = tablaMisCompras.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            compraService.cancelarCompra(compraSeleccionada);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Compra Cancelada");
            alert.setContentText("La compra ha sido cancelada y los asientos liberados.");
            alert.showAndWait();
            cargarComprasActivas();
        }
    }

    @FXML
    void onPagarCompraClick(ActionEvent event) {
        // Reutilizamos el detalle para proceder al pago
        onVerDetalleCompraClick(event);
    }

    @FXML
    void onVerDetalleCompraClick(ActionEvent event) {
        Compra compraSeleccionada = tablaMisCompras.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            try {
                Usuario usuario = MyApplication.getUsuarioLogueado();
                String basePath = (usuario != null && usuario.getRol() == Rol.ADMINISTRADOR) ? "administrador/" : "cliente/";
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/" + basePath + "DetalleCompraView.fxml"));
                Parent root = loader.load();
                DetalleCompraController controller = loader.getController();
                controller.setCompra(compraSeleccionada);

                Stage stage = new Stage();
                stage.setTitle("Detalle de la Compra");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                // Usar el rootPane para obtener la ventana propietaria de forma más robusta
                stage.initOwner(rootPane.getScene().getWindow());
                stage.showAndWait();
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
