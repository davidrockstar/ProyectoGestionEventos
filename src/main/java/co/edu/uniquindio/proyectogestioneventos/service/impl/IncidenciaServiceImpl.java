package co.edu.uniquindio.proyectogestioneventos.service.impl;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoIncidencia;
import co.edu.uniquindio.proyectogestioneventos.model.enums.TipoIncidencia;
import co.edu.uniquindio.proyectogestioneventos.service.IIncidenciaService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class IncidenciaServiceImpl implements IIncidenciaService {

    @Override
    public List<Incidencia> listarIncidencias() {
        return Taquilla.getInstance().getIncidencias();
    }

    @Override
    public void registrarIncidencia(TipoIncidencia tipo, String descripcion, Usuario usuario, Evento evento) throws Exception {
        if (descripcion == null || descripcion.trim().isEmpty()) throw new Exception("La descripción es obligatoria.");
        if (usuario == null) throw new Exception("Debe seleccionar un usuario.");
        if (evento == null) throw new Exception("Debe seleccionar un evento.");

        Incidencia nueva = new Incidencia(
            "INC-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase(),
            tipo,
            descripcion,
            LocalDateTime.now(),
            usuario,
            evento,
            EstadoIncidencia.PENDIENTE
        );
        Taquilla.getInstance().getIncidencias().add(nueva);
    }

    @Override
    public void generarDatosPrueba() {
        if (!listarIncidencias().isEmpty()) return;
        Taquilla t = Taquilla.getInstance();
        if (t.getUsuarios().isEmpty() || t.getEventos().isEmpty()) return;

        try {
            // 1. Una incidencia Pendiente
            registrarIncidencia(TipoIncidencia.ERROR_PAGO, "Fallo en transacción Stripe", t.getUsuarios().get(1), t.getEventos().get(0));
            
            // 2. Una incidencia que nacerá Resuelta
            registrarIncidencia(TipoIncidencia.DOBLE_RESERVA, "Doble asignación silla A1", t.getUsuarios().get(0), t.getEventos().get(1));
            cambiarEstado(listarIncidencias().get(1).getIdIncidencia(), EstadoIncidencia.RESUELTA);
            
        } catch (Exception ignored) {}
    }

    @Override
    public void cambiarEstado(String idIncidencia, EstadoIncidencia nuevoEstado) throws Exception {
        Incidencia inc = listarIncidencias().stream()
                .filter(i -> i.getIdIncidencia().equals(idIncidencia))
                .findFirst()
                .orElseThrow(() -> new Exception("Incidencia no encontrada."));

        // Validaciones de transición (RF-017 / Objetivo Funcional)
        if (nuevoEstado == EstadoIncidencia.RESUELTA && inc.getEstado() != EstadoIncidencia.PENDIENTE) {
            throw new Exception("Solo se pueden resolver incidencias que estén PENDIENTES.");
        }
        if (nuevoEstado == EstadoIncidencia.CERRADA && inc.getEstado() != EstadoIncidencia.RESUELTA) {
            throw new Exception("Solo se pueden cerrar incidencias que ya estén RESUELTAS.");
        }

        inc.setEstado(nuevoEstado);
    }
}