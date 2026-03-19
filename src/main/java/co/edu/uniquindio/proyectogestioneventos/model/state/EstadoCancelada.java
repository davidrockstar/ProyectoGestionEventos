package co.edu.uniquindio.proyectogestioneventos.model.state;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;

/**
 * Estado concreto: la compra ha sido cancelada.
 */
public class EstadoCancelada implements IEstadoCompra {
    @Override
    public void pagar(Compra compra) {
        System.out.println("No se puede pagar una compra cancelada.");
    }

    @Override
    public void cancelar(Compra compra) {
        System.out.println("La compra " + compra.getIdCompra() + " ya está cancelada.");
    }

    @Override
    public void reembolsar(Compra compra) {
        System.out.println("No se puede reembolsar una compra cancelada.");
    }
}
