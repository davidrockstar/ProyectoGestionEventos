package co.edu.uniquindio.proyectogestioneventos.datautil;

import co.edu.uniquindio.proyectogestioneventos.model.Administrador;
import co.edu.uniquindio.proyectogestioneventos.model.Cliente;
import co.edu.uniquindio.proyectogestioneventos.model.EventoUQ;

public class DataUtil {

    public static EventoUQ inicializarDatos() {
        EventoUQ eventoUQ = EventoUQ.getInstance();

        Administrador admin = new Administrador("admin", "Administrador", "admin");
        eventoUQ.agregarUsuario(admin);

        Cliente cliente = new Cliente("cliente", "Cliente", "cliente");
        eventoUQ.agregarUsuario(cliente);

        return eventoUQ;
    }
}
