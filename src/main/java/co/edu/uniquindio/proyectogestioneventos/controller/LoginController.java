package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.Administrador;
import co.edu.uniquindio.proyectogestioneventos.model.Cliente;
import co.edu.uniquindio.proyectogestioneventos.model.Taquilla;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;

import java.util.Optional;

public class LoginController {

    private final Taquilla taquilla;

    public LoginController() {
        this.taquilla = Taquilla.getInstance();
    }

    public String procesarLogin(String id, String contrasena) {
        if (id == null || id.isEmpty()) {
            return "ERROR_USUARIO_VACIO";
        }
        if (contrasena == null || contrasena.isEmpty()) {
            return "ERROR_PASSWORD_VACIO";
        }

        Optional<Usuario> usuarioOptional = taquilla.validarUsuario(id, contrasena);

        if (!usuarioOptional.isPresent()) {
            return "ERROR_CREDENCIALES";
        }

        Usuario usuario = usuarioOptional.get();

        if (usuario instanceof Administrador) {
            return "ADMINISTRADOR";
        }

        if (usuario instanceof Cliente) {
            return "CLIENTE";
        }

        return "ERROR_TIPO_USUARIO_DESCONOCIDO";
    }
}
