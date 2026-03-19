package co.edu.uniquindio.proyectogestioneventos.service.impl;

import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.decorator.*;
import co.edu.uniquindio.proyectogestioneventos.pago.IPago;
import co.edu.uniquindio.proyectogestioneventos.pago.PayPalAdapter;
import co.edu.uniquindio.proyectogestioneventos.pago.StripeAdapter;
import co.edu.uniquindio.proyectogestioneventos.pago.externo.PayPalGateway;
import co.edu.uniquindio.proyectogestioneventos.pago.externo.StripeGateway;
import co.edu.uniquindio.proyectogestioneventos.service.ICompraService;
import co.edu.uniquindio.proyectogestioneventos.service.IEventoService;
import co.edu.uniquindio.proyectogestioneventos.service.IUsuarioService;

import java.util.List;

public class CompraFacade {

    private IUsuarioService usuarioService;
    private IEventoService eventoService;
    private ICompraService compraService;

    public CompraFacade() {
        // Inicialización de servicios con implementaciones por defecto
        this.usuarioService = new UsuarioServiceImpl();
        this.eventoService = new EventoServiceImpl();
        this.compraService = new CompraServiceImpl();
    }

    public Comprable realizarCompraCompleta(String idUsuario, String idEvento, List<Entrada> entradas, List<String> serviciosAdicionales, String tipoPago) throws Exception {

        // 1. Obtener entidades principales
        Usuario usuario = usuarioService.obtenerUsuario(idUsuario).orElseThrow(() -> new Exception("Usuario no encontrado"));
        Evento evento = eventoService.obtenerDetalleEvento(idEvento).orElseThrow(() -> new Exception("Evento no encontrado"));

        // 2. Crear la compra base a través del servicio
        Compra compraBase = compraService.crearCompra(usuario, evento, entradas);

        // 3. Aplicar decoradores (Servicios Adicionales)
        Comprable compraDecorada = compraBase;
        if (serviciosAdicionales != null) {
            for (String servicio : serviciosAdicionales) {
                if ("VIP".equalsIgnoreCase(servicio)) {
                    compraDecorada = new VIPDecorator(compraDecorada);
                } else if ("SEGURO".equalsIgnoreCase(servicio)) {
                    compraDecorada = new SeguroDecorator(compraDecorada);
                } else if ("MERCHANDISING".equalsIgnoreCase(servicio)) {
                    compraDecorada = new MerchandisingDecorator(compraDecorada);
                }
            }
        }

        // 4. Seleccionar la estrategia de pago
        IPago motorDePago;
        if ("PAYPAL".equalsIgnoreCase(tipoPago)) {
            motorDePago = new PayPalAdapter(new PayPalGateway());
        } else if ("STRIPE".equalsIgnoreCase(tipoPago)) {
            motorDePago = new StripeAdapter(new StripeGateway());
        } else {
            throw new Exception("Método de pago no soportado");
        }

        // 5. Asignar el método de pago a la compra
        compraBase.setMetodoPago(motorDePago);

        // 6. Procesar el pago usando el patrón State
        compraBase.pagar();

        // 7. Verificar el resultado y finalizar
        if (compraBase.getEstado() == co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra.PAGADA) {
            System.out.println("Fachada: Compra completada con éxito!");
            System.out.println("Descripción final: " + compraDecorada.getDescripcion());
            System.out.println("Total pagado: $" + compraDecorada.getPrecioTotal());
        } else {
            System.out.println("Fachada: El pago fue rechazado.");
            throw new Exception("El pago falló. El estado de la compra es: " + compraBase.getEstado());
        }

        return compraDecorada;
    }
}
