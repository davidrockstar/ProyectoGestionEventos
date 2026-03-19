package co.edu.uniquindio.proyectogestioneventos.model;

import co.edu.uniquindio.proyectogestioneventos.model.enums.TipoMetodoPago;

public class MetodoPago {
    private String id;
    private TipoMetodoPago tipo;
    private String detalles;

    public MetodoPago(String id, TipoMetodoPago tipo, String detalles) {
        this.id = id;
        this.tipo = tipo;
        this.detalles = detalles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
