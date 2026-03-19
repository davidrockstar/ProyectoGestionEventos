package co.edu.uniquindio.proyectogestioneventos.model;

import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEvento;
import co.edu.uniquindio.proyectogestioneventos.model.observer.IObservador;
import co.edu.uniquindio.proyectogestioneventos.model.observer.ISujeto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Evento implements ISujeto {
    private String idEvento;
    private String nombre;
    private String categoria;
    private String descripcion;
    private String ciudad;
    private LocalDateTime fechaHora;
    private EstadoEvento estado;
    private Recinto recinto;
    private List<IObservador> observadores; // Lista de observadores

    public Evento(String idEvento, String nombre, String categoria, String descripcion, String ciudad, LocalDateTime fechaHora, EstadoEvento estado, Recinto recinto) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.recinto = recinto;
        this.observadores = new ArrayList<>();
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public void setEstado(EstadoEvento estado) {
        if (this.estado != estado) { // Solo notificar si el estado realmente cambia
            this.estado = estado;
            notificarObservadores("El estado del evento '" + this.nombre + "' ha cambiado a: " + estado);
        }
    }

    public Recinto getRecinto() {
        return recinto;
    }

    public void setRecinto(Recinto recinto) {
        this.recinto = recinto;
    }

    @Override
    public void agregarObservador(IObservador observador) {
        this.observadores.add(observador);
    }

    @Override
    public void eliminarObservador(IObservador observador) {
        this.observadores.remove(observador);
    }

    @Override
    public void notificarObservadores(String mensaje) {
        for (IObservador observador : observadores) {
            observador.actualizar(mensaje);
        }
    }
}
