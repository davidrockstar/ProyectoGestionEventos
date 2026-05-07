package co.edu.uniquindio.proyectogestioneventos.model.state;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;

public class EstadoPagada implements IEstadoCompra {
    @Override
    public void pagar(Compra compra) {
        System.out.println("La compra ya ha sido pagada.");
    }

    @Override
    public void cancelar(Compra compra) {
        System.out.println("No se puede cancelar una compra pagada. Debe solicitar reembolso.");
    }

    @Override
    public void reembolsar(Compra compra) {
        compra.setEstado(EstadoCompra.REEMBOLSADA);
    }
}