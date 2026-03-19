package co.edu.uniquindio.proyectogestioneventos.model;

import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompraBuilder {

    private String idCompra;
    private Usuario usuario;
    private Evento evento;
    private LocalDateTime fechaCreacion;
    private EstadoCompra estado;
    private List<Entrada> listaEntradas = new ArrayList<>();
    private List<ServicioAdicional> listaServiciosAdicionales = new ArrayList<>();

    public CompraBuilder() {
        this.idCompra = UUID.randomUUID().toString().substring(0, 8);
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoCompra.PENDIENTE;
    }

    public CompraBuilder setUsuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public CompraBuilder setEvento(Evento evento) {
        this.evento = evento;
        return this;
    }

    public CompraBuilder setEstado(EstadoCompra estado) {
        this.estado = estado;
        return this;
    }

    public CompraBuilder agregarEntrada(Entrada entrada) {
        this.listaEntradas.add(entrada);
        return this;
    }

    public CompraBuilder agregarServicio(ServicioAdicional servicio) {
        this.listaServiciosAdicionales.add(servicio);
        return this;
    }

    public Compra build() {
        if (usuario == null || evento == null) {
            throw new IllegalStateException("El usuario y el evento son obligatorios para crear una compra.");
        }
        Compra compra = new Compra(idCompra, usuario, evento, fechaCreacion, estado);
        compra.setListaEntradas(listaEntradas);
        compra.setListaServiciosAdicionales(listaServiciosAdicionales);
        compra.calcularTotal();
        return compra;
    }
}
