package co.edu.uniquindio.proyectogestioneventos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Taquilla implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<Usuario> usuarios;
    private static Taquilla instance;

    private Taquilla() {
        this.usuarios = new ArrayList<>();
    }

    public static Taquilla getInstance() {
        if (instance == null) {
            instance = new Taquilla();
        }
        return instance;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void agregarUsuario(Usuario usuario) {
        if (usuario != null) {
            this.usuarios.add(usuario);
        }
    }

    /**
     * Valida las credenciales de un usuario.
     * @param idUsuario El ID del usuario.
     * @param contrasena La contraseña a verificar.
     * @return Un Optional que contiene el Usuario si las credenciales son correctas, o un Optional vacío si no lo son.
     */
    public Optional<Usuario> validarUsuario(String idUsuario, String contrasena) {
        return usuarios.stream()
                .filter(u -> u.getIdUsuario().equals(idUsuario) && u.getContrasena().equals(contrasena))
                .findFirst();
    }
}
