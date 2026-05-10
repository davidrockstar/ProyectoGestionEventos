package co.edu.uniquindio.proyectogestioneventos.model;

import javafx.collections.FXCollections;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Taquilla implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Taquilla instance;

    private final ObservableList<Usuario> usuarios;
    private final ObservableList<Evento> eventos;
    private final ObservableList<Compra> compras;
    private final ObservableList<Recinto> recintos;
    private final ObservableList<Incidencia> incidencias;
    private final ObservableList<ServicioAdicional> servicios;
    private final IntegerProperty metricsUpdateCounter; // Para notificar cambios que afectan métricas

    private Taquilla() {
        this.usuarios = FXCollections.observableArrayList();
        this.eventos = FXCollections.observableArrayList();
        this.compras = FXCollections.observableArrayList();
        this.recintos = FXCollections.observableArrayList();
        this.incidencias = FXCollections.observableArrayList();
        this.servicios = FXCollections.observableArrayList();
        this.metricsUpdateCounter = new SimpleIntegerProperty(0);
    }

    public static Taquilla getInstance() {
        if (instance == null) {
            instance = new Taquilla();
        }
        return instance;
    }

    // Getters para todas las listas
    public ObservableList<Usuario> getUsuarios() {
        return usuarios;
    }

    public ObservableList<Evento> getEventos() {
        return eventos;
    }

    public ObservableList<Compra> getCompras() {
        return compras;
    }

    public ObservableList<Recinto> getRecintos() {
        return recintos;
    }

    public ObservableList<Incidencia> getIncidencias() {
        return incidencias;
    }

    public ObservableList<ServicioAdicional> getServicios() {
        return servicios;
    }

    public IntegerProperty metricsUpdateCounterProperty() {
        return metricsUpdateCounter;
    }

    public void incrementMetricsUpdateCounter() {
        metricsUpdateCounter.set(metricsUpdateCounter.get() + 1);
    }

    // Métodos de negocio
    public void agregarUsuario(Usuario usuario) {
        if (usuario != null) {
            this.usuarios.add(usuario);
        }
    }

    public Optional<Usuario> validarUsuario(String email, String contrasena) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getContrasena().equals(contrasena))
                .findFirst();
    }
}
