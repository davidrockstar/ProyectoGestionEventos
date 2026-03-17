package co.edu.uniquindio.proyectogestioneventos.service.impl;

import co.edu.uniquindio.proyectogestioneventos.model.Cliente;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.service.IUsuarioService;
import co.edu.uniquindio.proyectogestioneventos.utils.Almacenamiento;
import java.util.Optional;

public class UsuarioServiceImpl implements IUsuarioService {
    private final Almacenamiento almacenamiento = Almacenamiento.getInstance();

    @Override
    public Usuario registrarUsuario(String id, String nombre, String email, String telefono, String contrasena) throws Exception {
        if (almacenamiento.usuarios.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email))) {
            throw new Exception("El email ya está registrado.");
        }
        Usuario nuevoUsuario = new Cliente(id, nombre, email, telefono, contrasena);
        almacenamiento.usuarios.add(nuevoUsuario);
        return nuevoUsuario;
    }

    @Override
    public Optional<Usuario> autenticarUsuario(String email, String contrasena) {
        return almacenamiento.usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getContrasena().equals(contrasena))
                .findFirst();
    }

    @Override
    public Usuario actualizarPerfil(String idUsuario, String nuevoNombre, String nuevoEmail, String nuevoTelefono) throws Exception {
        Usuario usuario = almacenamiento.usuarios.stream()
                .filter(u -> u.getIdUsuario().equals(idUsuario))
                .findFirst()
                .orElseThrow(() -> new Exception("Usuario no encontrado."));

        usuario.setNombre(nuevoNombre);
        usuario.setEmail(nuevoEmail);
        usuario.setTelefono(nuevoTelefono);
        return usuario;
    }
}
