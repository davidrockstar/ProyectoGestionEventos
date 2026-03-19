package co.edu.uniquindio.proyectogestioneventos.service;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import java.time.LocalDate;
import java.util.List;

public interface ICompraService {
    /**
     * RF-006: Inicia una nueva compra para un usuario y un evento.
     */
    Compra crearCompra(Usuario usuario, Evento evento, List<Entrada> entradas);

    /**
     * RF-005, RF-006: Agrega una entrada a una compra existente.
     */
    void agregarEntrada(Compra compra, Zona zona, Asiento asiento) throws Exception;

    /**
     * RF-009: Agrega un servicio adicional a la compra.
     */
    void agregarServicioAdicional(Compra compra, ServicioAdicional servicio);

    /**
     * RF-006: Cancela una compra antes del pago.
     */
    void cancelarCompra(Compra compra);

    /**
     * RF-007: Procesa el pago de la compra.
     */
    // Se omite ComprobantePago por simplicidad en esta fase
    void realizarPago(Compra compra, MetodoPago metodoPago) throws Exception;

    /**
     * RF-010: Consulta el historial de compras de un usuario.
     */
    List<Compra> obtenerHistorialCompras(Usuario usuario, LocalDate fecha, String nombreEvento, EstadoCompra estado);
}
