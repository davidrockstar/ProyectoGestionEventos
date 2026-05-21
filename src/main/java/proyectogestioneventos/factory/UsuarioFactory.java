package proyectogestioneventos.factory;

import proyectogestioneventos.model.Administrador;
import proyectogestioneventos.model.Usuario;
import proyectogestioneventos.model.enums.Rol;

public class UsuarioFactory {

    public Usuario crearUsuario(String id, String nombre, String email, String telefono, String contrasena, Rol rol) {
        switch (rol) {
            case ADMINISTRADOR:
                return new Administrador(id, nombre, email, telefono, contrasena);
            case CLIENTE:
                return new Usuario(id, nombre, email, telefono, contrasena);
            default:
                throw new IllegalArgumentException("Rol de usuario no válido: " + rol);
        }
    }
}
