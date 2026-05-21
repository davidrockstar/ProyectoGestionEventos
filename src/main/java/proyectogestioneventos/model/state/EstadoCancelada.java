package proyectogestioneventos.model.state;

import proyectogestioneventos.model.Compra;

public class EstadoCancelada implements IEstadoCompra {
    @Override
    public void pagar(Compra compra) {
        System.out.println("No se puede pagar una compra que ya ha sido cancelada.");
    }

    @Override
    public void cancelar(Compra compra) {
        System.out.println("La compra ya se encuentra en estado cancelado.");
    }

    @Override
    public void reembolsar(Compra compra) {
        System.out.println("No se puede reembolsar una compra cancelada.");
    }
}