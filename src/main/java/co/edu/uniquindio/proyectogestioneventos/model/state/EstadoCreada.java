package co.edu.uniquindio.proyectogestioneventos.model.state;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import co.edu.uniquindio.proyectogestioneventos.pago.ProcesadorPago;

/**
 * Estado concreto: la compra ha sido creada pero no pagada.
 */
public class EstadoCreada implements IEstadoCompra {
    @Override
    public void pagar(Compra compra) {
        if (compra.getMetodoPago() == null) {
            return;
        }

        ProcesadorPago procesador = new ProcesadorPago();
        procesador.setEstrategiaPago(compra.getMetodoPago());

        boolean pagoExitoso = procesador.ejecutarPago(compra.getPrecioTotal());

        if (pagoExitoso) {
            // Al pagar, el estado cambia a PAGADA
            compra.setEstado(EstadoCompra.PAGADA);
        } else {
            // Opcional: cambiar a un estado PAGO_FALLIDO si existiera
        }
    }

    @Override
    public void cancelar(Compra compra) {
        compra.setEstado(EstadoCompra.CANCELADA);
    }

    @Override
    public void reembolsar(Compra compra) {
    }
}
