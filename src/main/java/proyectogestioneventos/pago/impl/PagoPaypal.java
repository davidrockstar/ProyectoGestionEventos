package proyectogestioneventos.pago.impl;

import proyectogestioneventos.pago.IPago;

public class PagoPaypal implements IPago {
    @Override
    public boolean procesarPago(double monto) {
        return true; // Simulación de pasarela PayPal
    }

    @Override
    public String getDetalles() {
        return "Procesado vía PayPal (Cuenta Digital)";
    }
}