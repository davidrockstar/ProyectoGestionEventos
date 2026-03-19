package co.edu.uniquindio.proyectogestioneventos.model.state;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;

/**
 * Estado concreto: la compra ha sido reembolsada.
 */
public class EstadoReembolsada implements IEstadoCompra {
    @Override
    public void pagar(Compra compra) {
        System.out.println("No se puede pagar una compra que ya fue reembolsada.");
    }

    @Override
    public void cancelar(Compra compra) {
        System.out.println("No se puede cancelar una compra que ya fue reembolsada.");
    }

    @Override
    public void reembolsar(Compra compra) {
        System.out.println("La compra " + compra.getIdCompra() + " ya fue reembolsada.");
    }
}
