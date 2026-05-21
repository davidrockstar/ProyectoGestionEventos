package proyectogestioneventos.service;

import proyectogestioneventos.model.*;
import proyectogestioneventos.model.Evento;
import proyectogestioneventos.model.Incidencia;
import proyectogestioneventos.model.Usuario;
import proyectogestioneventos.model.enums.TipoIncidencia;
import proyectogestioneventos.model.enums.EstadoIncidencia;
import java.util.List;

public interface IIncidenciaService {
    List<Incidencia> listarIncidencias();
    void registrarIncidencia(TipoIncidencia tipo, String descripcion, Usuario usuario, Evento evento) throws Exception;
    void generarDatosPrueba();
    void cambiarEstado(Long idIncidencia, EstadoIncidencia nuevoEstado) throws Exception;
}