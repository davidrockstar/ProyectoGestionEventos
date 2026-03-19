package co.edu.uniquindio.proyectogestioneventos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Taquilla implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Taquilla instance;

    private final List<Usuario> usuarios;
    private final List<Evento> eventos;
    private final List<Compra> compras;
    private final List<Recinto> recintos;
    private final List<Incidencia> incidencias;
    private final List<ServicioAdicional> servicios; // Nueva lista para servicios adicionales

    private Taquilla() {
        this.usuarios = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.compras = new ArrayList<>();
        this.recintos = new ArrayList<>();
        this.incidencias = new ArrayList<>();
        this.servicios = new ArrayList<>(); // Inicializar la nueva lista
    }

    public static Taquilla getInstance() {
        if (instance == null) {
            instance = new Taquilla();
        }
        return instance;
    }

    // Getters para todas las listas
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public List<Recinto> getRecintos() {
        return recintos;
    }

    public List<Incidencia> getIncidencias() {
        return incidencias;
    }

    public List<ServicioAdicional> getServicios() { // Getter para la nueva lista
        return servicios;
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
