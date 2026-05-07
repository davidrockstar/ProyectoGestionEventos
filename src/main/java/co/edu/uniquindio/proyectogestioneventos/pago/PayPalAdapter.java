package co.edu.uniquindio.proyectogestioneventos.pago;

import co.edu.uniquindio.proyectogestioneventos.pago.externo.PayPalGateway;

public class PayPalAdapter implements IPago {
    private PayPalGateway payPalGateway;

    public PayPalAdapter(PayPalGateway payPalGateway) {
        this.payPalGateway = payPalGateway;
    }

    @Override
    public boolean procesarPago(double monto) {
        // Adaptamos el método realizarCobro (void) de PayPal al contrato IPago (boolean)
        payPalGateway.realizarCobro(monto);
        return true; // Asumimos éxito a menos que la pasarela lance excepción
    }
}