package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.service.IUsuarioService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.UsuarioServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PerfilController {

    @FXML private TextField txtNombre, txtEmail, txtTelefono;
    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    @FXML
    public void initialize() {
        Usuario logueado = MyApplication.getUsuarioLogueado();
        if (logueado != null) {
            txtNombre.setText(logueado.getNombre());
            txtEmail.setText(logueado.getEmail());
            txtTelefono.setText(logueado.getTelefono());
        }
    }

    @FXML
    void onGuardarCambios() {
        try {
            Usuario logueado = MyApplication.getUsuarioLogueado();
            usuarioService.actualizarPerfil(
                logueado.getIdUsuario(),
                txtNombre.getText(),
                txtEmail.getText(),
                logueado.getContrasena(), // Contraseña actual para validar
                null // No cambia contraseña en este flujo simple
            );
            System.out.println("Perfil actualizado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}