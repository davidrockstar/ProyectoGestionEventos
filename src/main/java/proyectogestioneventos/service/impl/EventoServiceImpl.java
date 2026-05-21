package proyectogestioneventos.service.impl;

import proyectogestioneventos.model.Evento;
import proyectogestioneventos.model.Recinto;
import proyectogestioneventos.model.Taquilla;
import proyectogestioneventos.model.enums.EstadoEvento;
import proyectogestioneventos.service.IEventoService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.UUID;
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

    @Override
    public Evento crearEvento(String nombre, String categoria, String descripcion, String ciudad, LocalDateTime fechaHora, Recinto recinto) throws Exception {
        if (nombre == null || nombre.isEmpty()) throw new Exception("El nombre del evento es obligatorio.");
        if (categoria == null || categoria.isEmpty()) throw new Exception("La categoría del evento es obligatoria.");
        if (descripcion == null || descripcion.isEmpty()) throw new Exception("La descripción del evento es obligatoria.");
        if (ciudad == null || ciudad.isEmpty()) throw new Exception("La ciudad del evento es obligatoria.");
        if (fechaHora == null) throw new Exception("La fecha y hora del evento son obligatorias.");
        if (recinto == null) throw new Exception("El recinto del evento es obligatorio.");

        // Validar que no haya otro evento con el mismo nombre en el mismo recinto y fecha
        boolean existe = Taquilla.getInstance().getEventos().stream()
                .anyMatch(e -> e.getNombre().equalsIgnoreCase(nombre) &&
                        e.getRecinto().getIdRecinto().equals(recinto.getIdRecinto()) &&
                        e.getFechaHora().isEqual(fechaHora));
        if (existe) {
            throw new Exception("Ya existe un evento con el mismo nombre en este recinto y fecha.");
        }

        String idEvento = "EV-" + UUID.randomUUID().toString().substring(0, 5);
        Evento nuevoEvento = new Evento(idEvento, nombre, categoria, descripcion, ciudad, fechaHora, EstadoEvento.BORRADOR, recinto);
        Taquilla.getInstance().getEventos().add(nuevoEvento);
        return nuevoEvento;
    }

    @Override
    public Evento actualizarEvento(String idEvento, String nombre, String categoria, String descripcion, String ciudad, LocalDateTime fechaHora, Recinto recinto) throws Exception {
        Evento evento = obtenerDetalleEvento(idEvento).orElseThrow(() -> new Exception("Evento no encontrado."));

        if (nombre == null || nombre.isEmpty()) throw new Exception("El nombre del evento es obligatorio.");
        if (categoria == null || categoria.isEmpty()) throw new Exception("La categoría del evento es obligatoria.");
        if (descripcion == null || descripcion.isEmpty()) throw new Exception("La descripción del evento es obligatoria.");
        if (ciudad == null || ciudad.isEmpty()) throw new Exception("La ciudad del evento es obligatoria.");
        if (fechaHora == null) throw new Exception("La fecha y hora del evento son obligatorias.");
        if (recinto == null) throw new Exception("El recinto del evento es obligatorio.");

        evento.setNombre(nombre);
        evento.setCategoria(categoria);
        evento.setDescripcion(descripcion);
        evento.setCiudad(ciudad);
        evento.setFechaHora(fechaHora);
        evento.setRecinto(recinto);

        return evento;
    }

    @Override
    public void eliminarEvento(String idEvento) throws Exception {
        Evento evento = obtenerDetalleEvento(idEvento).orElseThrow(() -> new Exception("Evento no encontrado."));
        // TODO: Validar si hay compras asociadas antes de eliminar
        Taquilla.getInstance().getEventos().remove(evento);
    }

    @Override
    public void cambiarEstadoEvento(String idEvento, EstadoEvento nuevoEstado) throws Exception {
        Evento evento = obtenerDetalleEvento(idEvento).orElseThrow(() -> new Exception("Evento no encontrado."));
        evento.setEstado(nuevoEstado);
    }

    @Override
    public List<Evento> listarTodosEventos() {
        return Taquilla.getInstance().getEventos();
    }
}
