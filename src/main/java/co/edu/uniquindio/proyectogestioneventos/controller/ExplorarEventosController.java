package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import co.edu.uniquindio.proyectogestioneventos.service.IEventoService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.EventoServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ExplorarEventosController {

    private final IEventoService eventoService = new EventoServiceImpl();

    @FXML
    private TableView<Evento> tablaEventos;
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
        // Cargar eventos iniciales
        cargarEventos(eventoService.listarEventosDisponibles());
    }

    @FXML
    private void onFiltrarClick() {
        LocalDate fecha = campoFecha.getValue();
        String ciudad = campoCiudad.getText();
        String categoria = campoCategoria.getText();
        Double precioMax = null;
        try {
            precioMax = Double.parseDouble(campoPrecioMax.getText());
        } catch (NumberFormatException e) {
            // Manejar error si el precio no es un número válido
        }

        List<Evento> eventosFiltrados = eventoService.filtrarEventos(fecha, ciudad, categoria, precioMax);
        cargarEventos(eventosFiltrados);
    }

    @FXML
    private void onVerDetalleClick(ActionEvent event) {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/view/DetalleEventoView.fxml"));
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
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void cargarEventos(List<Evento> eventos) {
        tablaEventos.getItems().setAll(eventos);
        // Aquí se necesitaría configurar las celdas de las columnas para mostrar los datos del Evento
    }
}
