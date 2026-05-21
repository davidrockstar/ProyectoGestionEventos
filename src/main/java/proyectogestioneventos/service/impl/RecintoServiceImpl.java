package proyectogestioneventos.service.impl;

import proyectogestioneventos.model.Recinto;
import proyectogestioneventos.model.Taquilla;
import proyectogestioneventos.service.IRecintoService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RecintoServiceImpl implements IRecintoService {
    @Override
    public List<Recinto> listarRecintos() {
        return Taquilla.getInstance().getRecintos();
    }

    @Override
    public Optional<Recinto> obtenerRecinto(String idRecinto) {
        return Taquilla.getInstance().getRecintos().stream()
                .filter(r -> r.getIdRecinto().equals(idRecinto))
                .findFirst();
    }

    @Override
    public Recinto crearRecinto(String nombre, String direccion, String ciudad) throws Exception {
        if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() || ciudad == null || ciudad.isEmpty()) {
            throw new Exception("Todos los campos son obligatorios.");
        }
        String id = "R-" + UUID.randomUUID().toString().substring(0, 5);
        Recinto nuevo = new Recinto(id, nombre, direccion, ciudad);
        Taquilla.getInstance().getRecintos().add(nuevo);
        return nuevo;
    }

    @Override
    public Recinto actualizarRecinto(String idRecinto, String nombre, String direccion, String ciudad) throws Exception {
        Recinto recinto = obtenerRecinto(idRecinto).orElseThrow(() -> new Exception("Recinto no encontrado."));
        
        if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() || ciudad == null || ciudad.isEmpty()) {
            throw new Exception("Todos los campos son obligatorios.");
        }

        recinto.setNombre(nombre);
        recinto.setDireccion(direccion);
        recinto.setCiudad(ciudad);
        return recinto;
    }

    @Override
    public void eliminarRecinto(String idRecinto) throws Exception {
        Recinto recinto = obtenerRecinto(idRecinto).orElseThrow(() -> new Exception("Recinto no encontrado."));
        // Validación: No eliminar si tiene eventos asociados
        boolean tieneEventos = Taquilla.getInstance().getEventos().stream().anyMatch(e -> e.getRecinto().getIdRecinto().equals(idRecinto));
        if (tieneEventos) throw new Exception("No se puede eliminar el recinto porque tiene eventos programados.");
        
        Taquilla.getInstance().getRecintos().remove(recinto);
    }
}