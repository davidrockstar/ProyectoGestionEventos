package proyectogestioneventos.model.decorator;

public class VIPDecorator extends CompraDecorator {
    private final double costoAdicionalVIP = 50.0;

    public VIPDecorator(Comprable comprable) {
        super(comprable);
    }

    @Override
    public double getPrecioTotal() {
        return super.getPrecioTotal() + costoAdicionalVIP;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " [Acceso VIP Incluido]";
    }
}