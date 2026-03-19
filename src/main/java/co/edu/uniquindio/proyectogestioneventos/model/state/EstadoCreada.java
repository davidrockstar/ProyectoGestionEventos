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
            System.out.println("Error: No se ha seleccionado un método de pago.");
            return;
        }

        ProcesadorPago procesador = new ProcesadorPago();
        procesador.setEstrategiaPago(compra.getMetodoPago());

        System.out.println("Procesando pago para la compra " + compra.getIdCompra() + " por un total de " + compra.getPrecioTotal());
        boolean pagoExitoso = procesador.ejecutarPago(compra.getPrecioTotal());

        if (pagoExitoso) {
            System.out.println("Pago exitoso.");
            // Al pagar, el estado cambia a PAGADA
            compra.setEstado(EstadoCompra.PAGADA);
        } else {
            System.out.println("El pago falló.");
            // Opcional: cambiar a un estado PAGO_FALLIDO si existiera
        }
    }

    @Override
    public void cancelar(Compra compra) {
        System.out.println("Cancelando la compra " + compra.getIdCompra());
        compra.setEstado(EstadoCompra.CANCELADA);
    }

    @Override
    public void reembolsar(Compra compra) {
        System.out.println("No se puede reembolsar una compra que no ha sido pagada.");
    }
}
