package co.edu.uniquindio.proyectogestioneventos.model;

import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoIncidencia;
import co.edu.uniquindio.proyectogestioneventos.model.enums.TipoIncidencia;
import java.time.LocalDateTime;

public class Incidencia {
    private Long idIncidencia; // Cambiado a Long
    private TipoIncidencia tipo;
    private String descripcion;
    private LocalDateTime fechaReporte; // Cambiado de 'fecha' a 'fechaReporte'
    private EstadoIncidencia estado;
    private Usuario reportante; // Cambiado de 'usuario' a 'reportante'
    private Evento evento;
    private Compra compra; // Nueva relación opcional

    public Incidencia() {} // Constructor vacío

    // Constructor completo actualizado
    public Incidencia(Long idIncidencia, TipoIncidencia tipo, String descripcion, LocalDateTime fechaReporte, EstadoIncidencia estado, Usuario reportante, Evento evento, Compra compra) {
        this.idIncidencia = idIncidencia;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fechaReporte = fechaReporte;
        this.estado = (estado != null) ? estado : EstadoIncidencia.ABIERTA;
        this.reportante = reportante;
        this.evento = evento;
        this.compra = compra;
    }

    public Long getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(Long idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public TipoIncidencia getTipo() {
        return tipo;
    }

    public void setTipo(TipoIncidencia tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDateTime fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public EstadoIncidencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoIncidencia estado) {
        this.estado = estado;
    }

    public Usuario getReportante() {
        return reportante;
    }

    public void setReportante(Usuario reportante) {
        this.reportante = reportante;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    @Override
    public String toString() {
        return "Incidencia{" +
                "idIncidencia=" + idIncidencia +
                ", tipo=" + tipo +
                ", descripcion='" + descripcion + '\'' +
                ", fechaReporte=" + fechaReporte +
                ", estado=" + estado +
                ", reportante=" + (reportante != null ? reportante.getNombre() : "N/A") +
                ", evento=" + (evento != null ? evento.getNombre() : "N/A") +
                ", compra=" + (compra != null ? compra.getIdCompra() : "N/A") +
                '}';
    }
}