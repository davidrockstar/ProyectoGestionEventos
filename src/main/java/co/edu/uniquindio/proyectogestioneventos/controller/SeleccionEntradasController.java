package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoAsiento;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEntrada;
import co.edu.uniquindio.proyectogestioneventos.service.ICompraService;
import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.service.impl.CompraServiceImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SeleccionEntradasController {

    private final ICompraService compraService = new CompraServiceImpl();
    private Evento eventoActual;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label lblEvento;
    @FXML
    private ComboBox<Zona> cbZona;
    @FXML
    private ComboBox<Asiento> cbAsiento;
    @FXML
    private Spinner<Integer> spinnerCantidad;
    @FXML
    private TextArea txtMapaAsientos;

    public void setEvento(Evento evento) {
        this.eventoActual = evento;
        if (evento != null) {
            lblEvento.setText("Evento: " + evento.getNombre());
            cbZona.setItems(FXCollections.observableArrayList(evento.getRecinto().getListaZonas()));
            actualizarResumenOcupacion();
        }
    }

    @FXML
    private void initialize() {
        configurarCombos();
        
        cbZona.setOnAction(e -> {
            Zona seleccionada = cbZona.getValue();
            if (seleccionada != null && seleccionada.getListaAsientos() != null) {
                List<Asiento> disponibles = seleccionada.getListaAsientos().stream()
                        .filter(a -> a.getEstado() == EstadoAsiento.DISPONIBLE)
                        .collect(Collectors.toList());
                cbAsiento.setItems(FXCollections.observableArrayList(disponibles));
            }
        });

        // Añadir listener para la tecla ESC al panel raíz
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                onCancelarClick(null);
            }
        });
    }

    private void configurarCombos() {
        cbZona.setConverter(new StringConverter<>() {
            @Override public String toString(Zona z) { return z == null ? "" : z.getNombre() + " ($" + z.getPrecioBase() + ")"; }
            @Override public Zona fromString(String string) { return null; }
        });

        cbAsiento.setConverter(new StringConverter<>() {
            @Override public String toString(Asiento a) { return a == null ? "" : "Fila " + a.getFila() + " - N° " + a.getNumero(); }
            @Override public Asiento fromString(String string) { return null; }
        });
    }

    private void actualizarResumenOcupacion() {
        StringBuilder sb = new StringBuilder("--- ESTADO DE OCUPACIÓN ---\n");
        for (Zona z : eventoActual.getRecinto().getListaZonas()) {
            sb.append(String.format("Zona %s: %.1f%% ocupado (%d asientos)\n", 
                z.getNombre(), z.calcularOcupacion(), z.getCapacidad()));
        }
        txtMapaAsientos.setText(sb.toString());
    }

    @FXML
    void onAgregarACompraClick(ActionEvent event) {
        Zona zona = cbZona.getValue();
        Asiento asiento = cbAsiento.getValue();

        if (zona == null || (zona.getListaAsientos() != null && !zona.getListaAsientos().isEmpty() && asiento == null)) {
            mostrarAlerta("Error", "Debe seleccionar zona y asiento.");
            return;
        }

        // 1. Crear la entrada (Temporal hasta que se procese la compra)
        String idEntrada = "EN-" + UUID.randomUUID().toString().substring(0, 5);
        Entrada nuevaEntrada = new Entrada(idEntrada, zona, asiento, zona.getPrecioBase(), EstadoEntrada.ACTIVA);
        List<Entrada> entradas = new ArrayList<>();
        entradas.add(nuevaEntrada);

        // 2. Navegar al Proceso de Compra
        try {
            String basePath = (MyApplication.getUsuarioLogueado() instanceof Administrador) ? "administrador/" : "cliente/";
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/" + basePath + "ProcesoCompraView.fxml"));
            Parent root = loader.load();
            
            ProcesoCompraController controller = loader.getController();
            controller.setDatosCompra(eventoActual, entradas);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Finalizar Compra - " + eventoActual.getNombre());
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la vista de pago.");
        }
    }

    @FXML
    void onCancelarClick(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
