package co.edu.uniquindio.proyectogestioneventos.pago;

import co.edu.uniquindio.proyectogestioneventos.pago.externo.StripeGateway;

public class StripeAdapter implements IPago {
    private StripeGateway stripeGateway;

    public StripeAdapter(StripeGateway stripeGateway) {
        this.stripeGateway = stripeGateway;
    }

    @Override
    public boolean procesarPago(double monto) {
        return stripeGateway.charge(monto);
    }
}
