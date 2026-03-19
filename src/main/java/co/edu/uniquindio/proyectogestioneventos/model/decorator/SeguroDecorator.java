package co.edu.uniquindio.proyectogestioneventos.model.decorator;

public class SeguroDecorator extends CompraDecorator {
    private final double porcentajeSeguro = 0.05; // 5% del valor de la compra

    public SeguroDecorator(Comprable comprable) {
        super(comprable);
    }

    @Override
    public double getPrecioTotal() {
        double precioBase = super.getPrecioTotal();
        return precioBase + (precioBase * porcentajeSeguro);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Seguro de Cancelación";
    }
}
