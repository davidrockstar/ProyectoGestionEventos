package co.edu.uniquindio.proyectogestioneventos.model;

import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoAsiento;

public class Asiento {
    private String idAsiento;
    private String fila;
    private String numero;
    private EstadoAsiento estado;

    public Asiento(String idAsiento, String fila, String numero, EstadoAsiento estado) {
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
        this.estado = estado;
    }

    public String getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(String idAsiento) {
        this.idAsiento = idAsiento;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }
}
