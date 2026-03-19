package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Administrador;
import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.service.IEventoService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.EventoServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ExplorarEventosController {

    private final IEventoService eventoService = new EventoServiceImpl();

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
    private TableColumn<Evento, LocalDateTime> colFecha;
    @FXML
    private TableColumn<Evento, String> colRecinto;
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
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        colRecinto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRecinto().getNombre()));

        cargarEventos(eventoService.listarEventosDisponibles());

        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                onVolverClick(null);
            }
        });
    }

    @FXML
    private void onFiltrarClick() {
        LocalDate fecha = campoFecha.getValue();
        String ciudad = campoCiudad.getText();
        String categoria = campoCategoria.getText();
        Double precioMax = null;
        if (campoPrecioMax.getText() != null && !campoPrecioMax.getText().isEmpty()) {
            try {
                precioMax = Double.parseDouble(campoPrecioMax.getText());
            } catch (NumberFormatException e) {
                // Ignorar si no es un número
            }
        }
        List<Evento> eventosFiltrados = eventoService.filtrarEventos(fecha, ciudad, categoria, precioMax);
        cargarEventos(eventosFiltrados);
    }

    @FXML
    private void onVerDetalleClick(ActionEvent event) {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            try {
                Usuario usuario = MyApplication.getUsuarioLogueado();
                String basePath = (usuario instanceof Administrador) ? "administrador/" : "cliente/";
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/" + basePath + "DetalleEventoView.fxml"));
                Parent root = loader.load();
                DetalleEventoController controller = loader.getController();
                controller.setEvento(eventoSeleccionado);

                Stage stage = new Stage();
                stage.setTitle("Detalle del Evento");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(((Stage)((javafx.scene.control.Button)event.getSource()).getScene().getWindow()));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }



    private void cargarEventos(List<Evento> eventos) {
        tablaEventos.getItems().setAll(eventos);
    }
}
