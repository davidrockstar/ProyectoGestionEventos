package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Entrada;
import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import co.edu.uniquindio.proyectogestioneventos.model.decorator.Comprable;
import co.edu.uniquindio.proyectogestioneventos.service.impl.CompraFacade;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
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
        if (evento == null || entradasSeleccionadas == null || entradasSeleccionadas.isEmpty()) {
            mostrarAlerta("Error", "No hay datos de compra válidos.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // 1. Recopilar servicios adicionales (Patrón Decorator)
            List<String> servicios = new ArrayList<>();
            if (chkVIP.isSelected()) servicios.add("VIP");
            if (chkSeguro.isSelected()) servicios.add("SEGURO");
            if (chkMerch.isSelected()) servicios.add("MERCHANDISING");

            // 2. Obtener método de pago seleccionado
            String tipoPago = comboPago.getValue();

            // 3. Ejecutar a través de la Fachada
            String idUsuario = MyApplication.getUsuarioLogueado().getIdUsuario();
            
            Comprable resultado = compraFacade.realizarCompraCompleta(
                idUsuario, 
                evento.getIdEvento(), 
                entradasSeleccionadas, 
                servicios, 
                tipoPago
            );

            mostrarAlerta("Compra Exitosa", "Su pedido ha sido procesado:\n" + resultado.getDescripcion(), Alert.AlertType.INFORMATION);
            
            // Cerrar la ventana y volver al panel principal
            ((Stage) comboPago.getScene().getWindow()).close();
            
        } catch (Exception e) {
            mostrarAlerta("Error en la Compra", e.getMessage(), Alert.AlertType.ERROR);
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