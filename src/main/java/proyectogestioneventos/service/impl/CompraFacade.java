package proyectogestioneventos.service.impl;

import proyectogestioneventos.model.*;
import proyectogestioneventos.model.decorator.*;
import proyectogestioneventos.model.*;
import proyectogestioneventos.model.decorator.Comprable;
import proyectogestioneventos.model.decorator.MerchandisingDecorator;
import proyectogestioneventos.model.decorator.SeguroDecorator;
import proyectogestioneventos.model.decorator.VIPDecorator;
import proyectogestioneventos.model.enums.EstadoCompra;
import proyectogestioneventos.pago.IPago;
import proyectogestioneventos.pago.PayPalAdapter;
import proyectogestioneventos.pago.StripeAdapter;
import proyectogestioneventos.pago.externo.PayPalGateway;
import proyectogestioneventos.pago.externo.StripeGateway;
import proyectogestioneventos.service.ICompraService;
import proyectogestioneventos.service.IEventoService;
import proyectogestioneventos.service.IUsuarioService;

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
                    // Añadir el servicio adicional a la lista de la compra base para su posterior visualización
                    Taquilla.getInstance().getServicios().stream()
                            .filter(s -> s.getNombre().equalsIgnoreCase("Acceso VIP"))
                            .findFirst()
                            .ifPresent(compraBase.getListaServiciosAdicionales()::add);
                } else if ("SEGURO".equalsIgnoreCase(servicio)) {
                    compraDecorada = new SeguroDecorator(compraDecorada);
                    Taquilla.getInstance().getServicios().stream()
                            .filter(s -> s.getNombre().equalsIgnoreCase("Seguro de Cancelación"))
                            .findFirst()
                            .ifPresent(compraBase.getListaServiciosAdicionales()::add);
                } else if ("MERCHANDISING".equalsIgnoreCase(servicio)) {
                    compraDecorada = new MerchandisingDecorator(compraDecorada);
                    Taquilla.getInstance().getServicios().stream()
                            .filter(s -> s.getNombre().equalsIgnoreCase("Merchandising Oficial"))
                            .findFirst()
                            .ifPresent(compraBase.getListaServiciosAdicionales()::add);
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

        // 5. Procesar el pago a través del servicio (esto maneja estados y asientos)
        compraService.realizarPago(compraBase, motorDePago);

        if (compraBase.getEstado() != EstadoCompra.PAGADA) {
            throw new Exception("El pago falló o la compra no pudo ser procesada.");
        }

        return compraDecorada;
    }
}
