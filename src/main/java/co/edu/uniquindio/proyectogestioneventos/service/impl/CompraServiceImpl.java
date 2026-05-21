package co.edu.uniquindio.proyectogestioneventos.service.impl;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoAsiento;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEntrada;
import co.edu.uniquindio.proyectogestioneventos.pago.IPago;
import co.edu.uniquindio.proyectogestioneventos.service.ICompraService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompraServiceImpl implements ICompraService {

    @Override
    public Compra crearCompra(Usuario usuario, Evento evento, List<Entrada> entradas) {
        // Asumimos que CompraBuilder será corregido o la creación se manejará aquí.
        // Por ahora, usamos el constructor directamente.
        Compra nuevaCompra = new Compra(
                "C-" + UUID.randomUUID().toString().substring(0, 5),
                usuario,
                evento,
                LocalDateTime.now(),
                EstadoCompra.CREADA
        );
        nuevaCompra.setListaEntradas(entradas);
        Taquilla.getInstance().getCompras().add(nuevaCompra);
        return nuevaCompra;
    }

    @Override
    public void agregarEntrada(Compra compra, Zona zona, Asiento asiento) throws Exception {
        if (asiento.getEstado() != EstadoAsiento.DISPONIBLE) {
            throw new Exception("El asiento ya no está disponible.");
        }
        Long idEntrada = (long) (compra.getListaEntradas().size() + 1); // Generar un ID Long simple
        Entrada entrada = new Entrada(idEntrada, zona, asiento, zona.getPrecioBase(), EstadoEntrada.VALIDADA,
                                      compra.getEvento(), // El evento de la compra
                                      compra.getUsuario()); // El propietario es el usuario de la compra
        compra.agregarEntrada(entrada);
        asiento.setEstado(EstadoAsiento.RESERVADO);
    }

    /*
     * NOTA: La lógica de servicios adicionales ahora se maneja con el patrón Decorator.
     * Este método ya no es compatible. En lugar de "agregar" un servicio a la compra,
     * se debe "envolver" el objeto Comprable (la compra) con un decorador de servicio
     * en el nivel del controlador o facade.
     *
    @Override
    public void agregarServicioAdicional(Compra compra, ServicioAdicional servicio) {
        // compra.agregarServicioAdicional(servicio); // Obsoleto
    }
    */

    @Override
    public void cancelarCompra(Compra compra) {
        // Regla: Cualquier asiento asociado a una compra cancelada vuelve a estar DISPONIBLE
        // (Cubre transiciones: RESERVADO -> DISPONIBLE y OCUPADO -> DISPONIBLE)
        compra.getListaEntradas().forEach(entrada -> {
            if (entrada.getAsiento() != null) {
                entrada.getAsiento().setEstado(EstadoAsiento.DISPONIBLE);
                Taquilla.getInstance().incrementMetricsUpdateCounter(); // Notificar cambio para métricas
            }
        });

        // Delega la lógica de cancelación al estado actual de la compra
        compra.cancelar();
    }

    @Override
    public void realizarPago(Compra compra, IPago metodoPago) throws Exception {
        if (metodoPago == null) {
            throw new Exception("Método de pago no válido.");
        }
        // Asigna la estrategia de pago a la compra
        compra.setMetodoPago(metodoPago);
        // Delega la lógica de pago al estado actual
        compra.pagar();

        // Sincronización de Estados de Asientos
        if (compra.getEstado() == EstadoCompra.PAGADA) {
            compra.getListaEntradas().forEach(entrada -> {
                if (entrada.getAsiento() != null) {
                    entrada.getAsiento().setEstado(EstadoAsiento.OCUPADO);
                    entrada.setEstado(EstadoEntrada.VALIDADA);
                }
            });
            // Notificar una sola vez al final del proceso de pago
            Taquilla.getInstance().incrementMetricsUpdateCounter();
        }
    }

    @Override
    public List<Compra> obtenerHistorialCompras(Usuario usuario, LocalDate fecha, String nombreEvento, EstadoCompra estado) {
        Stream<Compra> stream = Taquilla.getInstance().getCompras().stream()
                .filter(c -> c.getUsuario().equals(usuario));

        if (fecha != null) {
            stream = stream.filter(c -> c.getFechaCreacion().toLocalDate().equals(fecha));
        }
        if (nombreEvento != null && !nombreEvento.isEmpty()) {
            stream = stream.filter(c -> c.getEvento().getNombre().equalsIgnoreCase(nombreEvento));
        }
        if (estado != null) {
            stream = stream.filter(c -> c.getEstado() == estado);
        }
        return stream.collect(Collectors.toList());
    }

    public List<Compra> listarTodasLasCompras() {
        return Taquilla.getInstance().getCompras();
    }

    @Override
    public void registrarReembolso(Compra compra) throws Exception {
        if (compra == null || compra.getEstado() != EstadoCompra.PAGADA) {
            throw new Exception("Solo se pueden reembolsar compras con pago registrado.");
        }

        compra.reembolsar();

        // Regla: El reembolso libera automáticamente la ocupación
        compra.getListaEntradas().forEach(entrada -> {
            if (entrada.getAsiento() != null) {
                entrada.getAsiento().setEstado(EstadoAsiento.DISPONIBLE);
                Taquilla.getInstance().incrementMetricsUpdateCounter(); // Notificar cambio para métricas
            }
        });
    }

    @Override
    public void reasignarAsiento(Compra compra, Entrada entrada, Asiento nuevoAsiento) throws Exception {
        if (compra == null || entrada == null || nuevoAsiento == null || nuevoAsiento.getEstado() != EstadoAsiento.DISPONIBLE) {
            throw new Exception("El nuevo asiento seleccionado no está disponible.");
        }

        if (entrada.getAsiento() != null) {
            entrada.getAsiento().setEstado(EstadoAsiento.DISPONIBLE);
        }

        boolean estaPagada = (compra.getEstado() == EstadoCompra.PAGADA);
        nuevoAsiento.setEstado(estaPagada ? EstadoAsiento.OCUPADO : EstadoAsiento.RESERVADO);
        entrada.setAsiento(nuevoAsiento);
        Taquilla.getInstance().incrementMetricsUpdateCounter(); // Notificar cambio para métricas
    }
}