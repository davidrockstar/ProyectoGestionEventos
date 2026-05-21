package proyectogestioneventos.model.state;

import proyectogestioneventos.model.Compra;
import proyectogestioneventos.model.enums.EstadoCompra;
import proyectogestioneventos.model.enums.EstadoPago;

public class EstadoPagada implements IEstadoCompra {
    @Override
    public void pagar(Compra compra) {
    }

    @Override
    public void cancelar(Compra compra) {
    }

    @Override
    public void reembolsar(Compra compra) {
        // Sincronizar el registro interno de pago
        if (compra.getRegistroPago() != null) {
            compra.getRegistroPago().setEstado(EstadoPago.REEMBOLSADO);
        }
        compra.setEstado(EstadoCompra.REEMBOLSADA);
    }
}