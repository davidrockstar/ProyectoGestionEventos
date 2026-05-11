package co.edu.uniquindio.proyectogestioneventos.model.state;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;

public class EstadoPagada implements IEstadoCompra {
    @Override
    public void pagar(Compra compra) {
    }

    @Override
    public void cancelar(Compra compra) {
    }

    @Override
    public void reembolsar(Compra compra) {
        compra.setEstado(EstadoCompra.REEMBOLSADA);
    }
}