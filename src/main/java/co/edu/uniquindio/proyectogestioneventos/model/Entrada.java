package co.edu.uniquindio.proyectogestioneventos.model;

import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEntrada;

import java.time.LocalDateTime;
import java.util.UUID; // Para generar el código QR

public class Entrada {
    private Long idEntrada;
    private String codigoQR;
    private LocalDateTime fechaGeneracion;
    private EstadoEntrada estado;
    private Asiento asiento; // Puede ser null si la zona no es numerada
    private Evento evento;
    private Usuario propietario;
    private Zona zona; // Mantener para acceso directo a propiedades de zona
    private double precioFinal; // Mantener para cálculo de compra

    // Constructor vacío
    public Entrada() {
    }

    // Constructor completo
    public Entrada(Long idEntrada, Zona zona, Asiento asiento, double precioFinal, EstadoEntrada estado, Evento evento, Usuario propietario) {
        this.idEntrada = idEntrada;
        this.zona = zona;
        this.asiento = asiento;
        this.precioFinal = precioFinal;
        this.estado = (estado != null) ? estado : EstadoEntrada.GENERADA; // Estado inicial GENERADA
        this.evento = evento;
        this.propietario = propietario;
        this.fechaGeneracion = LocalDateTime.now(); // Generar fecha actual
        this.codigoQR = "QR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // Generar QR simulado
    }

    // Getters y Setters
    public Long getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(Long idEntrada) {
        this.idEntrada = idEntrada;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public EstadoEntrada getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntrada estado) {
        this.estado = estado;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    @Override
    public String toString() {
        return "Entrada{" +
               "idEntrada=" + idEntrada +
               ", codigoQR='" + codigoQR + '\'' +
               ", fechaGeneracion=" + fechaGeneracion +
               ", estado=" + estado +
               ", asiento=" + (asiento != null ? asiento.getIdAsiento() : "N/A") +
               ", evento=" + (evento != null ? evento.getNombre() : "N/A") +
               ", propietario=" + (propietario != null ? propietario.getNombre() : "N/A") +
               ", zona=" + (zona != null ? zona.getNombre() : "N/A") +
               ", precioFinal=" + precioFinal +
               '}';
    }
}