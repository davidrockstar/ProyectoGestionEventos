package co.edu.uniquindio.proyectogestioneventos.model.decorator;

public class MerchandisingDecorator extends CompraDecorator {
    private final double costoMerchandising = 25.0; // Costo fijo del paquete

    public MerchandisingDecorator(Comprable comprable) {
        super(comprable);
    }

    @Override
    public double getPrecioTotal() {
        return super.getPrecioTotal() + costoMerchandising;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Paquete de Merchandising";
    }
}
