package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.model.enums.Rol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DetalleEventoController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Text lblNombreEvento;
    @FXML
    private Label lblCiudad;
    @FXML
    private Label lblFechaHora;
    @FXML
    private Label lblRecinto;
    @FXML
    private Label lblAforo;
    @FXML
    private Label lblCategoria;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private TextArea txtZonasPrecios;
    @FXML
    private TextArea txtReglas;

    private Evento eventoActual;

    public void setEvento(Evento evento) {
        this.eventoActual = evento;
        // Cargar datos del evento en la UI
        lblNombreEvento.setText(evento.getNombre());
        lblCiudad.setText(evento.getCiudad());
        lblFechaHora.setText(evento.getFechaHora().toString());
        lblRecinto.setText(evento.getRecinto().getNombre());
        // lblAforo.setText(String.valueOf(evento.getCapacidadMaxima())); // Asumiendo que Evento tiene capacidadMaxima
        lblCategoria.setText(evento.getCategoria());
        txtDescripcion.setText(evento.getDescripcion());
        // Construir string de zonas y precios
        StringBuilder zonasPrecios = new StringBuilder();
        evento.getRecinto().getListaZonas().forEach(zona ->
                zonasPrecios.append(zona.getNombre()).append(": $").append(zona.getPrecioBase()).append("\n")
        );
        txtZonasPrecios.setText(zonasPrecios.toString());
        txtReglas.setText("No se permite el ingreso de alimentos y bebidas externos."); // Ejemplo de regla
    }

    @FXML
    private void initialize() {
        // Añadir listener para la tecla ESC al panel raíz
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                onVolverClick(null);
            }
        });
    }

    @FXML
    void onSeleccionarEntradasClick(ActionEvent event) {
        try {
            Usuario usuario = MyApplication.getUsuarioLogueado();
            String basePath = (usuario != null && usuario.getRol() == Rol.ADMINISTRADOR) ? "administrador/" : "cliente/";
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/" + basePath + "SeleccionEntradasView.fxml"));
            Parent root = loader.load();
            SeleccionEntradasController controller = loader.getController();
            controller.setEvento(eventoActual); // Pasar el evento a la pantalla de selección de entradas

            Stage stage = new Stage();
            stage.setTitle("Selección de Entradas para " + eventoActual.getNombre());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Stage)((javafx.scene.control.Button)event.getSource()).getScene().getWindow()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
