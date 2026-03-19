package co.edu.uniquindio.proyectogestioneventos.factory;

import co.edu.uniquindio.proyectogestioneventos.model.Administrador;
import co.edu.uniquindio.proyectogestioneventos.model.Cliente;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.model.enums.Rol;

public class UsuarioFactory {

    public Usuario crearUsuario(String id, String nombre, String email, String telefono, String contrasena, Rol rol) {
        switch (rol) {
            case ADMINISTRADOR:
                return new Administrador(id, nombre, email, telefono, contrasena);
            case CLIENTE:
                return new Cliente(id, nombre, email, telefono, contrasena);
            default:
                throw new IllegalArgumentException("Rol de usuario no válido: " + rol);
        }
    }
}
