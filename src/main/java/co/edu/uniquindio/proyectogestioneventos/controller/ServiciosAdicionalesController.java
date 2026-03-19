package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class ServiciosAdicionalesController {

    @FXML
    private Label lblCompra;
    @FXML
    private CheckBox chkAccesoVIP;
    @FXML
    private CheckBox chkSeguroCancelacion;
    @FXML
    private CheckBox chkMerchandising;
    @FXML
    private CheckBox chkParqueadero;
    @FXML
    private CheckBox chkAccesoPreferencial;

    private Compra compraActual;

    public void setCompra(Compra compra) {
        this.compraActual = compra;
        lblCompra.setText("Compra: " + compra.getIdCompra());
        // Lógica para pre-seleccionar servicios ya agregados a la compra

        // Añadir listener para la tecla ESC
        if (lblCompra.getScene() != null) {
            lblCompra.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    onVolverClick(null);
                }
            });
        }
    }

    @FXML
    void onAgregarServiciosClick(ActionEvent event) {
        System.out.println("Agregando servicios a la compra: " + compraActual.getIdCompra());
        // Lógica para añadir los servicios seleccionados a la compra (RF-009)
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) lblCompra.getScene().getWindow();
        stage.close();
    }
}
