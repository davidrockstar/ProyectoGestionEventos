package proyectogestioneventos.pago.impl;

import proyectogestioneventos.pago.IPago;

public class PagoStripe implements IPago {
    @Override
    public boolean procesarPago(double monto) {
        return true; // Simulación de pasarela Stripe
    }

    @Override
    public String getDetalles() {
        return "Procesado vía Stripe (Tarjeta)";
    }
}