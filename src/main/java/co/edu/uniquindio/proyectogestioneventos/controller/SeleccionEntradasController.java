package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoAsiento;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEntrada;
import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.service.IAsientoService;
import co.edu.uniquindio.proyectogestioneventos.service.IZonaService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.AsientoServiceImpl;
import co.edu.uniquindio.proyectogestioneventos.service.impl.ZonaServiceImpl;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    private final IAsientoService asientoService = new AsientoServiceImpl();
    private final IZonaService zonaService = new ZonaServiceImpl();
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
    @FXML
    private Button btnContinuar;

    public void setEvento(Evento evento) {
        this.eventoActual = evento;
        if (evento != null) {
            // Asegurar que los asientos existan en el modelo para calcular disponibilidad real
            evento.getRecinto().getListaZonas().forEach(z -> {
                try {
                    asientoService.generarDatosPrueba(evento.getRecinto().getIdRecinto(), z.getIdZona());
                } catch (Exception e) {
                    System.err.println("Error pre-cargando asientos: " + e.getMessage());
                }
            });
            lblEvento.setText(evento.getNombre());
            cargarZonas();
            actualizarResumenOcupacion();
        }
    }

    private void cargarZonas() {
        if (eventoActual != null && eventoActual.getRecinto() != null) {
            List<Zona> zonas = zonaService.listarZonas(eventoActual.getRecinto().getIdRecinto());
            cbZona.setItems(FXCollections.observableArrayList(zonas));
        }
    }

    @FXML
    private void initialize() {
        configurarCombos();

        // El botón solo se habilita si hay un asiento seleccionado
        if (btnContinuar != null) {
            btnContinuar.disableProperty().bind(cbAsiento.valueProperty().isNull());
        }
        
        cbZona.setOnAction(e -> {
            Zona seleccionada = cbZona.getValue();
            if (seleccionada != null) {
                cargarAsientos(seleccionada);
            }
        });

        // Añadir listener para la tecla ESC al panel raíz
        if (rootPane != null) {
            rootPane.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    onCancelarClick(null);
                }
            });
        }
    }

    private void cargarAsientos(Zona zona) {
        try {
            String idRecinto = eventoActual.getRecinto().getIdRecinto();
            
            List<Asiento> todosLosAsientos = asientoService.listarAsientos(idRecinto, zona.getIdZona());
            
            // Actualizar el resumen de ocupación en la UI
            actualizarResumenOcupacion();

            // Filtrar solo los disponibles para el usuario
            List<Asiento> disponibles = todosLosAsientos.stream()
                    .filter(a -> a.getEstado() == EstadoAsiento.DISPONIBLE)
                    .collect(Collectors.toList());
            
            cbAsiento.setItems(FXCollections.observableArrayList(disponibles));
            
            if (disponibles.isEmpty()) {
                cbAsiento.setPromptText("Sin asientos disponibles");
            } else {
                cbAsiento.setPromptText("Seleccione un asiento");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudieron cargar los asientos de la zona.");
        }
    }

    private void configurarCombos() {
        cbZona.setConverter(new StringConverter<>() {
            @Override public String toString(Zona z) { 
                if (z == null) return "";
                long disponibles = z.getListaAsientos().stream()
                        .filter(a -> a.getEstado() == EstadoAsiento.DISPONIBLE)
                        .count();
                return String.format("%s - $%,.0f (Disp: %d/%d)", 
                    z.getNombre(), z.getPrecioBase(), disponibles, z.getCapacidad()); 
            }
            @Override public Zona fromString(String string) { return null; }
        });

        cbAsiento.setConverter(new StringConverter<>() {
            @Override public String toString(Asiento a) { 
                return a == null ? "" : "[" + a.getIdAsiento() + "] Fila " + a.getFila() + " - Num " + a.getNumero(); }
            @Override public Asiento fromString(String string) { return null; }
        });
    }

    private void actualizarResumenOcupacion() {
        StringBuilder sb = new StringBuilder("--- ESTADO DE OCUPACIÓN ---\n");
        for (Zona z : eventoActual.getRecinto().getListaZonas()) {
            long disp = z.getListaAsientos().stream()
                    .filter(a -> a.getEstado() == EstadoAsiento.DISPONIBLE)
                    .count();
            sb.append(String.format("Zona %s: %.1f%% ocupado (%d asientos)\n", 
                z.getNombre(), z.calcularOcupacion(), disp, z.getCapacidad()));
        }
        txtMapaAsientos.setText(sb.toString());
    }

    @FXML
    void onContinuarCheckoutClick(ActionEvent event) {
        Zona zona = cbZona.getValue();
        Asiento asiento = cbAsiento.getValue();

        // 1. Validaciones de selección real
        if (zona == null) {
            mostrarAlerta("Campo requerido", "Por favor seleccione una zona.");
            return;
        }
        if (asiento == null || asiento.getIdAsiento() == null) {
            mostrarAlerta("Campo requerido", "Por favor seleccione un asiento específico.");
            return;
        }

        // 2. Validación de estado real (Doble reserva)
        if (asiento.getEstado() != EstadoAsiento.DISPONIBLE) {
            mostrarAlerta("Asiento Ocupado", "Este asiento ya no se encuentra disponible. Por favor elija otro.");
            cargarAsientos(zona);
            return;
        }


        try {
            // 1. Efectuar reserva real en el Servicio (DISPONIBLE -> RESERVADO)
            asientoService.cambiarEstadoAsiento(
                eventoActual.getRecinto().getIdRecinto(),
                zona.getIdZona(), 
                asiento.getIdAsiento(), 
                EstadoAsiento.RESERVADO
            );

            // 2. Crear la entrada temporal
            String idEntrada = "EN-" + UUID.randomUUID().toString().substring(0, 5);
            Entrada nuevaEntrada = new Entrada(idEntrada, zona, asiento, zona.getPrecioBase(), EstadoEntrada.ACTIVA);
            List<Entrada> entradas = new ArrayList<>();
            entradas.add(nuevaEntrada);

            // 3. Navegar DIRECTAMENTE al Checkout como se solicitó en el flujo obligatorio
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/cliente/CheckoutView.fxml"));
            Parent root = loader.load();
            
            CheckoutViewController controller = loader.getController();
            
            // Enviamos los datos reales al Checkout
            List<String> extras = new ArrayList<>();
            controller.setDatos(eventoActual, entradas, extras, 0.0);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Finalizar Compra - " + eventoActual.getNombre());
        } catch (Exception e) {
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
