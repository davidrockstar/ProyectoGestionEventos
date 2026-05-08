package co.edu.uniquindio.proyectogestioneventos.service;

import co.edu.uniquindio.proyectogestioneventos.model.Asiento;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoAsiento;
import java.util.List;

public interface IAsientoService {
    List<Asiento> listarAsientos(String idRecinto, String idZona);
    void cambiarEstadoAsiento(String idRecinto, String idZona, String idAsiento, EstadoAsiento nuevoEstado) throws Exception;
    void generarDatosPrueba(String idRecinto, String idZona) throws Exception;
}