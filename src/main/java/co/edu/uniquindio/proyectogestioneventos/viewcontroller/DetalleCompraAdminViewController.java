package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label; // Importar Label
import javafx.scene.control.TextArea;
import javafx.scene.text.Text; // Mantener Text si lblIdCompra sigue siendo Text
import javafx.stage.Stage;

public class DetalleCompraAdminViewController {

    @FXML
    private Text lblIdCompra; // Este sigue siendo Text
    @FXML
    private Label lblUsuario;
    @FXML
    private Label lblEvento;
    @FXML
    private Label lblEstado;
    @FXML
    private Label lblTotal; // ¡Cambiado de Text a Label!
    @FXML
    private TextArea txtEntradas;
    @FXML
    private TextArea txtServicios;

    private Compra compraActual;

    public void setCompra(Compra compra) {
        this.compraActual = compra;
        // Aquí se cargarían los datos de la compra en la UI
        lblIdCompra.setText("Detalle de Compra: " + compra.getIdCompra());
        // lblUsuario.setText(compra.getUsuario().getNombre()); // Asumiendo que Compra tiene un Usuario
        // lblEvento.setText(compra.getEvento().getNombre()); // Asumiendo que Compra tiene un Evento
        // lblEstado.setText(compra.getEstado().toString());
        // lblTotal.setText("$ " + compra.getTotal());
        // txtEntradas.setText(obtenerDetalleEntradas(compra));
        // txtServicios.setText(obtenerDetalleServicios(compra));
    }

    @FXML
    void onReasignarAsientoClick(ActionEvent event) {
        mostrarAlerta("Información", "Reasignar Asiento", "Funcionalidad para reasignar asiento no implementada.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onCancelarCompraClick(ActionEvent event) {
        mostrarAlerta("Información", "Cancelar Compra", "Funcionalidad para cancelar compra no implementada.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onRegistrarReembolsoClick(ActionEvent event) {
        mostrarAlerta("Información", "Registrar Reembolso", "Funcionalidad para registrar reembolso no implementada.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) lblIdCompra.getScene().getWindow();
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
