package co.edu.uniquindio.proyectogestioneventos.utils;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEvento;

import java.time.LocalDateTime;

public class DatosIniciales {
    public static void cargar() { // Ya no recibe Almacenamiento
        Taquilla taquilla = Taquilla.getInstance();

        // RF-001: Crear usuarios de prueba
        Administrador admin = new Administrador("admin01", "Admin", "admin@eventos.com", "111", "admin123");
        Usuario cliente1 = new Usuario("user01", "Ana", "ana@email.com", "222", "user123"); // Instanciar Usuario directamente
        taquilla.getUsuarios().add(admin);
        taquilla.getUsuarios().add(cliente1);

        // Crear recintos, zonas y asientos
        Recinto teatro = new Recinto("R01", "Teatro Mayor", "Calle 1", "Bogotá");
        Zona vip = new Zona("Z01", "VIP", 50, 250000, null); // Asientos se añadirán después
        Zona general = new Zona("Z02", "General", 200, 100000, null); // Asientos se añadirán después
        teatro.getListaZonas().add(vip);
        teatro.getListaZonas().add(general);
        taquilla.getRecintos().add(teatro); // Añadir recinto a Taquilla

        // RF-003: Crear eventos de prueba
        Evento concierto = new Evento("E01", "Concierto de Rock", "Música", "Gran concierto de rock con bandas nacionales.", "Bogotá", LocalDateTime.now().plusDays(30), EstadoEvento.PUBLICADO, teatro); // Añadir EstadoEvento
        Evento obra = new Evento("E02", "Obra de Teatro Clásico", "Teatro", "Una obra maestra de la literatura universal.", "Medellín", LocalDateTime.now().plusDays(45), EstadoEvento.PUBLICADO, teatro); // Añadir EstadoEvento
        taquilla.getEventos().add(concierto);
        taquilla.getEventos().add(obra);

        // RF-009: Crear servicios adicionales
        taquilla.getServicios().add(new ServicioAdicional("S01", "Acceso VIP", 50000));
        taquilla.getServicios().add(new ServicioAdicional("S02", "Seguro de Cancelación", 20000));
        taquilla.getServicios().add(new ServicioAdicional("S03", "Merchandising Oficial", 80000));
    }
}
