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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DetalleEventoController {

    @FXML private Text lblNombreEvento;
    @FXML private Label lblCiudad, lblFechaHora, lblRecinto, lblAforo, lblCategoria;
    @FXML private TextArea txtDescripcion, txtZonasPrecios, txtReglas;
    
    private Evento evento;

    public void setEvento(Evento evento) {
        this.evento = evento;
        if (evento != null) {
            cargarDatos();
        }
    }

    private void cargarDatos() {
        lblNombreEvento.setText(evento.getNombre());
        lblCiudad.setText(evento.getCiudad());
        lblFechaHora.setText(evento.getFechaHora().toString());
        lblRecinto.setText(evento.getRecinto() != null ? evento.getRecinto().getNombre() : "N/A");
        lblCategoria.setText(evento.getCategoria());
        txtDescripcion.setText(evento.getDescripcion());
        
        int aforoTotal = evento.getRecinto().getListaZonas().stream().mapToInt(Zona::getCapacidad).sum();
        lblAforo.setText(String.valueOf(aforoTotal));

        StringBuilder sb = new StringBuilder();
        for (Zona zona : evento.getRecinto().getListaZonas()) {
            sb.append(String.format("- %s: $%,.2f (Capacidad: %d)\n", 
                zona.getNombre(), zona.getPrecioBase(), zona.getCapacidad()));
        }
        txtZonasPrecios.setText(sb.toString());
        txtReglas.setText("1. Prohibido el ingreso de alimentos.\n2. Llegar 30 min antes.");
    }

    @FXML
    void onSeleccionarEntradasClick(ActionEvent event) {
        try {
            Usuario usuario = MyApplication.getUsuarioLogueado();
            String basePath = (usuario instanceof Administrador) ? "administrador/" : "cliente/";
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/" + basePath + "SeleccionEntradasView.fxml"));
            Parent root = loader.load();
            
            SeleccionEntradasController controller = loader.getController();
            controller.setEvento(this.evento);

            Stage stage = (Stage) lblNombreEvento.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Seleccionar Entradas - " + evento.getNombre());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        ((Stage) lblNombreEvento.getScene().getWindow()).close();
    }
}