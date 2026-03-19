package co.edu.uniquindio.proyectogestioneventos.pago.externo;

public class StripeGateway {
    public boolean charge(double amount) {
        System.out.println("Cobrando $" + amount + " a través de Stripe.");
        // Lógica de la API de Stripe...
        return true; // Simula un pago exitoso
    }
}
