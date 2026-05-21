package co.edu.uniquindio.proyectogestioneventos.utils;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoAsiento;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEntrada;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoEvento;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DatosIniciales {
    public static void cargar() {
        Taquilla taquilla = Taquilla.getInstance();

        // Evitar duplicar datos si ya existen en memoria
        if (!taquilla.getUsuarios().isEmpty()) return;

        // 1. Crear Usuarios Reales (Administrador y Clientes)
        Administrador admin = new Administrador("A01", "Admin Sistema", "admin@eventos.com", "0000", "admin123");
        Usuario cliente1 = new Usuario("C01", "Juan Pérez", "juan@mail.com", "3112223344", "1234");
        Usuario cliente2 = new Usuario("C02", "Maria Lopez", "maria@mail.com", "3154445566", "1234");
        Usuario cliente3 = new Usuario("C03", "Carlos Ruiz", "carlos@mail.com", "3207778899", "1234");

        taquilla.getUsuarios().add(admin);
        taquilla.getUsuarios().add(cliente1);
        taquilla.getUsuarios().add(cliente2);
        taquilla.getUsuarios().add(cliente3);

        // 2. Crear recintos reales
        Recinto coliseo = new Recinto("R01", "Coliseo Central", "Calle 10", "Armenia");
        Recinto teatro = new Recinto("R02", "Teatro Municipal", "Carrera 15", "Pereira");
        Recinto arena = new Recinto("R03", "Arena Norte", "Avenida Bolivar", "Manizales");

        // Configurar Zonas y generar Asientos automáticamente para el Coliseo
        coliseo.agregarZona(crearZonaConAsientos("Z01-C", "VIP", 50, 150000));
        coliseo.agregarZona(crearZonaConAsientos("Z02-C", "Preferencial", 100, 80000));
        coliseo.agregarZona(crearZonaConAsientos("Z03-C", "General", 200, 30000));

        // Configurar Zonas y generar Asientos para el Teatro
        teatro.agregarZona(crearZonaConAsientos("Z01-T", "Platea", 80, 120000));
        teatro.agregarZona(crearZonaConAsientos("Z02-T", "Balcón", 60, 70000));

        // Configurar Zonas y generar Asientos para la Arena
        arena.agregarZona(crearZonaConAsientos("Z01-A", "Pista", 120, 100000));
        arena.agregarZona(crearZonaConAsientos("Z02-A", "Grada Alta", 150, 45000));

        taquilla.getRecintos().add(coliseo);
        taquilla.getRecintos().add(teatro);
        taquilla.getRecintos().add(arena);

        // 3. Crear eventos con estados coherentes
        // Eventos PUBLICADOS (Visibles para compra)
        Evento concierto = new Evento("E01", "Concierto Urbano", "Música", "Show de reggaeton.", "Armenia", 
                LocalDateTime.now().plusDays(10), EstadoEvento.PUBLICADO, coliseo);
        
        Evento festival = new Evento("E02", "Festival Rock", "Música", "Bandas de rock locales.", "Pereira", 
                LocalDateTime.now().plusDays(20), EstadoEvento.PUBLICADO, teatro);
        
        // Evento BORRADOR (No visible para el usuario cliente)
        Evento feria = new Evento("E03", "Feria Gamer", "Tecnología", "Torneos y stands de videojuegos.", "Manizales", 
                LocalDateTime.now().plusDays(30), EstadoEvento.BORRADOR, arena);

        taquilla.getEventos().add(concierto);
        taquilla.getEventos().add(festival);
        taquilla.getEventos().add(feria);

        // 4. Crear Servicios Adicionales Reales para el sistema
        taquilla.getServicios().add(new ServicioAdicional("S01", "Acceso VIP", "Ingreso preferencial y zona lounge.", 50000));
        taquilla.getServicios().add(new ServicioAdicional("S02", "Seguro de Cancelación", "Devolución en caso de imprevistos.", 15000));
        taquilla.getServicios().add(new ServicioAdicional("S03", "Merchandising Oficial", "Camiseta y gorra del evento.", 45000));

        // 5. Generar Compras Reales de Prueba (Para alimentar métricas e historial)
        generarCompra(cliente1, concierto, 2, EstadoCompra.PAGADA, false);
        generarCompra(cliente2, festival, 1, EstadoCompra.PAGADA, true);
        generarCompra(cliente3, concierto, 1, EstadoCompra.CANCELADA, false);
        generarCompra(cliente1, festival, 2, EstadoCompra.PAGADA, true);
    }

    /**
     * Método auxiliar para crear una zona y poblarla con asientos reales.
     * Esto asegura que el sistema de selección de entradas y métricas de ocupación funcione.
     */
    private static Zona crearZonaConAsientos(String idZona, String nombre, int capacidad, double precioBase) {
        ArrayList<Asiento> asientos = new ArrayList<>();
        
        // Definir una distribución simple de 10 asientos por fila
        final int ASIENTOS_POR_FILA = 10;
        
        for (int i = 0; i < capacidad; i++) {
            int filaNum = (i / ASIENTOS_POR_FILA);
            int colNum = (i % ASIENTOS_POR_FILA) + 1;
            
            Long idAsiento = (long) (i + 1); // ID numérico para el asiento
            // Convertir número de fila a letra (1->A, 2->B, etc.)
            String filaLetra = String.valueOf((char) ('A' + filaNum));

            // Todos los asientos inician DISPONIBLES; la compra cambiará su estado
            Asiento asiento = new Asiento(idAsiento, // Nuevo Long ID
                idZona + "-S" + (i + 1), // Código de negocio (String)
                filaLetra, 
                colNum, // Nuevo int numero
                EstadoAsiento.DISPONIBLE
            );
            asientos.add(asiento);
        }
        
        return new Zona(idZona, nombre, capacidad, precioBase, asientos);
    }

    /**
     * Crea una compra completa con entradas y relaciones reales.
     */
    private static void generarCompra(Usuario usuario, Evento evento, int cantEntradas, EstadoCompra estado, boolean conServicios) {
        Taquilla taquilla = Taquilla.getInstance();
        
        // Crear objeto compra
        Compra compra = new Compra("C-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase(), 
                                   usuario, evento, LocalDateTime.now().minusDays(2), estado);

        // Obtener la primera zona disponible del recinto para asignar asientos
        if (evento.getRecinto() != null && !evento.getRecinto().getListaZonas().isEmpty()) {
            Zona zona = evento.getRecinto().getListaZonas().get(0);
            List<Entrada> entradas = new ArrayList<>();
            int asignados = 0;

            for (Asiento asiento : zona.getListaAsientos()) {
                if (asiento.getEstado() == EstadoAsiento.DISPONIBLE && asignados < cantEntradas) {
                    // Crear entrada real
                Long idEntrada = (long) (taquilla.getCompras().size() * 1000 + asignados + 1); // ID único para la entrada
                Entrada entrada = new Entrada(idEntrada, // Nuevo Long ID
                                              zona, asiento, zona.getPrecioBase(), EstadoEntrada.VALIDADA,
                                              evento, usuario); // Nuevos atributos evento y propietario
                    entradas.add(entrada);

                    // Actualizar estado del asiento si la compra es válida
                    if (estado == EstadoCompra.PAGADA) {
                        asiento.setEstado(EstadoAsiento.OCUPADO);
                    }
                    asignados++;
                }
            }
            compra.setListaEntradas(entradas);
        }

        // Agregar servicios adicionales si se requiere
        if (conServicios && !taquilla.getServicios().isEmpty()) {
            compra.getListaServiciosAdicionales().add(taquilla.getServicios().get(0)); // Ejemplo: Acceso VIP
        }

        // La compra ya se agregó a la lista del usuario en su constructor, 
        // ahora la agregamos a la lista global de la taquilla para métricas.
        taquilla.getCompras().add(compra);
    }
}
