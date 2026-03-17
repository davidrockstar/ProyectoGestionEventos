package co.edu.uniquindio.proyectogestioneventos.datautil;

import co.edu.uniquindio.proyectogestioneventos.model.Administrador;
import co.edu.uniquindio.proyectogestioneventos.model.Cliente;
import co.edu.uniquindio.proyectogestioneventos.model.Taquilla;

public class DataUtil {

    public static Taquilla inicializarDatos() {
        Taquilla taquilla = Taquilla.getInstance();

        // Crear un administrador con datos de ejemplo
        Administrador admin = new Administrador(
                "admin",
                "Admin Principal",
                "admin@eventos.com", // Corregido
                "1234567890",
                "admin" // contraseña
        );
        taquilla.agregarUsuario(admin);

        // Crear un cliente con datos de ejemplo
        Cliente cliente = new Cliente(
                "cliente1",
                "Ana", // Corregido
                "ana@email.com", // Corregido
                "0987654321",
                "user123" // Corregido
        );
        taquilla.agregarUsuario(cliente);

        return taquilla;
    }
}
