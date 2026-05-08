package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.model.enums.Rol;
import co.edu.uniquindio.proyectogestioneventos.service.IUsuarioService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.UsuarioServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.Optional;

public class GestionUsuariosViewController {

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colID;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colTelefono;
    @FXML private TableColumn<Usuario, String> colRol;

    @FXML
    private void initialize() {
        configurarTabla();
        actualizarTabla();
    }

    private void configurarTabla() {
        colID.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        
        // El rol es un método en el modelo, usamos lambda para extraer el String
        colRol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getRol().toString())
        );
    }

    private void actualizarTabla() {
        tablaUsuarios.setItems(FXCollections.observableArrayList(usuarioService.listarUsuarios()));
    }

    @FXML
    void onCreateUsuarioClick(ActionEvent event) {
        mostrarDialogoUsuario(null);
    }

    @FXML
    void onEditUsuarioClick(ActionEvent event) {
        Usuario usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            mostrarDialogoUsuario(usuarioSeleccionado);
        } else {
            mostrarAlerta("Advertencia", "Editar Usuario", "Por favor, seleccione un usuario para editar.", Alert.AlertType.WARNING);
        }
    }

    private void mostrarDialogoUsuario(Usuario usuarioAEditar) {
        boolean esEdicion = (usuarioAEditar != null);
        
        Dialog<Usuario> dialog = new Dialog<>();
        dialog.setTitle(esEdicion ? "Editar Usuario" : "Crear Nuevo Usuario");
        dialog.setHeaderText("Ingrese los datos del usuario");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNombre = new TextField(esEdicion ? usuarioAEditar.getNombre() : "");
        TextField txtEmail = new TextField(esEdicion ? usuarioAEditar.getEmail() : "");
        TextField txtTelefono = new TextField(esEdicion ? usuarioAEditar.getTelefono() : "");
        PasswordField txtPassword = new PasswordField();
        ComboBox<Rol> comboRol = new ComboBox<>(FXCollections.observableArrayList(Rol.values()));
        
        if(esEdicion) {
            comboRol.setValue(usuarioAEditar.getRol());
            txtPassword.setPromptText("Dejar vacío para no cambiar");
        } else {
            comboRol.setValue(Rol.CLIENTE);
        }

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(txtEmail, 1, 1);
        grid.add(new Label("Teléfono:"), 0, 2);
        grid.add(txtTelefono, 1, 2);
        grid.add(new Label("Contraseña:"), 0, 3);
        grid.add(txtPassword, 1, 3);
        grid.add(new Label("Rol:"), 0, 4);
        grid.add(comboRol, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                try {
                    // Validaciones básicas
                    if(txtNombre.getText().isEmpty() || txtEmail.getText().isEmpty() || txtTelefono.getText().isEmpty()) {
                        throw new Exception("Nombre, Email y Teléfono son obligatorios.");
                    }

                    if (esEdicion) {
                        return usuarioService.actualizarPerfil(
                            usuarioAEditar.getIdUsuario(),
                            txtNombre.getText(),
                            txtEmail.getText(),
                            usuarioAEditar.getContrasena(), // Se requiere la actual para validar segun service
                            txtPassword.getText().isEmpty() ? null : txtPassword.getText()
                        );
                    } else {
                        if(txtPassword.getText().isEmpty()) throw new Exception("La contraseña es obligatoria.");
                        return usuarioService.registrarUsuario(
                            txtNombre.getText(),
                            txtEmail.getText(),
                            txtTelefono.getText(),
                            txtPassword.getText(),
                            comboRol.getValue()
                        );
                    }
                } catch (Exception e) {
                    mostrarAlerta("Error", "Error al procesar", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<Usuario> result = dialog.showAndWait();
        result.ifPresent(u -> actualizarTabla());
    }

    @FXML
    void onDeleteUsuarioClick(ActionEvent event) {
        Usuario usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            
            // Validación de seguridad: No eliminarse a sí mismo
            Usuario logueado = MyApplication.getUsuarioLogueado();
            if (logueado != null && logueado.getIdUsuario().equals(usuarioSeleccionado.getIdUsuario())) {
                mostrarAlerta("Error", "Acción no permitida", "No puedes eliminar tu propia cuenta de administrador.", Alert.AlertType.ERROR);
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar Eliminación");
            confirm.setContentText("¿Está seguro de eliminar al usuario: " + usuarioSeleccionado.getNombre() + "?");
            
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    usuarioService.eliminarUsuario(usuarioSeleccionado.getIdUsuario());
                    actualizarTabla();
                } catch (Exception e) {
                    mostrarAlerta("Error", "No se pudo eliminar", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
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

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        mostrarAlerta(titulo, null, contenido, tipo);
    }
}
