package co.edu.uniquindio.proyectogestioneventos.service.impl;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoAsiento;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import co.edu.uniquindio.proyectogestioneventos.service.ICompraService;
import co.edu.uniquindio.proyectogestioneventos.utils.Almacenamiento;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompraServiceImpl implements ICompraService {
    private final Almacenamiento almacenamiento = Almacenamiento.getInstance();

    @Override
    public Compra crearCompra(Usuario usuario, Evento evento) {
        String idCompra = "C-" + UUID.randomUUID().toString().substring(0, 4);
        Compra nuevaCompra = new Compra(idCompra, usuario, evento);
        almacenamiento.compras.add(nuevaCompra);
        return nuevaCompra;
    }

    @Override
    public void agregarEntrada(Compra compra, Zona zona, Asiento asiento) throws Exception {
        if (asiento.getEstadoAsiento() != EstadoAsiento.DISPONIBLE) {
            throw new Exception("El asiento ya no está disponible.");
        }
        String idEntrada = "EN-" + UUID.randomUUID().toString().substring(0, 4);
        Entrada entrada = new Entrada(idEntrada, zona, asiento, zona.getPrecioBase());
        compra.getListaEntradas().add(entrada);
        asiento.setEstadoAsiento(EstadoAsiento.RESERVADO);
        recalcularTotal(compra);
    }

    @Override
    public void agregarServicioAdicional(Compra compra, ServicioAdicional servicio) {
        compra.getListaServicios().add(servicio);
        recalcularTotal(compra);
    }

    @Override
    public void cancelarCompra(Compra compra) {
        // Liberar asientos
        compra.getListaEntradas().forEach(entrada -> entrada.getAsiento().setEstadoAsiento(EstadoAsiento.DISPONIBLE));
        compra.setEstadoCompra(EstadoCompra.CANCELADA);
        almacenamiento.compras.remove(compra);
    }

    @Override
    public void realizarPago(Compra compra, MetodoPago metodoPago) throws Exception {
        // Simulación de validación de pago
        if (metodoPago == null) {
            throw new Exception("Método de pago no válido.");
        }
        compra.setEstadoCompra(EstadoCompra.APROBADA);
        compra.getListaEntradas().forEach(entrada -> entrada.getAsiento().setEstadoAsiento(EstadoAsiento.OCUPADO));
    }

    @Override
    public List<Compra> obtenerHistorialCompras(Usuario usuario, LocalDate fecha, String nombreEvento, EstadoCompra estado) {
        Stream<Compra> stream = almacenamiento.compras.stream()
                .filter(c -> c.getUsuario().equals(usuario));

        if (fecha != null) {
            stream = stream.filter(c -> c.getFechaCreacion().toLocalDate().equals(fecha));
        }
        if (nombreEvento != null && !nombreEvento.isEmpty()) {
            stream = stream.filter(c -> c.getEvento().getNombre().equalsIgnoreCase(nombreEvento));
        }
        if (estado != null) {
            stream = stream.filter(c -> c.getEstadoCompra() == estado);
        }
        return stream.collect(Collectors.toList());
    }

    private void recalcularTotal(Compra compra) {
        double totalEntradas = compra.getListaEntradas().stream().mapToDouble(Entrada::getPrecioFinal).sum();
        double totalServicios = compra.getListaServicios().stream().mapToDouble(ServicioAdicional::getPrecio).sum();
        compra.setTotal(totalEntradas + totalServicios);
    }
}
