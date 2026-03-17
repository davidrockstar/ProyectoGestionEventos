package co.edu.uniquindio.proyectogestioneventos.service.impl;

import co.edu.uniquindio.proyectogestioneventos.model.Evento;
import co.edu.uniquindio.proyectogestioneventos.service.IEventoService;
import co.edu.uniquindio.proyectogestioneventos.utils.Almacenamiento;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventoServiceImpl implements IEventoService {
    private final Almacenamiento almacenamiento = Almacenamiento.getInstance();

    @Override
    public List<Evento> listarEventosDisponibles() {
        return almacenamiento.eventos;
    }

    @Override
    public List<Evento> filtrarEventos(LocalDate fecha, String ciudad, String categoria, Double precioMax) {
        Stream<Evento> stream = almacenamiento.eventos.stream();

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
        return almacenamiento.eventos.stream().filter(e -> e.getIdEvento().equals(idEvento)).findFirst();
    }
}
