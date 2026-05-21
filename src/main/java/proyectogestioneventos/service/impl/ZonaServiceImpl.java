package proyectogestioneventos.service.impl;

import proyectogestioneventos.model.Recinto;
import proyectogestioneventos.model.Taquilla;
import proyectogestioneventos.model.Zona;
import proyectogestioneventos.service.IZonaService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ZonaServiceImpl implements IZonaService {

    @Override
    public List<Zona> listarZonas(String idRecinto) {
        return Taquilla.getInstance().getRecintos().stream()
                .filter(r -> r.getIdRecinto().equals(idRecinto))
                .findFirst()
                .map(Recinto::getListaZonas)
                .orElse(new ArrayList<>());
    }

    @Override
    public Zona crearZona(String idRecinto, String nombre, int capacidad, double precioBase) throws Exception {
        Recinto recinto = Taquilla.getInstance().getRecintos().stream()
                .filter(r -> r.getIdRecinto().equals(idRecinto))
                .findFirst()
                .orElseThrow(() -> new Exception("Recinto no encontrado."));

        validarDatos(nombre, capacidad, precioBase);

        String id = "Z-" + UUID.randomUUID().toString().substring(0, 5);
        Zona nueva = new Zona(id, nombre, capacidad, precioBase, new ArrayList<>());
        recinto.agregarZona(nueva);
        return nueva;
    }

    @Override
    public Zona actualizarZona(String idRecinto, String idZona, String nombre, int capacidad, double precioBase) throws Exception {
        Recinto recinto = Taquilla.getInstance().getRecintos().stream()
                .filter(r -> r.getIdRecinto().equals(idRecinto))
                .findFirst()
                .orElseThrow(() -> new Exception("Recinto no encontrado."));

        Zona zona = recinto.getListaZonas().stream()
                .filter(z -> z.getIdZona().equals(idZona))
                .findFirst()
                .orElseThrow(() -> new Exception("Zona no encontrada."));

        validarDatos(nombre, capacidad, precioBase);

        zona.setNombre(nombre);
        zona.setCapacidad(capacidad);
        zona.setPrecioBase(precioBase);
        return zona;
    }

    @Override
    public void eliminarZona(String idRecinto, String idZona) throws Exception {
        Recinto recinto = Taquilla.getInstance().getRecintos().stream()
                .filter(r -> r.getIdRecinto().equals(idRecinto))
                .findFirst()
                .orElseThrow(() -> new Exception("Recinto no encontrado."));

        Zona zona = recinto.getListaZonas().stream()
                .filter(z -> z.getIdZona().equals(idZona))
                .findFirst()
                .orElseThrow(() -> new Exception("Zona no encontrada."));

        recinto.eliminarZona(zona);
    }

    private void validarDatos(String nombre, int capacidad, double precioBase) throws Exception {
        if (nombre == null || nombre.isEmpty()) throw new Exception("El nombre es obligatorio.");
        if (capacidad <= 0) throw new Exception("La capacidad debe ser mayor a 0.");
        if (precioBase < 0) throw new Exception("El precio no puede ser negativo.");
    }

    @Override
    public void generarDatosPrueba(String idRecinto) throws Exception {
        crearZona(idRecinto, "VIP", 50, 150000);
        crearZona(idRecinto, "Preferencial", 100, 100000);
        crearZona(idRecinto, "General", 300, 50000);
    }
}