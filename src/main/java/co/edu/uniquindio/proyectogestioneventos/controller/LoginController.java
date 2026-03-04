package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.Administrador;
import co.edu.uniquindio.proyectogestioneventos.model.Cliente;
import co.edu.uniquindio.proyectogestioneventos.model.EventoUQ;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;

public class LoginController {

    private EventoUQ eventoUQ;

    public LoginController() {
        this.eventoUQ = EventoUQ.getInstance();
    }

    public String procesarLogin(String id, String contrasena) {
        if (id == null || id.isEmpty()) {
            return "ERROR_USUARIO_VACIO";
        }
        if (contrasena == null || contrasena.isEmpty()) {
            return "ERROR_PASSWORD_VACIO";
        }

        Usuario usuario = eventoUQ.buscarUsuario(id, contrasena);

        if (usuario == null) {
            return "ERROR_CREDENCIALES";
        }

        if (usuario instanceof Administrador) {
            return "ADMINISTRADOR";
        }

        if (usuario instanceof Cliente) {
            return "CLIENTE";
        }

        return "ERROR_CREDENCIALES";
    }
}
