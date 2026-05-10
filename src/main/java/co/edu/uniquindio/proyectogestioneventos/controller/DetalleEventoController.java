package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Administrador;
import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.model.Zona;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class DetalleEventoController {

    @FXML private Text lblNombreEvento;
    @FXML private Label lblCiudad, lblFechaHora, lblRecinto, lblAforo, lblCategoria;
    @FXML private TextArea txtDescripcion, txtZonasPrecios, txtReglas;
    
    private Evento evento;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void setEvento(Evento evento) {
        if (evento == null) {
            mostrarAlerta("Error", "El evento seleccionado es inválido.", Alert.AlertType.ERROR);
            return;
        }
        this.evento = evento;
        cargarDatos();
    }

    private void cargarDatos() {
        if (evento.getRecinto() == null) {
            mostrarAlerta("Error", "Este evento no tiene un recinto configurado.", Alert.AlertType.ERROR);
            return;
        }

        lblNombreEvento.setText(evento.getNombre());
        lblCiudad.setText(evento.getCiudad());
        lblFechaHora.setText(evento.getFechaHora().format(formatter));
        lblRecinto.setText(evento.getRecinto().getNombre());
        lblCategoria.setText(evento.getCategoria());
        txtDescripcion.setText(evento.getDescripcion());
        
        int aforoTotal = evento.getRecinto().getListaZonas().stream().mapToInt(Zona::getCapacidad).sum();
        lblAforo.setText(String.valueOf(aforoTotal));

        if (evento.getRecinto().getListaZonas().isEmpty()) {
            txtZonasPrecios.setText("No hay zonas disponibles para este recinto.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Zona zona : evento.getRecinto().getListaZonas()) {
                double ocupacion = zona.calcularOcupacion();
                sb.append(String.format("- %s:\n  Precio Base: $%,.2f | Capacidad: %d | Ocupación: %.1f%%\n\n", 
                    zona.getNombre(), zona.getPrecioBase(), zona.getCapacidad(), ocupacion));
            }
            txtZonasPrecios.setText(sb.toString());
        }

        txtReglas.setText("1. Prohibido el ingreso de alimentos y bebidas.\n2. Llegar 30 min antes del inicio.\n3. Presentar entrada digital o impresa.");
    }

    @FXML
    void onSeleccionarEntradasClick(ActionEvent event) {
        if (evento == null || evento.getRecinto() == null || evento.getRecinto().getListaZonas().isEmpty()) {
            mostrarAlerta("Acción no permitida", "No se pueden seleccionar entradas para este evento en este momento.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // La vista SeleccionEntradasView.fxml está en la carpeta de administrador, asumiendo que es compartida.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/administrador/SeleccionEntradasView.fxml"));
            Parent root = loader.load();
            
            SeleccionEntradasController controller = loader.getController();
            controller.setEvento(this.evento);

            Stage stage = (Stage) lblNombreEvento.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Seleccionar Entradas - " + evento.getNombre());
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la vista de selección de entradas.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        try {
            // Navegar de vuelta a ExplorarEventosView.fxml
            // Asumiendo que ExplorarEventosView.fxml también está en la carpeta de administrador
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/administrador/explorarEventosView.fxml"));
            Parent root = loader.load();

            // Obtener el Stage actual y cambiar la escena
            Stage stage = (Stage) lblNombreEvento.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Explorar Eventos");

            // Opcional: Si ExplorarEventosController necesita inicialización específica al volver
            // ExplorarEventosController controller = loader.getController();
            // controller.cargarEventosPublicados(); // Por ejemplo, para refrescar la tabla
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la vista de exploración de eventos.", Alert.AlertType.ERROR);
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