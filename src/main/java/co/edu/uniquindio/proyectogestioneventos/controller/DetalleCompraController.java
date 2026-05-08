package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import co.edu.uniquindio.proyectogestioneventos.model.ServicioAdicional;
import co.edu.uniquindio.proyectogestioneventos.model.decorator.Comprable;
import co.edu.uniquindio.proyectogestioneventos.model.decorator.VIPDecorator;
import co.edu.uniquindio.proyectogestioneventos.model.decorator.MerchandisingDecorator;
import co.edu.uniquindio.proyectogestioneventos.model.decorator.SeguroDecorator;
import co.edu.uniquindio.proyectogestioneventos.pago.IPago;
import co.edu.uniquindio.proyectogestioneventos.pago.PayPalAdapter;
import co.edu.uniquindio.proyectogestioneventos.pago.externo.PayPalGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
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
        actualizarDetalles();

        // Añadir listener para la tecla ESC
        if (lblIdCompra.getScene() != null) {
            lblIdCompra.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    onVolverClick(null);
                }
            });
        }
    }

    private void actualizarDetalles() {
        lblIdCompra.setText("Detalle de Compra: " + compraActual.getIdCompra());
        lblEvento.setText(compraActual.getEvento().getNombre());
        lblFechaCreacion.setText(compraActual.getFechaCreacion().toLocalDate().toString());
        lblEstado.setText(compraActual.getEstado().toString());

        // Construir string de entradas
        StringBuilder entradasStr = new StringBuilder();
        compraActual.getListaEntradas().forEach(entrada ->
                entradasStr.append("Zona: ").append(entrada.getZona().getNombre())
                        .append(", Asiento: ").append(entrada.getAsiento() != null ? entrada.getAsiento().getFila() + entrada.getAsiento().getNumero() : "N/A")
                        .append(", Precio: $").append(entrada.getPrecioFinal()).append("\n")
        );
        txtEntradas.setText(entradasStr.toString());

        // Re-aplicar decoradores para calcular el precio total y obtener la descripción completa
        Comprable comprableDecorado = compraActual; // La compra base es el primer Comprable
        StringBuilder serviciosStr = new StringBuilder();

        for (ServicioAdicional servicio : compraActual.getListaServiciosAdicionales()) {
            if ("Acceso VIP".equalsIgnoreCase(servicio.getNombre())) {
                comprableDecorado = new VIPDecorator(comprableDecorado);
                serviciosStr.append("Acceso VIP (+$50.0)\n");
            } else if ("Seguro de Cancelación".equalsIgnoreCase(servicio.getNombre())) {
                comprableDecorado = new SeguroDecorator(comprableDecorado);
                serviciosStr.append("Seguro de Cancelación (5%)\n");
            } else if ("Merchandising Oficial".equalsIgnoreCase(servicio.getNombre())) {
                comprableDecorado = new MerchandisingDecorator(comprableDecorado);
                serviciosStr.append("Kit Merchandising (+$25.0)\n");
            }
        }
        txtServicios.setText(serviciosStr.toString());

        lblTotal.setText("$ " + String.format("%.2f", comprableDecorado.getPrecioTotal()));
    }

    @FXML
    void onAgregarServiciosClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/cliente/ServiciosAdicionalesView.fxml"));
            Parent root = loader.load();
            ServiciosAdicionalesController controller = loader.getController();
            controller.setCompra(compraActual);

            Stage stage = new Stage();
            stage.setTitle("Agregar Servicios Adicionales");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Stage)((javafx.scene.control.Button)event.getSource()).getScene().getWindow()));
            stage.showAndWait();
            // Actualizar la vista después de agregar servicios
            actualizarDetalles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onPagarCompraClick(ActionEvent event) {
        // En una aplicación real, aquí se abriría un diálogo para que el usuario elija el método de pago.
        // Por simplicidad, usaremos PayPal por defecto para la demostración.
        IPago metodoDePago = new PayPalAdapter(new PayPalGateway());
        compraActual.setMetodoPago(metodoDePago);

        // La lógica de pago ahora se delega al estado actual de la compra
        compraActual.pagar();

        // Muestra una alerta con el resultado
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Proceso de Pago");
        alert.setHeaderText("Resultado del pago para la compra " + compraActual.getIdCompra());
        alert.setContentText("El nuevo estado de la compra es: " + compraActual.getEstado());
        alert.showAndWait();

        actualizarDetalles(); // Actualiza la UI para reflejar el cambio de estado
    }

    @FXML
    void onCancelarCompraClick(ActionEvent event) {
        // La lógica de cancelación se delega al estado actual
        compraActual.cancelar();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Proceso de Cancelación");
        alert.setHeaderText("Resultado de la cancelación para la compra " + compraActual.getIdCompra());
        alert.setContentText("El nuevo estado de la compra es: " + compraActual.getEstado());
        alert.showAndWait();

        actualizarDetalles(); // Actualiza la UI
    }



    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) lblIdCompra.getScene().getWindow();
        stage.close();
    }
}
