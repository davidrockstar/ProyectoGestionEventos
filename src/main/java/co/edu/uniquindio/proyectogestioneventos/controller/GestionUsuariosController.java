package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.model.enums.Rol;
import co.edu.uniquindio.proyectogestioneventos.service.IUsuarioService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.UsuarioServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class GestionUsuariosController {

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colID, colNombre, colEmail, colRol;
    @FXML private TextField txtNombre, txtEmail, txtTelefono, txtPassword;
    @FXML private ComboBox<Rol> comboRol;

    @FXML
    public void initialize() {
        colID.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRol().toString()));

        comboRol.setItems(FXCollections.observableArrayList(Rol.values()));
        actualizarTabla();
    }

    private void actualizarTabla() {
        tablaUsuarios.getItems().setAll(usuarioService.listarUsuarios());
    }

    @FXML
    void onCrearUsuario() {
        try {
            usuarioService.registrarUsuario(
                txtNombre.getText(), txtEmail.getText(), txtTelefono.getText(), 
                txtPassword.getText(), comboRol.getValue()
            );
            actualizarTabla();
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    @FXML
    void onEliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                usuarioService.eliminarUsuario(seleccionado.getIdUsuario());
                actualizarTabla();
            } catch (Exception e) {
                mostrarAlerta("Error", e.getMessage());
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtEmail.clear();
        txtTelefono.clear();
        txtPassword.clear();
        comboRol.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}