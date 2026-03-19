package co.edu.uniquindio.proyectogestioneventos.datautil;

import co.edu.uniquindio.proyectogestioneventos.model.Administrador;
import co.edu.uniquindio.proyectogestioneventos.model.Taquilla;
import co.edu.uniquindio.proyectogestioneventos.model.Usuario;

public class DataUtil {

    public static Taquilla inicializarDatos() {
        Taquilla taquilla = Taquilla.getInstance();

        // Crear un administrador con datos de ejemplo
        Administrador admin = new Administrador(
                "admin",
                "Admin Principal",
                "admin@eventos.com",
                "1234567890",
                "123" // contraseña
        );
        taquilla.agregarUsuario(admin);

        // Crear un cliente (usuario normal) con datos de ejemplo
        Usuario cliente = new Usuario(
                "cliente1",
                "Juan",
                "juan@email.com",
                "0987654321",
                "123"
        );
        taquilla.agregarUsuario(cliente);

        return taquilla;
    }
}
