package co.edu.uniquindio.proyectogestioneventos.model;

import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEntrada;

public class Entrada {
    private String idEntrada;
    private Zona zona;
    private Asiento asiento; // Puede ser null si la zona no es numerada
    private double precioFinal;
    private EstadoEntrada estado;

    public Entrada(String idEntrada, Zona zona, Asiento asiento, double precioFinal, EstadoEntrada estado) {
        this.idEntrada = idEntrada;
        this.zona = zona;
        this.asiento = asiento;
        this.precioFinal = precioFinal;
        this.estado = estado;
    }

    public String getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(String idEntrada) {
        this.idEntrada = idEntrada;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public EstadoEntrada getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntrada estado) {
        this.estado = estado;
    }
}
