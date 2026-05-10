package co.edu.uniquindio.proyectogestioneventos.pago.impl;

import co.edu.uniquindio.proyectogestioneventos.pago.IPago;

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