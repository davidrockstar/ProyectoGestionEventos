package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DetalleCompraController {

    @FXML
    private Text lblIdCompra;
    @FXML
    private Label lblEvento;
    @FXML
    private Label lblFechaCreacion;
    @FXML
    private Label lblEstado;
    @FXML
    private TextArea txtEntradas;
    @FXML
    private TextArea txtServicios;
    @FXML
    private Text lblTotal;

    private Compra compraActual;

    public void setCompra(Compra compra) {
        this.compraActual = compra;
        lblIdCompra.setText("Detalle de Compra: " + compra.getIdCompra());
        lblEvento.setText(compra.getEvento().getNombre());
        lblFechaCreacion.setText(compra.getFechaCreacion().toLocalDate().toString());
        lblEstado.setText(compra.getEstadoCompra().toString());
        // Construir strings de entradas y servicios
        StringBuilder entradasStr = new StringBuilder();
        compra.getListaEntradas().forEach(entrada ->
                entradasStr.append("Zona: ").append(entrada.getZona().getNombre())
                        .append(", Asiento: ").append(entrada.getAsiento().getFila()).append(entrada.getAsiento().getNumero())
                        .append(", Precio: $").append(entrada.getPrecioFinal()).append("\n")
        );
        txtEntradas.setText(entradasStr.toString());

        StringBuilder serviciosStr = new StringBuilder();
        compra.getListaServicios().forEach(servicio ->
                serviciosStr.append(servicio.getNombre()).append(": $").append(servicio.getPrecio()).append("\n")
        );
        txtServicios.setText(serviciosStr.toString());

        lblTotal.setText("$ " + String.format("%.2f", compra.getTotal()));
    }

    @FXML
    void onAgregarServiciosClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/view/ServiciosAdicionalesView.fxml"));
            Parent root = loader.load();
            ServiciosAdicionalesController controller = loader.getController();
            controller.setCompra(compraActual);

            Stage stage = new Stage();
            stage.setTitle("Agregar Servicios Adicionales");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Stage)((javafx.scene.control.Button)event.getSource()).getScene().getWindow()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onPagarCompraClick(ActionEvent event) {
        System.out.println("Pagar compra: " + compraActual.getIdCompra());
        // Lógica para procesar el pago (RF-007)
    }

    @FXML
    void onCancelarCompraClick(ActionEvent event) {
        System.out.println("Cancelar compra: " + compraActual.getIdCompra());
        // Lógica para cancelar la compra (RF-006)
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
