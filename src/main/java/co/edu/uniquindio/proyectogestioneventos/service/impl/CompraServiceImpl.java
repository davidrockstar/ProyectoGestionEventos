package co.edu.uniquindio.proyectogestioneventos.service.impl;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoAsiento;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEntrada;
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
        CompraBuilder builder = new CompraBuilder();
        builder.setUsuario(usuario)
                .setEvento(evento);

        for (Entrada entrada : entradas) {
            builder.agregarEntrada(entrada);
        }

        Compra nuevaCompra = builder.build();
        Taquilla.getInstance().getCompras().add(nuevaCompra);
        return nuevaCompra;
    }

    @Override
    public void agregarEntrada(Compra compra, Zona zona, Asiento asiento) throws Exception {
        if (asiento.getEstado() != EstadoAsiento.DISPONIBLE) {
            throw new Exception("El asiento ya no está disponible.");
        }
        String idEntrada = "EN-" + UUID.randomUUID().toString().substring(0, 4);
        Entrada entrada = new Entrada(idEntrada, zona, asiento, zona.getPrecioBase(), EstadoEntrada.ACTIVA);
        compra.agregarEntrada(entrada);
        asiento.setEstado(EstadoAsiento.RESERVADO);
    }

    @Override
    public void agregarServicioAdicional(Compra compra, ServicioAdicional servicio) {
        compra.agregarServicioAdicional(servicio);
    }

    @Override
    public void cancelarCompra(Compra compra) {
        compra.getListaEntradas().forEach(entrada -> {
            if (entrada.getAsiento() != null) {
                entrada.getAsiento().setEstado(EstadoAsiento.DISPONIBLE);
            }
        });
        compra.setEstado(EstadoCompra.CANCELADA);
    }

    @Override
    public void realizarPago(Compra compra, MetodoPago metodoPago) throws Exception {
        if (metodoPago == null) {
            throw new Exception("Método de pago no válido.");
        }
        compra.setEstado(EstadoCompra.PAGADA);
        compra.getListaEntradas().forEach(entrada -> {
            if (entrada.getAsiento() != null) {
                entrada.getAsiento().setEstado(EstadoAsiento.VENDIDO);
            }
        });
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
}
