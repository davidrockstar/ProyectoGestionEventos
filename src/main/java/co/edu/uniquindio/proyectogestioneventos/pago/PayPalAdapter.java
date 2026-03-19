package co.edu.uniquindio.proyectogestioneventos.pago;

import co.edu.uniquindio.proyectogestioneventos.pago.externo.PayPalGateway;

public class PayPalAdapter implements IPago {
    private PayPalGateway payPalGateway;

    public PayPalAdapter(PayPalGateway payPalGateway) {
        this.payPalGateway = payPalGateway;
    }

    @Override
    public boolean procesarPago(double monto) {
        payPalGateway.realizarCobro(monto);
        // La API de PayPal no devuelve un booleano, así que asumimos éxito.
        // En un caso real, podríamos manejar excepciones aquí.
        return true;
    }
}
