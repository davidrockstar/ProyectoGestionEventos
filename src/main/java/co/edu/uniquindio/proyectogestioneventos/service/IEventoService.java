package co.edu.uniquindio.proyectogestioneventos.service;

import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IEventoService {
    /**
     * RF-003: Obtiene todos los eventos disponibles.
     */
    List<Evento> listarEventosDisponibles();

    /**
     * RF-003: Filtra eventos según los criterios proporcionados.
     */
    List<Evento> filtrarEventos(LocalDate fecha, String ciudad, String categoria, Double precioMax);

    /**
     * RF-004: Obtiene el detalle completo de un evento por su ID.
     */
    Optional<Evento> obtenerDetalleEvento(String idEvento);
}
