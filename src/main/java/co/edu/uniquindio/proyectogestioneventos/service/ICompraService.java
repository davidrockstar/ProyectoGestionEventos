package co.edu.uniquindio.proyectogestioneventos.service;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import co.edu.uniquindio.proyectogestioneventos.pago.IPago;
import java.time.LocalDate;
import java.util.List;

public interface ICompraService {
    Compra crearCompra(Usuario usuario, Evento evento, List<Entrada> entradas);
    void agregarEntrada(Compra compra, Zona zona, Asiento asiento) throws Exception;
    void cancelarCompra(Compra compra);
    void realizarPago(Compra compra, IPago metodoPago) throws Exception;
    List<Compra> obtenerHistorialCompras(Usuario usuario, LocalDate fecha, String nombreEvento, EstadoCompra estado);
    List<Compra> listarTodasLasCompras();

    /**
     * Procesa un reembolso liberando los asientos asociados.
     */
    void registrarReembolso(Compra compra) throws Exception;

    /**
     * Cambia el asiento de una entrada específica dentro de una compra.
     */
    void reasignarAsiento(Compra compra, Entrada entrada, Asiento nuevoAsiento) throws Exception;
}