package co.edu.uniquindio.proyectogestioneventos.model;

import java.util.ArrayList;
import java.util.List;

public class EventoUQ {
    private List<Usuario> usuarios = new ArrayList<>();
    private static EventoUQ instance;

    public EventoUQ() {
    }

    public static EventoUQ getInstance() {
        if (instance == null) {
            instance = new EventoUQ();
        }
        return instance;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Usuario buscarUsuario(String id, String contrasena) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id) && usuario.getContrasena().equals(contrasena)) {
                return usuario;
            }
        }
        return null;
    }
}
