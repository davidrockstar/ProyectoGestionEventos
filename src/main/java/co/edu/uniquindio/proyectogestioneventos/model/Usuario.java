package co.edu.uniquindio.proyectogestioneventos.model;

import co.edu.uniquindio.proyectogestioneventos.model.enums.Rol;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String idUsuario;
    private String nombre;
    private String email;
    private String telefono;
    private String contrasena;
    private List<MetodoPago> listaMetodosPago;
    private List<Compra> listaCompras;

    public Usuario(String idUsuario, String nombre, String email, String telefono, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.listaMetodosPago = new ArrayList<>();
        this.listaCompras = new ArrayList<>();
    }

    // Getters y Setters

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public List<MetodoPago> getListaMetodosPago() {
        return listaMetodosPago;
    }

    public void setListaMetodosPago(List<MetodoPago> listaMetodosPago) {
        this.listaMetodosPago = listaMetodosPago;
    }

    public List<Compra> getListaCompras() {
        return listaCompras;
    }

    public void setListaCompras(List<Compra> listaCompras) {
        this.listaCompras = listaCompras;
    }

    public Rol getRol() {
        if (this instanceof Administrador) {
            return Rol.ADMINISTRADOR;
        }
        return Rol.CLIENTE;
    }

    // Métodos de negocio

    public void agregarMetodoPago(MetodoPago metodoPago) {
        this.listaMetodosPago.add(metodoPago);
    }

    public void eliminarMetodoPago(MetodoPago metodoPago) {
        this.listaMetodosPago.remove(metodoPago);
    }
}
