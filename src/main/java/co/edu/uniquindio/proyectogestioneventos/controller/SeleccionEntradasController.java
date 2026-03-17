package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SeleccionEntradasController {

    @FXML
    private Label lblEvento;
    @FXML
    private ComboBox<String> cbZona;
    @FXML
    private ComboBox<String> cbAsiento;
    @FXML
    private Spinner<Integer> spinnerCantidad;
    @FXML
    private TextArea txtMapaAsientos;

    private Evento eventoActual;

    public void setEvento(Evento evento) {
        this.eventoActual = evento;
        lblEvento.setText("Evento: " + evento.getNombre());
        // Cargar zonas y asientos disponibles para el evento
        // cbZona.getItems().addAll(evento.getRecinto().getListaZonas().stream().map(Zona::getNombre).collect(Collectors.toList()));
        // txtMapaAsientos.setText(generarMapaAsientos(evento)); // Método auxiliar para simular el mapa
    }

    @FXML
    void onAgregarACompraClick(ActionEvent event) {
        // Lógica para agregar entradas a una compra (RF-005)
        System.out.println("Entradas agregadas a la compra.");
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void onCancelarClick(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
