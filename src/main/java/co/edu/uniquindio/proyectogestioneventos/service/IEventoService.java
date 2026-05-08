package co.edu.uniquindio.proyectogestioneventos.service;

import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import co.edu.uniquindio.proyectogestioneventos.model.Recinto;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEvento;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    /**
     * Crea un nuevo evento en el sistema.
     */
    Evento crearEvento(String nombre, String categoria, String descripcion, String ciudad, LocalDateTime fechaHora, Recinto recinto) throws Exception;

    /**
     * Actualiza la información de un evento existente.
     */
    Evento actualizarEvento(String idEvento, String nombre, String categoria, String descripcion, String ciudad, LocalDateTime fechaHora, Recinto recinto) throws Exception;

    /**
     * Elimina un evento del sistema.
     */
    void eliminarEvento(String idEvento) throws Exception;

    /**
     * Cambia el estado de un evento.
     */
    void cambiarEstadoEvento(String idEvento, EstadoEvento nuevoEstado) throws Exception;

    /**
     * Obtiene la lista de todos los eventos, incluyendo los no publicados (para administración).
     */
    List<Evento> listarTodosEventos();
}
