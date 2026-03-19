package co.edu.uniquindio.proyectogestioneventos.service.impl;

import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import co.edu.uniquindio.proyectogestioneventos.model.Taquilla;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEvento;
import co.edu.uniquindio.proyectogestioneventos.service.IEventoService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventoServiceImpl implements IEventoService {

    @Override
    public List<Evento> listarEventosDisponibles() {
        return Taquilla.getInstance().getEventos().stream()
                .filter(e -> e.getEstado() == EstadoEvento.PUBLICADO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Evento> filtrarEventos(LocalDate fecha, String ciudad, String categoria, Double precioMax) {
        Stream<Evento> stream = listarEventosDisponibles().stream();

        if (fecha != null) {
            stream = stream.filter(e -> e.getFechaHora().toLocalDate().equals(fecha));
        }
        if (ciudad != null && !ciudad.isEmpty()) {
            stream = stream.filter(e -> e.getCiudad().equalsIgnoreCase(ciudad));
        }
        if (categoria != null && !categoria.isEmpty()) {
            stream = stream.filter(e -> e.getCategoria().equalsIgnoreCase(categoria));
        }
        if (precioMax != null) {
            stream = stream.filter(evento -> evento.getRecinto().getListaZonas().stream()
                    .anyMatch(zona -> zona.getPrecioBase() <= precioMax));
        }

        return stream.collect(Collectors.toList());
    }

    @Override
    public Optional<Evento> obtenerDetalleEvento(String idEvento) {
        return Taquilla.getInstance().getEventos().stream()
                .filter(e -> e.getIdEvento().equals(idEvento))
                .findFirst();
    }
}
