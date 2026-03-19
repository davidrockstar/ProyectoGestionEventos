package co.edu.uniquindio.proyectogestioneventos.model.state;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;

/**
 * Estado concreto: la compra ha sido pagada.
 */
public class EstadoPagada implements IEstadoCompra {
    @Override
    public void pagar(Compra compra) {
        System.out.println("La compra " + compra.getIdCompra() + " ya fue pagada.");
    }

    @Override
    public void cancelar(Compra compra) {
        System.out.println("No se puede cancelar una compra que ya fue pagada. Debe solicitar un reembolso.");
    }

    @Override
    public void reembolsar(Compra compra) {
        System.out.println("Procesando reembolso para la compra " + compra.getIdCompra());
        // Aquí iría la lógica para comunicarse con la pasarela de pago y efectuar el reembolso.
        // Si el reembolso es exitoso, se cambia el estado.
        System.out.println("Reembolso exitoso.");
        compra.setEstado(EstadoCompra.REEMBOLSADA);
    }
}
