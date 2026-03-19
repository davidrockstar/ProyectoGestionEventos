package co.edu.uniquindio.proyectogestioneventos.model;

import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Compra {
    private String idCompra;
    private Usuario usuario;
    private Evento evento;
    private LocalDateTime fechaCreacion;
    private double total;
    private EstadoCompra estado;
    private List<Entrada> listaEntradas;
    private List<ServicioAdicional> listaServiciosAdicionales;

    public Compra(String idCompra, Usuario usuario, Evento evento, LocalDateTime fechaCreacion, EstadoCompra estado) {
        this.idCompra = idCompra;
        this.usuario = usuario;
        this.evento = evento;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.listaEntradas = new ArrayList<>();
        this.listaServiciosAdicionales = new ArrayList<>();
        this.total = 0;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public EstadoCompra getEstado() {
        return estado;
    }

    public void setEstado(EstadoCompra estado) {
        this.estado = estado;
    }

    public List<Entrada> getListaEntradas() {
        return listaEntradas;
    }

    public void setListaEntradas(List<Entrada> listaEntradas) {
        this.listaEntradas = listaEntradas;
        calcularTotal();
    }

    public List<ServicioAdicional> getListaServiciosAdicionales() {
        return listaServiciosAdicionales;
    }

    public void setListaServiciosAdicionales(List<ServicioAdicional> listaServiciosAdicionales) {
        this.listaServiciosAdicionales = listaServiciosAdicionales;
        calcularTotal();
    }

    public void agregarEntrada(Entrada entrada) {
        this.listaEntradas.add(entrada);
        calcularTotal();
    }

    public void eliminarEntrada(Entrada entrada) {
        this.listaEntradas.remove(entrada);
        calcularTotal();
    }

    public void agregarServicioAdicional(ServicioAdicional servicio) {
        this.listaServiciosAdicionales.add(servicio);
        calcularTotal();
    }

    public void eliminarServicioAdicional(ServicioAdicional servicio) {
        this.listaServiciosAdicionales.remove(servicio);
        calcularTotal();
    }

    public void calcularTotal() {
        double totalEntradas = listaEntradas.stream().mapToDouble(Entrada::getPrecioFinal).sum();
        double totalServicios = listaServiciosAdicionales.stream().mapToDouble(ServicioAdicional::getPrecio).sum();
        this.total = totalEntradas + totalServicios;
    }
}
