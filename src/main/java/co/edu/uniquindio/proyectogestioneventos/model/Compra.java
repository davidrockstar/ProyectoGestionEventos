package co.edu.uniquindio.proyectogestioneventos.model;

import co.edu.uniquindio.proyectogestioneventos.model.decorator.Comprable;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import co.edu.uniquindio.proyectogestioneventos.model.observer.IObservador;
import co.edu.uniquindio.proyectogestioneventos.model.observer.ISujeto;
import co.edu.uniquindio.proyectogestioneventos.model.state.*;
import co.edu.uniquindio.proyectogestioneventos.pago.IPago;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Compra implements Comprable, ISujeto {
    private String idCompra;
    private Usuario usuario;
    private Evento evento;
    private LocalDateTime fechaCreacion;
    private EstadoCompra estado; // Mantenemos el enum para persistencia y consulta simple
    private IEstadoCompra estadoInterno; // Patrón State
    private List<Entrada> listaEntradas;
    private List<ServicioAdicional> listaServiciosAdicionales;
    private List<IObservador> observadores;
    private IPago metodoPago; // Almacena la estrategia de pago elegida

    public Compra(String idCompra, Usuario usuario, Evento evento, LocalDateTime fechaCreacion, EstadoCompra estado) {
        this.idCompra = idCompra;
        this.usuario = usuario;
        this.evento = evento;
        this.fechaCreacion = fechaCreacion;
        this.listaEntradas = new ArrayList<>();
        this.listaServiciosAdicionales = new ArrayList<>();
        this.observadores = new ArrayList<>();
        setEstado(estado); // Inicializa tanto el enum como el objeto de estado
        if (usuario instanceof IObservador) {
            this.agregarObservador((IObservador) usuario);
        }
    }

    // Getters y Setters
    public String getIdCompra() { return idCompra; }
    public void setIdCompra(String idCompra) { this.idCompra = idCompra; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public List<Entrada> getListaEntradas() { return listaEntradas; }
    public void setListaEntradas(List<Entrada> listaEntradas) { this.listaEntradas = listaEntradas; }
    public List<ServicioAdicional> getListaServiciosAdicionales() { return listaServiciosAdicionales; }
    public void setListaServiciosAdicionales(List<ServicioAdicional> listaServiciosAdicionales) { this.listaServiciosAdicionales = listaServiciosAdicionales; }
    public IPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(IPago metodoPago) { this.metodoPago = metodoPago; }

    // Gestión del estado (Patrón State)
    public EstadoCompra getEstado() { return estado; }

    public void setEstado(EstadoCompra nuevoEstado) {
        if (this.estado != nuevoEstado) {
            this.estado = nuevoEstado;
            // Sincroniza el objeto de estado con el enum
            switch (nuevoEstado) {
                case CREADA:
                    this.estadoInterno = new EstadoCreada();
                    break;
                case PAGADA:
                    this.estadoInterno = new EstadoPagada();
                    break;
                case CANCELADA:
                    this.estadoInterno = new EstadoCancelada();
                    break;
                case REEMBOLSADA:
                    this.estadoInterno = new EstadoReembolsada();
                    break;
            }
            notificarObservadores("El estado de su compra " + this.idCompra + " ha cambiado a: " + nuevoEstado);
        }
    }

    // Este método es para que los objetos de estado cambien el estado de la compra
    public void setEstadoInterno(IEstadoCompra nuevoEstado) {
        this.estadoInterno = nuevoEstado;
    }

    // Métodos que delegan el comportamiento al objeto de estado actual
    public void pagar() {
        estadoInterno.pagar(this);
    }

    public void cancelar() {
        estadoInterno.cancelar(this);
    }

    public void reembolsar() {
        estadoInterno.reembolsar(this);
    }

    // Métodos de negocio
    public void agregarEntrada(Entrada entrada) { this.listaEntradas.add(entrada); }
    public void eliminarEntrada(Entrada entrada) { this.listaEntradas.remove(entrada); }

    @Override
    public double getPrecioTotal() {
        return listaEntradas.stream().mapToDouble(Entrada::getPrecioFinal).sum();
    }

    @Override
    public String getDescripcion() {
        return "Compra para el evento: " + evento.getNombre() + " (" + listaEntradas.size() + " entradas)";
    }

    // Implementación del patrón Observer
    @Override
    public void agregarObservador(IObservador observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void eliminarObservador(IObservador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores(String mensaje) {
        for (IObservador observador : observadores) {
            observador.actualizar(mensaje);
        }
    }
}
