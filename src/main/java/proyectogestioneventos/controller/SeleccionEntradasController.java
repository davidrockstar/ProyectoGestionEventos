package proyectogestioneventos.controller;

import proyectogestioneventos.model.*;
import proyectogestioneventos.model.Asiento;
import proyectogestioneventos.model.Entrada;
import proyectogestioneventos.model.Evento;
import proyectogestioneventos.model.Zona;
import proyectogestioneventos.model.enums.EstadoAsiento;
import proyectogestioneventos.model.enums.EstadoEntrada;
import proyectogestioneventos.MyApplication;
import proyectogestioneventos.service.IAsientoService;
import proyectogestioneventos.service.IZonaService;
import proyectogestioneventos.service.impl.AsientoServiceImpl;
import proyectogestioneventos.service.impl.ZonaServiceImpl;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

import java.util.ArrayList;
import java.util.List;
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
    @FXML
    private TableView<Asiento> tablaAsientos;
    @FXML
    private TableColumn<Asiento, String> colCodigo, colFila, colEstado;
    @FXML
    private TableColumn<Asiento, Integer> colNumero; // Cambiado a Integer para coincidir con el modelo

    public void setEvento(Evento evento) {
        if (evento == null) {
            mostrarAlerta("Error de Carga", "El evento seleccionado no es válido.", Alert.AlertType.ERROR);
            return;
        }

        this.eventoActual = evento;
        lblEvento.setText(eventoActual.getNombre());
        cargarZonas();
        actualizarResumenOcupacion();
    }

    private void cargarZonas() {
        cbZona.getItems().clear();
        if (eventoActual != null && eventoActual.getRecinto() != null) {
            List<Zona> listaZonas = eventoActual.getRecinto().getListaZonas();
            if (listaZonas != null && !listaZonas.isEmpty()) {
                cbZona.setItems(FXCollections.observableArrayList(listaZonas));
            }
        }
    }

    @FXML
    private void initialize() {
        configurarCombos();
        
        // Configuración de columnas de la tabla de asientos (usando el nuevo atributo 'codigo')
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colFila.setCellValueFactory(new PropertyValueFactory<>("fila"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Listener para cargar asientos cuando cambia la zona seleccionada
        cbZona.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarAsientos(newVal);
            } else {
                tablaAsientos.getItems().clear();
            }
        });

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
        // Limpiar tabla y validar nulos
        tablaAsientos.getItems().clear();
        if (zona == null || zona.getListaAsientos() == null) return;

        // Filtrar exclusivamente los asientos con estado DISPONIBLE
        List<Asiento> disponibles = zona.getListaAsientos().stream()
                .filter(a -> a != null && a.getEstado() == EstadoAsiento.DISPONIBLE)
                .collect(Collectors.toList());

        // Cargar los datos filtrados en la tabla
        tablaAsientos.setItems(FXCollections.observableArrayList(disponibles));
        
        // Sincronizar también el ComboBox de asientos si sigue en uso
        cbAsiento.setItems(FXCollections.observableArrayList(disponibles));
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
        // 1. Validar selección completa (Evento, Zona, Asiento)
        if (eventoActual == null) {
            mostrarAlerta("Error", "No se ha cargado la información del evento.", Alert.AlertType.ERROR);
            return;
        }

        Zona zona = cbZona.getValue();
        Asiento asiento = cbAsiento.getValue();

        if (zona == null) {
            mostrarAlerta("Selección Incompleta", "Por favor seleccione una zona.", Alert.AlertType.WARNING);
            return;
        }
        if (asiento == null || asiento.getIdAsiento() == null) {
            mostrarAlerta("Selección Incompleta", "Por favor seleccione un asiento específico.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // 2. Cambiar Estado: DISPONIBLE -> RESERVADO (Seguridad contra doble venta)
            if (asiento.getEstado() != EstadoAsiento.DISPONIBLE) {
                throw new Exception("El asiento ya no está disponible.");
            }

            asientoService.cambiarEstadoAsiento(
                eventoActual.getRecinto().getIdRecinto(),
                zona.getIdZona(), 
                asiento.getIdAsiento(), 
                EstadoAsiento.RESERVADO
            );

            // 3. Generar entrada temporal
            Long idEntrada = (long) (System.currentTimeMillis() % 1_000_000); // Generar un ID Long simple
            Entrada nuevaEntrada = new Entrada(idEntrada, // Nuevo Long ID
                                              zona, asiento, zona.getPrecioBase(), EstadoEntrada.VALIDADA,
                                              eventoActual, // El evento actual
                                              MyApplication.getUsuarioLogueado()); // El usuario logueado es el propietario
            List<Entrada> entradas = new ArrayList<>();
            entradas.add(nuevaEntrada);

            // 4. Abrir CheckoutView enviando datos reales
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyectogestioneventos/usuario/cliente/CheckoutView.fxml")); // Mantener la ruta existente
            Parent root = loader.load();
            
            CheckoutViewController controller = loader.getController();
            controller.setDatos(eventoActual, entradas, new ArrayList<>(), 0.0);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Finalizar Compra - " + eventoActual.getNombre());
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
            cargarAsientos(zona); // Refrescar para mostrar estado real
        }
    }

    @FXML
    void onCancelarClick(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
