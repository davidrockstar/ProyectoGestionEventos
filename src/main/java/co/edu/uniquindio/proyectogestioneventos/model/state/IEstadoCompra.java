package co.edu.uniquindio.proyectogestioneventos.model.state;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;

/**
 * Interfaz para el patrón State.
 * Define las acciones que varían según el estado de la compra.
 */
public interface IEstadoCompra {
    void pagar(Compra compra);
    void cancelar(Compra compra);
    void reembolsar(Compra compra);
}
