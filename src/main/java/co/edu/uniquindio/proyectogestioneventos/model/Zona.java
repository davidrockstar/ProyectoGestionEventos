package co.edu.uniquindio.proyectogestioneventos.model;

import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoAsiento;

import java.util.List;

public class Zona {
    private String idZona;
    private String nombre;
    private int capacidad;
    private double precioBase;
    private List<Asiento> listaAsientos;

    public Zona(String idZona, String nombre, int capacidad, double precioBase, List<Asiento> listaAsientos) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        this.listaAsientos = listaAsientos;
    }

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public List<Asiento> getListaAsientos() {
        return listaAsientos;
    }

    public void setListaAsientos(List<Asiento> listaAsientos) {
        this.listaAsientos = listaAsientos;
    }

    public double calcularOcupacion() {
        if (listaAsientos == null || listaAsientos.isEmpty()) {
            return 0.0;
        }
        long asientosOcupados = listaAsientos.stream()
                .filter(asiento -> asiento.getEstado() == EstadoAsiento.VENDIDO || asiento.getEstado() == EstadoAsiento.RESERVADO)
                .count();
        return (double) asientosOcupados / capacidad * 100;
    }
}
