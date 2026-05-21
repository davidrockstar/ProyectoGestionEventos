package proyectogestioneventos.viewcontroller;

import proyectogestioneventos.controller.DetalleCompraController;
import proyectogestioneventos.model.*;
import proyectogestioneventos.model.*;
import proyectogestioneventos.model.enums.EstadoAsiento;
import proyectogestioneventos.service.ICompraService;
import proyectogestioneventos.service.impl.CompraServiceImpl;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;

public class GestionComprasAdminViewController {

    @FXML private AnchorPane rootPane;
    @FXML private TableView<Compra> tablaCompras;
    @FXML private TableColumn<Compra, String> colId, colUsuario, colEvento, colFecha, colEstado;
    @FXML private TableColumn<Compra, Double> colTotal;

    private final ICompraService compraService = new CompraServiceImpl();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        configurarTabla();
        actualizarTabla();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idCompra"));
        
        // Obtener nombre del usuario desde el objeto Compra
        colUsuario.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getUsuario() != null ? 
                cellData.getValue().getUsuario().getNombre() : "N/A"));

        // Obtener nombre del evento
        colEvento.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEvento() != null ? 
                cellData.getValue().getEvento().getNombre() : "N/A"));

        // Formatear fecha
        colFecha.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getFechaCreacion() != null ? 
                cellData.getValue().getFechaCreacion().format(formatter) : "N/A"));

        // precioTotal es double, TableColumn espera Double
        colTotal.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));
        
        colEstado.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEstado().toString()));
    }

    private void actualizarTabla() {
        List<Compra> lista = compraService.listarTodasLasCompras();
        if (lista.isEmpty()) {
            generarDatosPrueba();
            lista = compraService.listarTodasLasCompras();
        }
        tablaCompras.setItems(FXCollections.observableArrayList(lista));
    }

    private void generarDatosPrueba() {
        // Buscar un usuario y un evento para simular la compra
        Taquilla taquilla = Taquilla.getInstance();
        if (!taquilla.getUsuarios().isEmpty() && !taquilla.getEventos().isEmpty()) {
            Usuario user = taquilla.getUsuarios().get(0);
            Evento ev = taquilla.getEventos().get(0);
            compraService.crearCompra(user, ev, new ArrayList<>());
        }
    }

    @FXML
    void onVerDetalleClick(ActionEvent event) {
        Compra seleccionada = tablaCompras.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Seleccione una compra para ver el detalle.", Alert.AlertType.WARNING);
            return;
        }

        if (seleccionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyectogestioneventos/usuario/administrador/DetalleCompraView.fxml"));
                Parent root = loader.load();
                
                DetalleCompraController controller = loader.getController();
                controller.setCompra(seleccionada);

                Stage stage = new Stage();
                stage.setTitle("Detalle de Compra - Admin");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(rootPane.getScene().getWindow());
                stage.showAndWait();
                actualizarTabla();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onCancelarCompraClick(ActionEvent event) {
        Compra seleccionada = tablaCompras.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Seleccione una compra para cancelar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Cancelación");
        confirm.setHeaderText("¿Está seguro de cancelar la compra " + seleccionada.getIdCompra() + "?");
        confirm.setContentText("Esta acción cambiará el estado a CANCELADA y liberará los asientos correspondientes.");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            compraService.cancelarCompra(seleccionada);
            actualizarTabla();
            mostrarAlerta("Éxito", "La compra ha sido cancelada.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void onReasignarAsientoClick(ActionEvent event) {
        Compra seleccionada = tablaCompras.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Seleccione una compra para reasignar asientos.", Alert.AlertType.WARNING);
            return;
        }

        if (seleccionada.getListaEntradas().isEmpty()) {
            mostrarAlerta("Error", "La compra seleccionada no tiene entradas asociadas.", Alert.AlertType.ERROR);
            return;
        }

        // 1. Seleccionar la entrada a modificar
        List<Entrada> entradas = seleccionada.getListaEntradas();
        ChoiceDialog<Entrada> dialogEntrada = new ChoiceDialog<>(entradas.get(0), entradas);
        dialogEntrada.setTitle("Reasignar Asiento");
        dialogEntrada.setHeaderText("Paso 1: Seleccione la entrada");
        dialogEntrada.setContentText("Entrada:");

        Optional<Entrada> resultEntrada = dialogEntrada.showAndWait();
        if (resultEntrada.isPresent()) {
            Entrada entrada = resultEntrada.get();
            
            // 2. Buscar asientos disponibles en la misma zona
            if (entrada.getZona() == null || entrada.getZona().getListaAsientos() == null) {
                mostrarAlerta("Error", "La zona de esta entrada no tiene asientos configurados.", Alert.AlertType.ERROR);
                return;
            }

            List<Asiento> disponibles = entrada.getZona().getListaAsientos().stream()
                    .filter(a -> a.getEstado() == EstadoAsiento.DISPONIBLE)
                    .collect(Collectors.toList());

            if (disponibles.isEmpty()) {
                mostrarAlerta("Sin Disponibilidad", "No hay más asientos disponibles en la zona: " + entrada.getZona().getNombre(), Alert.AlertType.WARNING);
                return;
            }

            // 3. Seleccionar el nuevo asiento usando una lista de Strings para la visualización
            List<String> opcionesVisuales = disponibles.stream()
                    .map(a -> a.getCodigo() + " (Fila: " + a.getFila() + ", Número: " + a.getNumero() + ")")
                    .collect(Collectors.toList());

            ChoiceDialog<String> dialogAsiento = new ChoiceDialog<>(opcionesVisuales.get(0), opcionesVisuales);
            dialogAsiento.setTitle("Reasignar Asiento");
            dialogAsiento.setHeaderText("Paso 2: Seleccione el nuevo asiento para " + entrada.getZona().getNombre());
            dialogAsiento.setContentText("Nuevo Asiento:");

            Optional<String> resultAsiento = dialogAsiento.showAndWait();
            if (resultAsiento.isPresent()) {
                try {
                    int index = opcionesVisuales.indexOf(resultAsiento.get());
                    Asiento asientoSeleccionado = disponibles.get(index);
                    compraService.reasignarAsiento(seleccionada, entrada, asientoSeleccionado);
                    actualizarTabla();
                    mostrarAlerta("Éxito", "Asiento reasignado correctamente.", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }
    }

    @FXML
    void onRegistrarReembolsoClick(ActionEvent event) {
        Compra seleccionada = tablaCompras.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Seleccione una compra para registrar el reembolso.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Reembolso");
        confirm.setHeaderText("¿Está seguro de procesar el reembolso de la compra " + seleccionada.getIdCompra() + "?");
        confirm.setContentText("El estado cambiará a REEMBOLSADA y los asientos quedarán DISPONIBLES.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                compraService.registrarReembolso(seleccionada);
                actualizarTabla();
                mostrarAlerta("Éxito", "Reembolso registrado y asientos liberados.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}