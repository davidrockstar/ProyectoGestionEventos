package proyectogestioneventos.pago;

import proyectogestioneventos.pago.externo.StripeGateway;

public class StripeAdapter implements IPago {
    private StripeGateway stripeGateway;

    public StripeAdapter(StripeGateway stripeGateway) {
        this.stripeGateway = stripeGateway;
    }

    @Override
    public boolean procesarPago(double monto) {
        return stripeGateway.charge(monto);
    }

    @Override
    public String getDetalles() {
        return "Procesado vía Stripe (Adaptador Externo)";
    }
}
