package co.edu.uniquindio.proyectogestioneventos.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Incidencia implements Serializable {
    private String idIncidencia;
    private String tipo; // Ej: "Técnica", "Logística", "Seguridad"
    private String descripcion;
    private LocalDateTime fecha;
    private Object entidadAfectada; // Puede ser un Evento, Compra, etc.

    public Incidencia(String idIncidencia, String tipo, String descripcion, Object entidadAfectada) {
        this.idIncidencia = idIncidencia;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
        this.entidadAfectada = entidadAfectada;
    }

    // Getters y Setters
    public String getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(String idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Object getEntidadAfectada() {
        return entidadAfectada;
    }

    public void setEntidadAfectada(Object entidadAfectada) {
        this.entidadAfectada = entidadAfectada;
    }

    @Override
    public String toString() {
        return "Incidencia{" +
                "tipo='" + tipo + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
