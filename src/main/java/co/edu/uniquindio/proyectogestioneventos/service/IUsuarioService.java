package co.edu.uniquindio.proyectogestioneventos.service;

import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import java.util.Optional;

public interface IUsuarioService {
    /**
     * RF-001: Registra un nuevo usuario en el sistema.
     */
    Usuario registrarUsuario(String id, String nombre, String email, String telefono, String contrasena) throws Exception;

    /**
     * RF-001: Autentica un usuario por su email y contraseña.
     */
    Optional<Usuario> autenticarUsuario(String email, String contrasena);

    /**
     * RF-002: Actualiza el perfil de un usuario existente.
     */
    Usuario actualizarPerfil(String idUsuario, String nuevoNombre, String nuevoEmail, String nuevoTelefono) throws Exception;
}
