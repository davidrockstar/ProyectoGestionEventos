package co.edu.uniquindio.proyectogestioneventos.utils;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEvento;
import java.time.LocalDateTime;

public class DatosIniciales {
    public static void cargar() {
        Taquilla taquilla = Taquilla.getInstance();

        // Evitar duplicar datos si ya existen en memoria
        if (!taquilla.getRecintos().isEmpty()) return;

        // 1. Crear recintos solicitados
        Recinto coliseo = new Recinto("R01", "Coliseo Central", "Calle 10", "Armenia");
        Recinto teatro = new Recinto("R02", "Teatro Municipal", "Carrera 15", "Pereira");
        Recinto arena = new Recinto("R03", "Arena Norte", "Avenida Bolivar", "Manizales");

        // Agregar zona básica a cada recinto
        Zona zonaGral = new Zona("Z01", "General", 500, 50000, new java.util.ArrayList<>());
        coliseo.agregarZona(zonaGral);
        teatro.agregarZona(zonaGral);
        arena.agregarZona(zonaGral);

        taquilla.getRecintos().add(coliseo);
        taquilla.getRecintos().add(teatro);
        taquilla.getRecintos().add(arena);

        // 2. Crear eventos de prueba solicitados
        Evento concierto = new Evento("E01", "Concierto Urbano", "Música", "Show de reggaeton.", "Armenia", 
                LocalDateTime.now().plusDays(10), EstadoEvento.PUBLICADO, coliseo);
        
        Evento festival = new Evento("E02", "Festival Rock", "Música", "Bandas de rock locales.", "Pereira", 
                LocalDateTime.now().plusDays(20), EstadoEvento.BORRADOR, teatro);
        
        Evento feria = new Evento("E03", "Feria Gamer", "Tecnología", "Torneos y stands.", "Manizales", 
                LocalDateTime.now().plusDays(30), EstadoEvento.PAUSADO, arena);

        taquilla.getEventos().add(concierto);
        taquilla.getEventos().add(festival);
        taquilla.getEventos().add(feria);
    }
}
