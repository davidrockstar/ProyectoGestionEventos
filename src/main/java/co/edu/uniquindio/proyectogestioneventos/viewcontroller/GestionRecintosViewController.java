package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.model.Recinto;
import co.edu.uniquindio.proyectogestioneventos.service.IRecintoService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.RecintoServiceImpl;
import co.edu.uniquindio.proyectogestioneventos.utils.DatosIniciales;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class GestionRecintosViewController {

    @FXML
    private TableView<Recinto> tablaRecintos;
    @FXML private TableColumn<Recinto, String> colIdRecinto;
    @FXML private TableColumn<Recinto, String> colNombre;
    @FXML private TableColumn<Recinto, String> colDireccion;
    @FXML private TableColumn<Recinto, String> colCiudad;

    private final IRecintoService recintoService = new RecintoServiceImpl();

    @FXML
    private void initialize() {
        configurarTabla();
        actualizarTabla();
    }

    private void configurarTabla() {
        colIdRecinto.setCellValueFactory(new PropertyValueFactory<>("idRecinto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
    }

    private void actualizarTabla() {
        if (recintoService.listarRecintos().isEmpty()) {
            DatosIniciales.cargar(); // Carga automática si está vacío
        }
        tablaRecintos.setItems(FXCollections.observableArrayList(recintoService.listarRecintos()));
    }

    @FXML
    void onCreateRecintoClick(ActionEvent event) {
        mostrarDialogoRecinto(null);
    }

    @FXML
    void onEditRecintoClick(ActionEvent event) {
        Recinto recintoSeleccionado = tablaRecintos.getSelectionModel().getSelectedItem();
        if (recintoSeleccionado != null) {
            mostrarDialogoRecinto(recintoSeleccionado);
        } else {
            mostrarAlerta("Advertencia", "Editar Recinto", "Por favor, seleccione un recinto para editar.", Alert.AlertType.WARNING);
        }
    }

    private void mostrarDialogoRecinto(Recinto recintoAEditar) {
        boolean esEdicion = (recintoAEditar != null);
        Dialog<Recinto> dialog = new Dialog<>();
        dialog.setTitle(esEdicion ? "Editar Recinto" : "Crear Recinto");
        
        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNombre = new TextField(esEdicion ? recintoAEditar.getNombre() : "");
        TextField txtDireccion = new TextField(esEdicion ? recintoAEditar.getDireccion() : "");
        TextField txtCiudad = new TextField(esEdicion ? recintoAEditar.getCiudad() : "");

        grid.add(new Label("Nombre:"), 0, 0); grid.add(txtNombre, 1, 0);
        grid.add(new Label("Dirección:"), 0, 1); grid.add(txtDireccion, 1, 1);
        grid.add(new Label("Ciudad:"), 0, 2); grid.add(txtCiudad, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                try {
                    if (esEdicion) {
                        return recintoService.actualizarRecinto(recintoAEditar.getIdRecinto(), txtNombre.getText(), txtDireccion.getText(), txtCiudad.getText());
                    } else {
                        return recintoService.crearRecinto(txtNombre.getText(), txtDireccion.getText(), txtCiudad.getText());
                    }
                } catch (Exception e) {
                    mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<Recinto> result = dialog.showAndWait();
        result.ifPresent(r -> actualizarTabla());
    }

    @FXML
    void onDeleteRecintoClick(ActionEvent event) {
        Recinto recintoSeleccionado = tablaRecintos.getSelectionModel().getSelectedItem();
        if (recintoSeleccionado != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de eliminar este recinto?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        recintoService.eliminarRecinto(recintoSeleccionado.getIdRecinto());
                        actualizarTabla();
                    } catch (Exception e) {
                        mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            });
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
                controller.setRecinto(recintoSeleccionado); 

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

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        mostrarAlerta(titulo, null, contenido, tipo);
    }
}
