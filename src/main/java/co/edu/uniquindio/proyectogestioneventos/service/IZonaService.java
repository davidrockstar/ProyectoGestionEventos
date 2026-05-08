package co.edu.uniquindio.proyectogestioneventos.service;

import co.edu.uniquindio.proyectogestioneventos.model.Zona;
import java.util.List;

public interface IZonaService {
    List<Zona> listarZonas(String idRecinto);
    Zona crearZona(String idRecinto, String nombre, int capacidad, double precioBase) throws Exception;
    Zona actualizarZona(String idRecinto, String idZona, String nombre, int capacidad, double precioBase) throws Exception;
    void eliminarZona(String idRecinto, String idZona) throws Exception;
    void generarDatosPrueba(String idRecinto) throws Exception;
}