package co.edu.uniquindio.proyectogestioneventos.model.decorator;

public abstract class CompraDecorator implements Comprable {
    protected Comprable comprable;

    public CompraDecorator(Comprable comprable) {
        this.comprable = comprable;
    }

    @Override
    public double getPrecioTotal() {
        return comprable.getPrecioTotal();
    }

    @Override
    public String getDescripcion() {
        return comprable.getDescripcion();
    }
}
