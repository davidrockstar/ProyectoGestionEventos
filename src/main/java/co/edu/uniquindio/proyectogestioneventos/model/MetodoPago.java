package co.edu.uniquindio.proyectogestioneventos.model;

import co.edu.uniquindio.proyectogestioneventos.model.enums.TipoMetodoPago;

public class MetodoPago {
    private String idMetodoPago;
    private TipoMetodoPago tipo;
    private String detalles;

    public MetodoPago(String idMetodoPago, TipoMetodoPago tipo, String detalles) {
        this.idMetodoPago = idMetodoPago;
        this.tipo = tipo;
        this.detalles = detalles;
    }

    public String getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(String idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public TipoMetodoPago getTipo() {
        return tipo;
    }

    public void setTipo(TipoMetodoPago tipo) {
        this.tipo = tipo;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
}
