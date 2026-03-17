package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.service.IUsuarioService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.UsuarioServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class PerfilUsuarioController {

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();
    private Usuario usuarioActual;

    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoEmail;
    @FXML
    private TextField campoTelefono;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        campoNombre.setText(usuario.getNombre());
        campoEmail.setText(usuario.getEmail());
        campoTelefono.setText(usuario.getTelefono());
    }

    @FXML
    private void onGuardarCambios() {
        try {
            usuarioService.actualizarPerfil(
                    usuarioActual.getIdUsuario(),
                    campoNombre.getText(),
                    campoEmail.getText(),
                    campoTelefono.getText()
            );
            // Mostrar alerta de éxito
            System.out.println("Perfil actualizado con éxito.");
        } catch (Exception e) {
            // Mostrar alerta de error
            e.printStackTrace();
        }
    }

    @FXML
    private void onCerrarClick(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
