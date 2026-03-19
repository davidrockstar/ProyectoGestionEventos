package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.model.Recinto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class GestionRecintosViewController {

    @FXML
    private TableView<Recinto> tablaRecintos;

    @FXML
    private void initialize() {
        // Aquí se inicializarían las columnas de la tabla y se cargarían los recintos
        // Por ahora, solo un placeholder
        System.out.println("GestionRecintosView inicializada.");
    }

    @FXML
    void onCreateRecintoClick(ActionEvent event) {
        mostrarAlerta("Información", "Crear Recinto", "Funcionalidad para crear recinto no implementada.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onEditRecintoClick(ActionEvent event) {
        Recinto recintoSeleccionado = tablaRecintos.getSelectionModel().getSelectedItem();
        if (recintoSeleccionado != null) {
            mostrarAlerta("Información", "Editar Recinto", "Funcionalidad para editar recinto no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Editar Recinto", "Por favor, seleccione un recinto para editar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onDeleteRecintoClick(ActionEvent event) {
        Recinto recintoSeleccionado = tablaRecintos.getSelectionModel().getSelectedItem();
        if (recintoSeleccionado != null) {
            mostrarAlerta("Información", "Eliminar Recinto", "Funcionalidad para eliminar recinto no implementada.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Eliminar Recinto", "Por favor, seleccione un recinto para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onAdministrarZonasClick(ActionEvent event) {
        Recinto recintoSeleccionado = tablaRecintos.getSelectionModel().getSelectedItem();
        if (recintoSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/administrador/GestionZonasView.fxml"));
                Parent root = loader.load();
                GestionZonasViewController controller = loader.getController();
                // controller.setRecinto(recintoSeleccionado); // Pasar el recinto a la vista de gestión de zonas

                Stage stage = new Stage();
                stage.setTitle("Gestionar Zonas de " + recintoSeleccionado.getNombre());
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(((Stage)((javafx.scene.control.Button)event.getSource()).getScene().getWindow()));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo cargar la ventana", "Hubo un error al intentar abrir la gestión de zonas.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Advertencia", "Administrar Zonas", "Por favor, seleccione un recinto para administrar sus zonas.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) tablaRecintos.getScene().getWindow();
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
