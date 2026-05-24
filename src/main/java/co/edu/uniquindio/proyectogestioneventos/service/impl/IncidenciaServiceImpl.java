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

        Long idIncidencia = (long) (Taquilla.getInstance().getIncidencias().size() + 1); // Generar ID Long
        Incidencia nueva = new Incidencia(
            idIncidencia, // Nuevo Long ID
            tipo,
            descripcion,
            LocalDateTime.now(), // Usar fechaReporte
            EstadoIncidencia.ABIERTA, // Estado inicial ABIERTA
            usuario, // Usar reportante
            evento,
            null // Compra relacionada (opcional, por ahora null)
        );
        Taquilla.getInstance().getIncidencias().add(nueva);
    }

    @Override
    public void generarDatosPrueba() {
        if (!listarIncidencias().isEmpty()) return;
        Taquilla t = Taquilla.getInstance();
        if (t.getUsuarios().isEmpty() || t.getEventos().isEmpty()) return;

        try {
            // 1. Una incidencia Abierta
            Long idIncidencia1 = (long) (t.getIncidencias().size() + 1);
            Incidencia inc1 = new Incidencia(idIncidencia1, TipoIncidencia.ERROR_PAGO, "Fallo en transacción Stripe", LocalDateTime.now(), EstadoIncidencia.ABIERTA, t.getUsuarios().get(1), t.getEventos().get(0), null);
            t.getIncidencias().add(inc1);
            
            // 2. Una incidencia que nacerá Resuelta
            Long idIncidencia2 = (long) (t.getIncidencias().size() + 1);
            Incidencia inc2 = new Incidencia(idIncidencia2, TipoIncidencia.DOBLE_RESERVA, "Doble asignación silla A1", LocalDateTime.now(), EstadoIncidencia.ABIERTA, t.getUsuarios().get(0), t.getEventos().get(1), null);
            t.getIncidencias().add(inc2);
            cambiarEstado(inc2.getIdIncidencia(), EstadoIncidencia.RESUELTA);
            
        } catch (Exception ignored) {}
    }

    @Override
    public void cambiarEstado(Long idIncidencia, EstadoIncidencia nuevoEstado) throws Exception {
        Incidencia inc = listarIncidencias().stream()
                .filter(i -> i.getIdIncidencia().equals(idIncidencia)) 
                .findFirst()
                .orElseThrow(() -> new Exception("Incidencia no encontrada."));

        // Validaciones de transición (RF-017 / Objetivo Funcional)
        if (nuevoEstado == EstadoIncidencia.RESUELTA && (inc.getEstado() == EstadoIncidencia.CERRADA)) {
            throw new Exception("No se puede resolver una incidencia que ya está CERRADA.");
        }
        if (nuevoEstado == EstadoIncidencia.CERRADA && inc.getEstado() != EstadoIncidencia.RESUELTA) {
            throw new Exception("Solo se pueden cerrar incidencias que ya estén RESUELTAS.");
        }

        inc.setEstado(nuevoEstado);
    }
}