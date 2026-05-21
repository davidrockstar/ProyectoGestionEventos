package proyectogestioneventos.service.impl;

import proyectogestioneventos.model.*;
import proyectogestioneventos.model.Asiento;
import proyectogestioneventos.model.Taquilla;
import proyectogestioneventos.model.Zona;
import proyectogestioneventos.model.enums.EstadoAsiento;
import proyectogestioneventos.service.IAsientoService;
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
    public void cambiarEstadoAsiento(String idRecinto, String idZona, Long idAsiento, EstadoAsiento nuevoEstado) throws Exception {
        Zona zona = obtenerZona(idRecinto, idZona).orElseThrow(() -> new Exception("Zona no encontrada"));
        
        Asiento asiento = zona.getListaAsientos().stream()
                .filter(a -> a.getIdAsiento().equals(idAsiento) || (a.getCodigo() != null && a.getCodigo().equals(String.valueOf(idAsiento)))) // Buscar por Long ID o por String codigo si se pasa como String
                .findFirst()
                .orElseThrow(() -> new Exception("Asiento no encontrado"));

        // Validaciones de transición de estados
        if (nuevoEstado == EstadoAsiento.INHABILITADO && 
            (asiento.getEstado() == EstadoAsiento.OCUPADO || asiento.getEstado() == EstadoAsiento.RESERVADO)) {
            throw new Exception("No se puede inhabilitar un asiento que ya tiene una transacción (Ocupado/Reservado).");
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
            int numero = (i % asientosPorFila) + 1; // Cambiado a int
            Long id = (long) (i + 1); // ID numérico para el asiento
            String codigo = idZona + "-" + fila + numero; // Código de negocio (String)
            
            EstadoAsiento estado = EstadoAsiento.DISPONIBLE;
            // Simular algunos inhabilitados y ocupados
            if (i % 12 == 0) estado = EstadoAsiento.INHABILITADO;
            if (i % 15 == 0) estado = EstadoAsiento.OCUPADO;

            zona.getListaAsientos().add(new Asiento(id, codigo, fila, numero, estado)); // Constructor actualizado
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