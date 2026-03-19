package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class GestionUsuariosViewController {

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private void initialize() {
        // Aquí se inicializarían las columnas de la tabla y se cargarían los usuarios
        // Por ahora, solo un placeholder
        System.out.println("GestionUsuariosView inicializada.");
    }

    @FXML
    void onCreateUsuarioClick(ActionEvent event) {
        mostrarAlerta("Información", "Crear Usuario", "Funcionalidad para crear usuario no implementada.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onEditUsuarioClick(ActionEvent event) {
        Usuario usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            mostrarAlerta("Información", "Editar Usuario", "Funcionalidad para editar usuario no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Editar Usuario", "Por favor, seleccione un usuario para editar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onDeleteUsuarioClick(ActionEvent event) {
        Usuario usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            mostrarAlerta("Información", "Eliminar Usuario", "Funcionalidad para eliminar usuario no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Eliminar Usuario", "Por favor, seleccione un usuario para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) tablaUsuarios.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
