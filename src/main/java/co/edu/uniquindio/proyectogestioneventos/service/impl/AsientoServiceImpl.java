package co.edu.uniquindio.proyectogestioneventos.service.impl;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoAsiento;
import co.edu.uniquindio.proyectogestioneventos.service.IAsientoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AsientoServiceImpl implements IAsientoService {

    @Override
    public List<Asiento> listarAsientos(String idRecinto, String idZona) {
        return obtenerZona(idRecinto, idZona)
                .map(Zona::getListaAsientos)
                .orElse(new ArrayList<>());
    }
    
    @Override
    public void cambiarEstadoAsiento(String idRecinto, String idZona, String idAsiento, EstadoAsiento nuevoEstado) throws Exception {
        Zona zona = obtenerZona(idRecinto, idZona).orElseThrow(() -> new Exception("Zona no encontrada"));
        
        Asiento asiento = zona.getListaAsientos().stream()
                .filter(a -> a.getIdAsiento().equals(idAsiento))
                .findFirst()
                .orElseThrow(() -> new Exception("Asiento no encontrado"));

        // Validaciones de transición de estados reales
        if (nuevoEstado == EstadoAsiento.BLOQUEADO && 
            (asiento.getEstado() == EstadoAsiento.VENDIDO || asiento.getEstado() == EstadoAsiento.RESERVADO)) {
            throw new Exception("No se puede bloquear un asiento que ya tiene una transacción (Vendido/Reservado).");
        }

        // Validación de Doble Reserva
        if (nuevoEstado == EstadoAsiento.RESERVADO && asiento.getEstado() != EstadoAsiento.DISPONIBLE) {
            throw new Exception("El asiento ya está siendo procesado por otro usuario.");
        }

        asiento.setEstado(nuevoEstado);
        Taquilla.getInstance().incrementMetricsUpdateCounter(); // Notificar cambio para métricas
    }

    @Override
    public void generarDatosPrueba(String idRecinto, String idZona) throws Exception {
        Zona zona = obtenerZona(idRecinto, idZona).orElseThrow(() -> new Exception("Zona no encontrada"));

        if (zona.getListaAsientos() == null) {
            zona.setListaAsientos(new ArrayList<>());
        }
        
        if (!zona.getListaAsientos().isEmpty()) return;

        int capacidad = zona.getCapacidad();
        int asientosPorFila = 10;
        
        for (int i = 0; i < capacidad; i++) {
            char letraFila = (char) ('A' + (i / asientosPorFila));
            String fila = String.valueOf(letraFila);
            String numero = String.valueOf((i % asientosPorFila) + 1);
            String id = idZona + "-" + fila + numero;
            
            EstadoAsiento estado = EstadoAsiento.DISPONIBLE;
            // Simular algunos bloqueados y vendidos
            if (i % 12 == 0) estado = EstadoAsiento.BLOQUEADO;
            if (i % 15 == 0) estado = EstadoAsiento.VENDIDO;

            zona.getListaAsientos().add(new Asiento(id, fila, numero, estado));
        }
    }

    private Optional<Zona> obtenerZona(String idRecinto, String idZona) {
        return Taquilla.getInstance().getRecintos().stream()
                .filter(r -> r.getIdRecinto().equals(idRecinto))
                .findFirst()
                .flatMap(r -> r.getListaZonas().stream()
                        .filter(z -> z.getIdZona().equals(idZona))
                        .findFirst());
    }
}