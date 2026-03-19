package co.edu.uniquindio.proyectogestioneventos.model.decorator;

public class VIPDecorator extends CompraDecorator {
    private final double costoVIP = 50.0; // Costo fijo para el acceso VIP

    public VIPDecorator(Comprable comprable) {
        super(comprable);
    }

    @Override
    public double getPrecioTotal() {
        return super.getPrecioTotal() + costoVIP;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Acceso VIP";
    }
}
