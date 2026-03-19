package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.model.enums.Rol;
import co.edu.uniquindio.proyectogestioneventos.service.ICompraService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.CompraServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MisComprasController {

    private final ICompraService compraService = new CompraServiceImpl();
    private Usuario usuarioActual;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Compra> tablaMisCompras;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarComprasActivas();
    }

    private void cargarComprasActivas() {
        // Filtrar compras por estado PENDIENTE o CREADA
        // List<Compra> comprasActivas = compraService.obtenerComprasActivas(usuarioActual);
        // tablaMisCompras.getItems().setAll(comprasActivas);
    }

    @FXML
    private void initialize() {
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
            System.out.println("Cancelar compra: " + compraSeleccionada.getIdCompra());
            // Lógica para cancelar compra (RF-006)
        }
    }

    @FXML
    void onPagarCompraClick(ActionEvent event) {
        Compra compraSeleccionada = tablaMisCompras.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            System.out.println("Pagar compra: " + compraSeleccionada.getIdCompra());
            // Lógica para abrir pantalla de pago (RF-007)
        }
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
                stage.initOwner(((Stage)((javafx.scene.control.Button)event.getSource()).getScene().getWindow()));
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
