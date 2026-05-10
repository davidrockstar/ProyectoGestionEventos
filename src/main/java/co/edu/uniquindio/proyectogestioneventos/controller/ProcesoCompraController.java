package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Entrada;
import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import co.edu.uniquindio.proyectogestioneventos.model.decorator.Comprable;
import co.edu.uniquindio.proyectogestioneventos.service.impl.CompraFacade;
import javafx.collections.FXCollections;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ProcesoCompraController {

    @FXML private CheckBox chkVIP, chkSeguro, chkMerch;
    @FXML private ComboBox<String> comboPago;
    @FXML private Label lblTotalCompra;
    @FXML private Label lblResumen;
    
    private final CompraFacade compraFacade = new CompraFacade();
    private Evento evento;
    private List<Entrada> entradasSeleccionadas;
    private double precioBase;

    @FXML
    public void initialize() {
        // Configurar opciones de pago
        comboPago.setItems(FXCollections.observableArrayList("PAYPAL", "STRIPE"));
        comboPago.getSelectionModel().selectFirst();

        // Listeners para actualizar el total en tiempo real
        chkVIP.setOnAction(e -> actualizarTotal());
        chkSeguro.setOnAction(e -> actualizarTotal());
        chkMerch.setOnAction(e -> actualizarTotal());
    }

    /**
     * Recibe la información desde el controlador anterior (SeleccionEntradas)
     */
    public void setDatosCompra(Evento evento, List<Entrada> entradas) {
        this.evento = evento;
        this.entradasSeleccionadas = entradas;
        
        // Calcular el precio base de las entradas seleccionadas
        this.precioBase = entradas.stream().mapToDouble(Entrada::getPrecioFinal).sum();
        
        lblResumen.setText(evento.getNombre() + " - " + entradas.size() + " entrada(s)");
        actualizarTotal();
    }

    /**
     * Calcula el total basándose en los costos definidos en tus Decoradores
     */
    private void actualizarTotal() {
        double total = precioBase;

        // Valores de referencia basados en las clases Decorator existentes
        if (chkVIP.isSelected()) total += 50.0; 
        if (chkSeguro.isSelected()) total += (total * 0.05); 
        if (chkMerch.isSelected()) total += 25.0;

        lblTotalCompra.setText(String.format("$ %.2f", total));
    }

    @FXML
    void onFinalizarCompra(ActionEvent event) {
        try {
            List<String> servicios = new ArrayList<>();
            if (chkVIP.isSelected()) servicios.add("VIP");
            if (chkSeguro.isSelected()) servicios.add("SEGURO");
            if (chkMerch.isSelected()) servicios.add("MERCHANDISING");

            double totalExtras = 0;
            if (chkVIP.isSelected()) totalExtras += 50.0;
            if (chkSeguro.isSelected()) totalExtras += (precioBase * 0.05);
            if (chkMerch.isSelected()) totalExtras += 25.0;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/cliente/CheckoutView.fxml"));
            Parent root = loader.load();

            CheckoutViewController controller = loader.getController();
            controller.setDatos(evento, entradasSeleccionadas, servicios, totalExtras);

            Stage stage = (Stage) comboPago.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Checkout - Resumen Final");
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}