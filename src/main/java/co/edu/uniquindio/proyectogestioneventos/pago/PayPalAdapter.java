package co.edu.uniquindio.proyectogestioneventos.pago;

import co.edu.uniquindio.proyectogestioneventos.pago.externo.PayPalGateway;

public class PayPalAdapter implements IPago {
    private PayPalGateway payPalGateway;

    public PayPalAdapter(PayPalGateway payPalGateway) {
        this.payPalGateway = payPalGateway;
    }

    @Override
    public boolean procesarPago(double monto) {
        // Aquí se utiliza el gateway de PayPal para procesar el monto
        return true; 
    }

    @Override
    public String getDetalles() {
        return "Procesado vía PayPal (Adaptador Externo)";
    }
}