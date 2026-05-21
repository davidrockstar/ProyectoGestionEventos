package proyectogestioneventos.pago;

public interface IPago {
    boolean procesarPago(double monto);
    String getDetalles();
}