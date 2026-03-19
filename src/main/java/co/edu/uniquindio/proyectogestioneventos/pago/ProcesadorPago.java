package co.edu.uniquindio.proyectogestioneventos.pago;

/**
 * Contexto del patrón Strategy.
 * Permite cambiar el algoritmo de pago dinámicamente.
 */
public class ProcesadorPago {
    private IPago estrategiaPago;

    // Permite cambiar la estrategia en tiempo de ejecución
    public void setEstrategiaPago(IPago estrategiaPago) {
        this.estrategiaPago = estrategiaPago;
    }

    public boolean ejecutarPago(double monto) {
        if (estrategiaPago == null) {
            throw new IllegalStateException("Estrategia de pago no establecida");
        }
        return estrategiaPago.procesarPago(monto);
    }
}
