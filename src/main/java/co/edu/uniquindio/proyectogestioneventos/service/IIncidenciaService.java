package co.edu.uniquindio.proyectogestioneventos.service;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.enums.TipoIncidencia;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoIncidencia;
import java.util.List;

public interface IIncidenciaService {
    List<Incidencia> listarIncidencias();
    void registrarIncidencia(TipoIncidencia tipo, String descripcion, Usuario usuario, Evento evento) throws Exception;
    void generarDatosPrueba();
    void cambiarEstado(String idIncidencia, EstadoIncidencia nuevoEstado) throws Exception;
}