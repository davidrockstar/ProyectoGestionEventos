package co.edu.uniquindio.proyectogestioneventos.model;

import java.io.Serializable;

public class MetodoPago implements Serializable {
    private String idMetodo;
    private String tipo; // Ej: "Tarjeta de Crédito", "PSE"
    private String numeroSimulado; // Ej: "**** **** **** 1234"

    public MetodoPago(String idMetodo, String tipo, String numeroSimulado) {
        this.idMetodo = idMetodo;
        this.tipo = tipo;
        this.numeroSimulado = numeroSimulado;
    }

    // Getters y Setters
    public String getIdMetodo() {
        return idMetodo;
    }

    public void setIdMetodo(String idMetodo) {
        this.idMetodo = idMetodo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumeroSimulado() {
        return numeroSimulado;
    }

    public void setNumeroSimulado(String numeroSimulado) {
        this.numeroSimulado = numeroSimulado;
    }

    @Override
    public String toString() {
        return "MetodoPago{" +
                "idMetodo='" + idMetodo + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
