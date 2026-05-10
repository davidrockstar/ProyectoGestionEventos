package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Administrador;
import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.service.IEventoService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.EventoServiceImpl;
import co.edu.uniquindio.proyectogestioneventos.model.Taquilla;
import javafx.collections.ListChangeListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEvento;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ExplorarEventosController {

    private final IEventoService eventoService = new EventoServiceImpl();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Evento> tablaEventos;
    @FXML
    private TableColumn<Evento, String> colNombre;
    @FXML
    private TableColumn<Evento, String> colCategoria;
    @FXML
    private TableColumn<Evento, String> colCiudad;
    @FXML
    private TableColumn<Evento, String> colFecha;
    @FXML
    private TableColumn<Evento, String> colRecinto;
    @FXML
    private TableColumn<Evento, String> colEstado;
    @FXML
    private DatePicker campoFecha;
    @FXML
    private TextField campoCiudad;
    @FXML
    private TextField campoCategoria;
    @FXML
    private TextField campoPrecioMax;

    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        
        colFecha.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getFechaHora().format(formatter)));

        // Vinculaciones personalizadas para objetos anidados o Enums
        colRecinto.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getRecinto() != null ? cellData.getValue().getRecinto().getNombre() : "N/A"));
        
        if (colEstado != null) {
            colEstado.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getEstado().toString()));
        }

        cargarEventosPublicados(); // Carga inicial de eventos PUBLICADOS y válidos

        // Listener para actualizar la tabla si el administrador agrega un evento en tiempo real
        Taquilla.getInstance().getEventos().addListener((ListChangeListener<Evento>) c -> {
            cargarEventosPublicados();
        });

        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                onVolverClick(null);
            }
        });
    }

    @FXML
    private void cargarEventosPublicados() {
        // Obtiene todos los eventos y filtra para mostrar solo los PUBLICADOS, con recinto y fecha válidos
        List<Evento> todosLosEventos = eventoService.listarTodosEventos();
        List<Evento> eventosFiltrados = todosLosEventos.stream()
                .filter(e -> e.getEstado() == EstadoEvento.PUBLICADO)
                .filter(e -> e.getRecinto() != null)
                .filter(e -> e.getFechaHora() != null && e.getFechaHora().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
        cargarEventos(eventosFiltrados);
    }

    @FXML
    private void onFiltrarClick() {
        LocalDate fecha = campoFecha.getValue();
        String ciudad = campoCiudad.getText();
        String categoria = campoCategoria.getText();
        Double precioMaxAux = null;
        if (campoPrecioMax.getText() != null && !campoPrecioMax.getText().isEmpty()) {
            try {
                precioMaxAux = Double.parseDouble(campoPrecioMax.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El precio máximo debe ser un valor numérico.", Alert.AlertType.WARNING);
            }
        }
        final Double precioMax = precioMaxAux;
        
        // Iniciar el filtrado con la lista base de eventos PUBLICADOS y válidos
        List<Evento> baseEventos = eventoService.listarTodosEventos().stream()
                .filter(e -> e.getEstado() == EstadoEvento.PUBLICADO)
                .filter(e -> e.getRecinto() != null)
                .filter(e -> e.getFechaHora() != null && e.getFechaHora().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        List<Evento> eventosFiltrados = baseEventos.stream()
                .filter(e -> fecha == null || (e.getFechaHora() != null && e.getFechaHora().toLocalDate().equals(fecha)))
                .filter(e -> ciudad == null || ciudad.isEmpty() || (e.getCiudad() != null && e.getCiudad().equalsIgnoreCase(ciudad)))
                .filter(e -> categoria == null || categoria.isEmpty() || (e.getCategoria() != null && e.getCategoria().equalsIgnoreCase(categoria)))
                .filter(e -> precioMax == null || e.getRecinto().getListaZonas().stream().anyMatch(z -> z.getPrecioBase() <= precioMax))
                .collect(Collectors.toList());
        cargarEventos(eventosFiltrados); // Cargar la lista filtrada en la tabla
    }

    @FXML
    void onTablaMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            onVerDetalleClick(null);
        }
    }

    @FXML
    private void onVerDetalleClick(ActionEvent event) {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado == null) {
            mostrarAlerta("Advertencia", "Por favor seleccione un evento de la tabla.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // La vista DetalleEventoView.fxml está en la carpeta de administrador, asumiendo que es compartida.
            String resourcePath = "/co/edu/uniquindio/proyectogestioneventos/usuario/administrador/DetalleEventoView.fxml";
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent root = loader.load();
            
            DetalleEventoController controller = loader.getController();
            controller.setEvento(eventoSeleccionado);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Detalle del Evento - " + eventoSeleccionado.getNombre());
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la vista de detalle del evento.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onComprarEntradasClick(ActionEvent event) {
        Evento seleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selección Requerida", "Por favor seleccione un evento para iniciar la compra.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Navegación directa a la selección de entradas
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/administrador/SeleccionEntradasView.fxml"));
            Parent root = loader.load();
            
            SeleccionEntradasController controller = loader.getController();
            controller.setEvento(seleccionado);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Compra de Entradas - " + seleccionado.getNombre());
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar el módulo de selección de entradas.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }



    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cargarEventos(List<Evento> eventos) {
        tablaEventos.getItems().setAll(eventos);
    }
}
