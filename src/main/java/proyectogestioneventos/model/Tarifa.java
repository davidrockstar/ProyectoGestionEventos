package proyectogestioneventos.model;

public class Tarifa {
    private Long idTarifa;
    private String nombre;
    private Double precioBase;
    private Double recargo;
    private Double impuesto;
    private Double precioFinal;

    public Tarifa() {}

    public Tarifa(Long idTarifa, String nombre, Double precioBase, Double recargo, Double impuesto) {
        this.idTarifa = idTarifa;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.recargo = recargo;
        this.impuesto = impuesto;
        calcularPrecioFinal(); // Calcular al construir
    }

    // Getters y Setters
    public Long getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Long idTarifa) {
        this.idTarifa = idTarifa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
        calcularPrecioFinal();
    }

    public Double getRecargo() {
        return recargo;
    }

    public void setRecargo(Double recargo) {
        this.recargo = recargo;
        calcularPrecioFinal();
    }

    public Double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Double impuesto) {
        this.impuesto = impuesto;
        calcularPrecioFinal();
    }

    public Double getPrecioFinal() {
        return precioFinal;
    }

    // No hay setPrecioFinal público, se calcula automáticamente

    public void calcularPrecioFinal() {
        this.precioFinal = (precioBase != null ? precioBase : 0.0)
                + (recargo != null ? recargo : 0.0)
                + (impuesto != null ? impuesto : 0.0);
    }

    @Override
    public String toString() {
        return "Tarifa{" +
                "idTarifa=" + idTarifa +
                ", nombre='" + nombre + '\'' +
                ", precioBase=" + precioBase +
                ", recargo=" + recargo +
                ", impuesto=" + impuesto +
                ", precioFinal=" + precioFinal +
                '}';
    }
}