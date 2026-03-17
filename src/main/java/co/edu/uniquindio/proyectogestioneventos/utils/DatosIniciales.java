package co.edu.uniquindio.proyectogestioneventos.utils;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import java.time.LocalDateTime;

public class DatosIniciales {
    public static void cargar(Almacenamiento almacenamiento) {
        // RF-001: Crear usuarios de prueba
        Usuario admin = new Administrador("admin01", "Admin", "admin@eventos.com", "111", "admin123");
        Usuario cliente1 = new Cliente("user01", "Ana", "ana@email.com", "222", "user123");
        almacenamiento.usuarios.add(admin);
        almacenamiento.usuarios.add(cliente1);

        // Crear recintos, zonas y asientos
        Recinto teatro = new Recinto("R01", "Teatro Mayor", "Calle 1", "Bogotá");
        Zona vip = new Zona("Z01", "VIP", 50, 250000);
        Zona general = new Zona("Z02", "General", 200, 100000);
        teatro.getListaZonas().add(vip);
        teatro.getListaZonas().add(general);

        // RF-003: Crear eventos de prueba
        Evento concierto = new Evento("E01", "Concierto de Rock", "Música", "Gran concierto de rock con bandas nacionales.", "Bogotá", LocalDateTime.now().plusDays(30), teatro);
        Evento obra = new Evento("E02", "Obra de Teatro Clásico", "Teatro", "Una obra maestra de la literatura universal.", "Medellín", LocalDateTime.now().plusDays(45), teatro);
        almacenamiento.eventos.add(concierto);
        almacenamiento.eventos.add(obra);

        // RF-009: Crear servicios adicionales
        almacenamiento.servicios.add(new ServicioAdicional("S01", "Acceso VIP", 50000));
        almacenamiento.servicios.add(new ServicioAdicional("S02", "Seguro de Cancelación", 20000));
        almacenamiento.servicios.add(new ServicioAdicional("S03", "Merchandising Oficial", 80000));
    }
}
