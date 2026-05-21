package proyectogestioneventos.model.state;

import proyectogestioneventos.model.Compra;
import proyectogestioneventos.model.Pago;
import proyectogestioneventos.model.enums.EstadoCompra;
import proyectogestioneventos.model.enums.EstadoPago;
import proyectogestioneventos.pago.ProcesadorPago;

import java.time.LocalDateTime;
import java.util.UUID;

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
            // Crear el registro interno del pago tras el éxito de la pasarela
            Pago registro = new Pago(
                    System.currentTimeMillis(),
                    compra.getPrecioTotal(),
                    LocalDateTime.now(),
                    EstadoPago.APROBADO,
                    UUID.randomUUID().toString(),
                    compra.getMetodoPago().getDetalles()
            );
            compra.setRegistroPago(registro);

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
