package proyectogestioneventos.controller;

import proyectogestioneventos.MyApplication;
import proyectogestioneventos.model.Administrador;
import proyectogestioneventos.model.Usuario;
import proyectogestioneventos.model.Evento;
import proyectogestioneventos.model.Zona;
import proyectogestioneventos.service.IEventoService;
import proyectogestioneventos.service.impl.EventoServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class DetalleEventoController {

    @FXML private Text lblNombreEvento;
    @FXML private Label lblCiudad, lblFechaHora, lblRecinto, lblAforo, lblCategoria, lblEstado;
    @FXML private TextArea txtDescripcion, txtZonasPrecios, txtReglas;
    
    private final IEventoService eventoService = new EventoServiceImpl();
    private Evento evento;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void setEvento(Evento evento) {
        if (evento == null) {
            mostrarAlerta("Error", "El evento seleccionado es inválido.", Alert.AlertType.ERROR);
            return;
        }
        
        try {
            // Obtener datos actualizados del servicio para asegurar que el Recinto y Zonas estén cargados
            this.evento = eventoService.obtenerDetalleEvento(evento.getIdEvento())
                    .orElse(evento);
            
            cargarDatos();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error cargando evento: no se pudo recuperar la información.", Alert.AlertType.ERROR);
        }
    }

    private void cargarDatos() {
        if (evento == null) return;

        lblNombreEvento.setText(evento.getNombre());
        lblCiudad.setText(evento.getCiudad() != null ? evento.getCiudad() : "N/A");
        lblFechaHora.setText(evento.getFechaHora() != null ? evento.getFechaHora().format(formatter) : "N/A");
        lblCategoria.setText(evento.getCategoria() != null ? evento.getCategoria() : "N/A");
        lblEstado.setText(evento.getEstado() != null ? evento.getEstado().toString() : "N/A");
        txtDescripcion.setText(evento.getDescripcion() != null ? evento.getDescripcion() : "Sin descripción disponible.");
        
        if (evento.getRecinto() != null) {
            lblRecinto.setText(evento.getRecinto().getNombre());
            
            int aforoTotal = evento.getRecinto().getListaZonas().stream()
                    .filter(z -> z != null)
                    .mapToInt(Zona::getCapacidad)
                    .sum();
            lblAforo.setText(String.valueOf(aforoTotal));

            if (evento.getRecinto().getListaZonas().isEmpty()) {
                txtZonasPrecios.setText("No hay zonas disponibles configuradas.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Zona zona : evento.getRecinto().getListaZonas()) {
                    if (zona != null) {
                        sb.append(String.format("• %s\n  Precio: $%,.2f | Capacidad: %d | Ocupación: %.1f%%\n\n", 
                            zona.getNombre(), zona.getPrecioBase(), zona.getCapacidad(), zona.calcularOcupacion()));
                    }
                }
                txtZonasPrecios.setText(sb.toString());
            }
        } else {
            lblRecinto.setText("No asignado");
            lblAforo.setText("0");
            txtZonasPrecios.setText("Información de zonas no disponible.");
        }

        txtReglas.setText("1. Prohibido el ingreso de alimentos.\n2. Presentar documento de identidad.\n3. Prohibido fumar en el recinto.");
    }

    @FXML
    void onSeleccionarEntradasClick(ActionEvent event) {
        if (evento == null || evento.getRecinto() == null) {
            mostrarAlerta("Acción no permitida", "Este evento no permite selección de entradas actualmente.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Determinar carpeta según el rol para el flujo de compra
            Usuario user = MyApplication.getUsuarioLogueado();
            String folder = (user instanceof Administrador) ? "administrador" : "cliente";
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/" + folder + "/SeleccionEntradasView.fxml"));
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
            // Retornar a la vista de exploración según el rol (SesionUsuario)
            Usuario user = MyApplication.getUsuarioLogueado();
            String folder = (user instanceof Administrador) ? "administrador" : "cliente";
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/" + folder + "/explorarEventosView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) lblNombreEvento.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Explorar Eventos");
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