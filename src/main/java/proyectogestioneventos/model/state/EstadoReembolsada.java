package proyectogestioneventos.model.state;

import proyectogestioneventos.model.Compra;

public class EstadoReembolsada implements IEstadoCompra {
    @Override
    public void pagar(Compra compra) {
        System.out.println("No se puede pagar una compra que ha sido reembolsada.");
    }

    @Override
    public void cancelar(Compra compra) {
        System.out.println("Una compra reembolsada no puede ser cancelada, ya ha finalizado su ciclo.");
    }

    @Override
    public void reembolsar(Compra compra) {
        System.out.println("La compra ya fue reembolsada anteriormente.");
    }
}