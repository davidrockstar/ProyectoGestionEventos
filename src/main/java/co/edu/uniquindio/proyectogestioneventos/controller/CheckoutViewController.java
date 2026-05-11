package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import co.edu.uniquindio.proyectogestioneventos.model.*;
import co.edu.uniquindio.proyectogestioneventos.model.decorator.Comprable;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoAsiento;
import co.edu.uniquindio.proyectogestioneventos.model.enums.EstadoCompra;
import co.edu.uniquindio.proyectogestioneventos.service.IAsientoService;
import co.edu.uniquindio.proyectogestioneventos.service.ICompraService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.AsientoServiceImpl;
import co.edu.uniquindio.proyectogestioneventos.service.impl.CompraFacade;
import co.edu.uniquindio.proyectogestioneventos.service.impl.CompraServiceImpl;
import co.edu.uniquindio.proyectogestioneventos.pago.IPago;
import co.edu.uniquindio.proyectogestioneventos.pago.impl.PagoPaypal;
import co.edu.uniquindio.proyectogestioneventos.pago.impl.PagoStripe;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CheckoutViewController {

    @FXML private Label lblNombreEvento, lblFechaEvento, lblCiudadEvento, lblRecintoEvento;
    @FXML private Label lblZona, lblAsiento, lblPrecioBase;
    @FXML private VBox vboxServicios;
    @FXML private Label lblTotalExtras, lblTotalFinal;
    @FXML private ComboBox<String> cbMetodoPago;

    private final IAsientoService asientoService = new AsientoServiceImpl();
    private final ICompraService compraService = new CompraServiceImpl();
    private final CompraFacade compraFacade = new CompraFacade();

    private Evento evento;
    private List<Entrada> entradas;
    private List<String> serviciosNombres;
    private double totalExtras;

    @FXML
    public void initialize() {
        cbMetodoPago.setItems(FXCollections.observableArrayList("Tarjeta de Crédito", "PSE", "Efectivo", "Transferencia"));
        cbMetodoPago.getSelectionModel().selectFirst();
    }

    public void setDatos(Evento evento, List<Entrada> entradas, List<String> serviciosNombres, double totalExtras) {
        this.evento = evento;
        this.entradas = entradas;
        this.serviciosNombres = serviciosNombres;
        this.totalExtras = totalExtras;
        cargarResumen();
    }

    private void cargarResumen() {
        lblNombreEvento.setText(evento.getNombre());
        lblFechaEvento.setText(evento.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        lblCiudadEvento.setText(evento.getCiudad());
        lblRecintoEvento.setText(evento.getRecinto().getNombre());

        if (!entradas.isEmpty()) {
            Entrada e = entradas.get(0);
            lblZona.setText(e.getZona().getNombre());
            lblAsiento.setText(e.getAsiento() != null ? e.getAsiento().getFila() + e.getAsiento().getNumero() : "N/A");
            double subtotalEntradas = entradas.stream().mapToDouble(Entrada::getPrecioFinal).sum();
            lblPrecioBase.setText(String.format("$ %,.0f", subtotalEntradas));
            lblTotalFinal.setText(String.format("$ %,.0f", subtotalEntradas + totalExtras));
        }

        vboxServicios.getChildren().clear();
        serviciosNombres.forEach(s -> {
            Label item = new Label("• " + s);
            item.setStyle("-fx-text-fill: #34495e;");
            vboxServicios.getChildren().add(item);
        });

        lblTotalExtras.setText(String.format("$ %,.0f", totalExtras));
    }

    @FXML
    void onConfirmarCompra(ActionEvent event) {
        Usuario usuario = MyApplication.getUsuarioLogueado();
        String metodoSeleccionado = cbMetodoPago.getValue();

        // 1. Validaciones iniciales
        if (usuario == null) {
            mostrarAlerta("Error de Sesión", "No hay un usuario autenticado.", Alert.AlertType.ERROR);
            return;
        }
        if (metodoSeleccionado == null || metodoSeleccionado.isEmpty()) {
            mostrarAlerta("Campo Requerido", "Por favor seleccione un método de pago.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // 2. Crear la Compra en el modelo global (Estado: CREADA)
            Compra compraReal = compraService.crearCompra(usuario, evento, entradas);
            
            if (compraReal.getPrecioTotal() <= 0 && totalExtras <= 0) {
                throw new Exception("El total de la compra no puede ser cero.");
            }

            // 3. Ejecutar Pago (RESERVADO -> VENDIDO y CREADA -> PAGADA)
            IPago estrategiaPago = metodoSeleccionado.contains("Tarjeta") ? new PagoStripe() : new PagoPaypal();
            compraService.realizarPago(compraReal, estrategiaPago);

            // 4. Transición Final (PAGADA -> CONFIRMADA)
            if (compraReal.getEstado() == EstadoCompra.PAGADA) {
                compraReal.setEstado(EstadoCompra.CONFIRMADA);
            }

            mostrarAlerta("¡Compra Exitosa!", "Gracias por su compra", 
                "Su código de reserva es: " + compraReal.getIdCompra() + 
                "\nLos asientos han sido confirmados como VENDIDOS.", Alert.AlertType.INFORMATION);
            
            navegarAMisCompras();

        } catch (Exception e) {
            mostrarAlerta("Error en la Transacción", "No se pudo completar el proceso", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void navegarAMisCompras() {
        try {
            // Usamos la ruta definida en el contexto para MisCompras
            // Determinar la carpeta según el rol del usuario logueado
            Usuario user = MyApplication.getUsuarioLogueado();
            String folder = (user instanceof Administrador) ? "administrador" : "cliente";

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/proyectogestioneventos/usuario/" + folder + "/MisComprasView.fxml"));
            Parent root = loader.load();
            
            // Obtener el controlador de la vista de destino y pasar el usuario actual
            MisComprasController controller = loader.getController();
            controller.setUsuario(MyApplication.getUsuarioLogueado());
            
            Stage stage = (Stage) lblNombreEvento.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Mis Compras - Historial");
        } catch (Exception e) {
            e.printStackTrace();
            cerrarVentana(); // Fallback si no carga la vista
        }
    }

    @FXML
    void onCancelarCompra(ActionEvent event) {
        try {
            for (Entrada entrada : entradas) {
                if (entrada.getAsiento() != null) {
                    asientoService.cambiarEstadoAsiento(evento.getRecinto().getIdRecinto(), 
                        entrada.getZona().getIdZona(), entrada.getAsiento().getIdAsiento(), EstadoAsiento.DISPONIBLE);
                }
            }
            cerrarVentana();
        } catch (Exception e) {
            System.err.println("Error al liberar asientos: " + e.getMessage());
        }
    }

    private void cerrarVentana() {
        ((Stage) lblNombreEvento.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        mostrarAlerta(titulo, null, msg, tipo);
    }

    private void mostrarAlerta(String titulo, String cab, String msg, Alert.AlertType tipo) {
        Alert a = new Alert(tipo); a.setTitle(titulo); a.setHeaderText(cab); a.setContentText(msg); a.showAndWait();
    }
}