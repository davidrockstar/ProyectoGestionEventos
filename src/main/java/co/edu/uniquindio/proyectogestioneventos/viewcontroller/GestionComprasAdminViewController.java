package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class GestionComprasAdminViewController {

    @FXML
    private TableView<Compra> tablaCompras;

    @FXML
    private void initialize() {
        // Aquí se inicializarían las columnas de la tabla y se cargarían las compras
        // Por ahora, solo un placeholder
        System.out.println("GestionComprasAdminView inicializada.");
    }

    @FXML
    void onVerDetalleClick(ActionEvent event) {
        Compra compraSeleccionada = tablaCompras.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/administrador/DetalleCompraAdminView.fxml"));
                Parent root = loader.load();
                DetalleCompraAdminViewController controller = loader.getController();
                // controller.setCompra(compraSeleccionada); // Pasar la compra seleccionada

                Stage stage = new Stage();
                stage.setTitle("Detalle de Compra (Admin)");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(((Stage)((javafx.scene.control.Button)event.getSource()).getScene().getWindow()));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo cargar la ventana", "Hubo un error al intentar abrir el detalle de compra.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Advertencia", "Ver Detalle", "Por favor, seleccione una compra para ver su detalle.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onReasignarAsientoClick(ActionEvent event) {
        Compra compraSeleccionada = tablaCompras.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            mostrarAlerta("Información", "Reasignar Asiento", "Funcionalidad para reasignar asiento no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Reasignar Asiento", "Por favor, seleccione una compra para reasignar un asiento.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onCancelarCompraClick(ActionEvent event) {
        Compra compraSeleccionada = tablaCompras.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            mostrarAlerta("Información", "Cancelar Compra", "Funcionalidad para cancelar compra no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Cancelar Compra", "Por favor, seleccione una compra para cancelar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onRegistrarReembolsoClick(ActionEvent event) {
        Compra compraSeleccionada = tablaCompras.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            mostrarAlerta("Información", "Registrar Reembolso", "Funcionalidad para registrar reembolso no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Registrar Reembolso", "Por favor, seleccione una compra para registrar un reembolso.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) tablaCompras.getScene().getWindow();
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
