package proyectogestioneventos.service;

import proyectogestioneventos.model.Recinto;
import java.util.List;
import java.util.Optional;

public interface IRecintoService {
    /**
     * Obtiene la lista de todos los recintos disponibles.
     */
    List<Recinto> listarRecintos();
    Optional<Recinto> obtenerRecinto(String idRecinto);

    /**
     * Crea un nuevo recinto.
     */
    Recinto crearRecinto(String nombre, String direccion, String ciudad) throws Exception;

    /**
     * Actualiza un recinto existente.
     */
    Recinto actualizarRecinto(String idRecinto, String nombre, String direccion, String ciudad) throws Exception;

    /**
     * Elimina un recinto.
     */
    void eliminarRecinto(String idRecinto) throws Exception;
}