package co.edu.uniquindio.proyectogestioneventos.service.impl;

import co.edu.uniquindio.proyectogestioneventos.factory.UsuarioFactory;
import co.edu.uniquindio.proyectogestioneventos.model.Taquilla;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.model.enums.Rol;
import co.edu.uniquindio.proyectogestioneventos.service.IUsuarioService;

import java.util.Optional;
import java.util.UUID;

public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioFactory usuarioFactory = new UsuarioFactory();

    @Override
    public Usuario registrarUsuario(String nombre, String email, String telefono, String contrasena, Rol rol) throws Exception {
        if (Taquilla.getInstance().getUsuarios().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email))) {
            throw new Exception("El email ya está registrado.");
        }
        // El ID se puede generar automáticamente para asegurar unicidad
        String idUsuario = UUID.randomUUID().toString().substring(0, 8);
        Usuario nuevoUsuario = usuarioFactory.crearUsuario(idUsuario, nombre, email, telefono, contrasena, rol);
        Taquilla.getInstance().agregarUsuario(nuevoUsuario);
        return nuevoUsuario;
    }

    @Override
    public Optional<Usuario> autenticarUsuario(String email, String contrasena) {
        return Taquilla.getInstance().validarUsuario(email, contrasena);
    }

    @Override
    public Usuario actualizarPerfil(String idUsuario, String nuevoNombre, String nuevoEmail, String contrasenaActual, String nuevaContrasena) throws Exception {
        Usuario usuario = Taquilla.getInstance().getUsuarios().stream()
                .filter(u -> u.getIdUsuario().equals(idUsuario))
                .findFirst()
                .orElseThrow(() -> new Exception("Usuario no encontrado."));

        if (!usuario.getContrasena().equals(contrasenaActual)) {
            throw new Exception("La contraseña actual es incorrecta.");
        }

        // Validar que el nuevo email no esté ya en uso por otro usuario
        if (nuevoEmail != null && !nuevoEmail.isEmpty() && !nuevoEmail.equalsIgnoreCase(usuario.getEmail())) {
            if (Taquilla.getInstance().getUsuarios().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(nuevoEmail))) {
                throw new Exception("El nuevo email ya está registrado por otro usuario.");
            }
            usuario.setEmail(nuevoEmail);
        }

        if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
            usuario.setNombre(nuevoNombre);
        }

        if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
            usuario.setContrasena(nuevaContrasena);
        }

        return usuario;
    }
}
