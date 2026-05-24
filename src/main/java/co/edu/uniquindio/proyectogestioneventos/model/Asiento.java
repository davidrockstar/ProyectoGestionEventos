package co.edu.uniquindio.proyectogestioneventos.model;

import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoAsiento;

public class Asiento {
    private Long idAsiento;
    private String codigo; // Identificador de negocio (ej: VIP-01)
    private String fila;
    private int numero;
    private EstadoAsiento estado;

    public Asiento() {} // Constructor vacío para frameworks/serialización

    public Asiento(Long idAsiento, String codigo, String fila, int numero, EstadoAsiento estado) {
        this.idAsiento = idAsiento;
        this.codigo = codigo;
        this.fila = fila;
        this.numero = numero;
        this.estado = estado;
    }

    public Long getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(Long idAsiento) {
        this.idAsiento = idAsiento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Asiento{" +
                "idAsiento=" + idAsiento +
                ", codigo='" + codigo + '\'' +
                ", fila='" + fila + '\'' +
                ", numero=" + numero +
                ", estado=" + estado +
                '}';
    }
}