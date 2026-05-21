package proyectogestioneventos.model;

import proyectogestioneventos.model.enums.Rol;

public class Administrador extends Usuario {
    public Administrador(String idUsuario, String nombre, String email, String telefono, String contrasena) {
        super(idUsuario, nombre, email, telefono, contrasena);
    }

    @Override
    public Rol getRol() {
        return Rol.ADMINISTRADOR;
    }
}
