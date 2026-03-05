package co.edu.uniquindio.proyectogestioneventos.model;

import java.io.Serializable;

public class ServicioAdicional implements Serializable {
    private String idServicio;
    private String nombre;
    private double precio;

    public ServicioAdicional(String idServicio, String nombre, double precio) {
        this.idServicio = idServicio;
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters y Setters
    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "ServicioAdicional{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                '}';
    }
}
