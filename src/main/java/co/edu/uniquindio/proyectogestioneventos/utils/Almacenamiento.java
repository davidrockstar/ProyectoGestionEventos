package co.edu.uniquindio.proyectogestioneventos.utils;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import java.util.ArrayList;
import java.util.List;

public class Almacenamiento {
    private static Almacenamiento instance;
    public final List<Usuario> usuarios = new ArrayList<>();
    public final List<Evento> eventos = new ArrayList<>();
    public final List<Compra> compras = new ArrayList<>();
    public final List<ServicioAdicional> servicios = new ArrayList<>();

    private Almacenamiento() {
        // Cargar datos iniciales
        DatosIniciales.cargar(this);
    }

    public static Almacenamiento getInstance() {
        if (instance == null) {
            instance = new Almacenamiento();
        }
        return instance;
    }
}
