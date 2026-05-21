package proyectogestioneventos.viewcontroller;

import proyectogestioneventos.model.*;
import proyectogestioneventos.model.*;
import proyectogestioneventos.model.enums.EstadoCompra;
import proyectogestioneventos.service.*;
import proyectogestioneventos.service.impl.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import proyectogestioneventos.service.ICompraService;
import proyectogestioneventos.service.IEventoService;
import proyectogestioneventos.service.IUsuarioService;
import proyectogestioneventos.service.impl.CompraServiceImpl;
import proyectogestioneventos.service.impl.EventoServiceImpl;
import proyectogestioneventos.service.impl.UsuarioServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PanelMetricasAdminViewController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label lblTotalUsuarios, lblTotalEventos, lblTotalCompras, lblIngresosTotales, lblTasaCancelacion;
    @FXML
    private Label lblEventoMasVendido, lblZonaMasOcupada, lblUsuarioMasActivo, lblIngresosPromedio;

    @FXML
    private DatePicker dpInicio, dpFin;
    @FXML
    private ComboBox<String> cbCategoriaFiltro;
    @FXML
    private ComboBox<Evento> cbEventoFiltro;

    @FXML
    private LineChart<String, Number> ventasPorPeriodoChart;
    @FXML
    private PieChart ingresosServiciosAdicionalesChart; // Se usará para Ingresos por Categoría
    @FXML
    private BarChart<String, Number> ocupacionPorZonaChart;
    @FXML
    private BarChart<String, Number> topEventosChart; // Se usará para Ventas por Evento (Cantidad)

    @FXML
    private CategoryAxis topEventosXAxis, ocupacionZonaXAxis;
    @FXML
    private NumberAxis topEventosYAxis, ocupacionZonaYAxis;

    private final IUsuarioService usuarioService = new UsuarioServiceImpl();
    private final IEventoService eventoService = new EventoServiceImpl();
    private final ICompraService compraService = new CompraServiceImpl();

    @FXML
    private void initialize() {
        try {
            configurarFiltros();
            actualizarMetricas(); // Carga automática real al abrir
            configurarActualizacionAutomatica(); // Suscribirse a cambios
        } catch (Exception e) {
            System.err.println("Error al inicializar métricas: " + e.getMessage());
        }
    }

    /**
     * Configura los listeners de JavaFX para reaccionar a cambios en filtros y datos
     */
    private void configurarActualizacionAutomatica() {
        // 1. Escuchar cambios en los filtros de la UI
        if (dpInicio != null) dpInicio.valueProperty().addListener((obs, old, val) -> actualizarMetricas());
        if (dpFin != null) dpFin.valueProperty().addListener((obs, old, val) -> actualizarMetricas());
        if (cbCategoriaFiltro != null)
            cbCategoriaFiltro.valueProperty().addListener((obs, old, val) -> actualizarMetricas());
        if (cbEventoFiltro != null) cbEventoFiltro.valueProperty().addListener((obs, old, val) -> actualizarMetricas());

        // 2. Escuchar cambios reales en el Modelo (Taquilla)
        Taquilla data = Taquilla.getInstance();

        data.getUsuarios().addListener((ListChangeListener<Usuario>) c -> actualizarMetricas());
        data.getCompras().addListener((ListChangeListener<Compra>) c -> actualizarMetricas());

        // Cuando cambian los eventos, también refrescamos los filtros dinámicos
        // O cuando cambia el estado de un asiento dentro de un evento/zona
        Taquilla.getInstance().metricsUpdateCounterProperty().addListener((obs, oldVal, newVal) -> actualizarMetricas());

        data.getEventos().addListener((ListChangeListener<Evento>) c -> {
            configurarFiltros();
            actualizarMetricas();
        });
    }

    private void configurarFiltros() {
        if (cbCategoriaFiltro != null) {
            String seleccionActual = cbCategoriaFiltro.getValue();
            List<String> categorias = eventoService.listarTodosEventos().stream()
                    .filter(e -> e.getCategoria() != null)
                    .map(Evento::getCategoria)
                    .distinct()
                    .collect(Collectors.toList());
            cbCategoriaFiltro.setItems(FXCollections.observableArrayList(categorias));
            if (seleccionActual != null) cbCategoriaFiltro.setValue(seleccionActual);
        }

        if (cbEventoFiltro != null) {
            Evento eventoActual = cbEventoFiltro.getValue();
            cbEventoFiltro.setItems(FXCollections.observableArrayList(eventoService.listarTodosEventos()));
            cbEventoFiltro.setConverter(new StringConverter<Evento>() {
                @Override
                public String toString(Evento e) {
                    return e != null ? e.getNombre() : "Todos los Eventos";
                }

                @Override
                public Evento fromString(String s) {
                    return null;
                }
            });
            if (eventoActual != null) cbEventoFiltro.setValue(eventoActual);
        }
    }

    @FXML
    void onActualizarMetricasClick(ActionEvent event) {
        actualizarMetricas();
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    @FXML
    void onLimpiarFiltrosClick(ActionEvent event) {
        if (dpInicio != null) dpInicio.setValue(null);
        if (dpFin != null) dpFin.setValue(null);
        if (cbCategoriaFiltro != null) cbCategoriaFiltro.getSelectionModel().clearSelection();
        if (cbEventoFiltro != null) cbEventoFiltro.getSelectionModel().clearSelection();
        actualizarMetricas();
    }

    @FXML
    void onScrollUpClick(ActionEvent event) {
        scrollPane.setVvalue(Math.max(0.0, scrollPane.getVvalue() - 0.25));
    }

    @FXML
    void onScrollDownClick(ActionEvent event) {
        scrollPane.setVvalue(Math.min(1.0, scrollPane.getVvalue() + 0.25));
    }

    private void actualizarMetricas() {
        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            List<Evento> eventos = eventoService.listarTodosEventos();
            List<Compra> compras = compraService.listarTodasLasCompras();

            if (usuarios == null || eventos == null || compras == null) return;

            // 1. Obtener valores de filtros
            LocalDate inicio = (dpInicio != null) ? dpInicio.getValue() : null;
            LocalDate fin = (dpFin != null) ? dpFin.getValue() : null;
            String filtroCat = (cbCategoriaFiltro != null) ? cbCategoriaFiltro.getValue() : null;
            Evento evFiltro = (cbEventoFiltro != null) ? cbEventoFiltro.getValue() : null;

            // 2. Filtrar compras
            // 2. Filtrar compras
            List<Compra> filtradas = compras.stream()
                    .filter(c -> c != null && c.getEvento() != null && c.getFechaCreacion() != null)
                    .filter(c -> !c.getListaEntradas().isEmpty()) // Regla 1: Ventas Reales (entradas > 0)
                    .filter(c -> {
                        LocalDate f = c.getFechaCreacion().toLocalDate();
                        boolean matchFecha = (inicio == null || !f.isBefore(inicio)) && (fin == null || !f.isAfter(fin));
                        boolean matchCat = (filtroCat == null || (c.getEvento().getCategoria() != null && c.getEvento().getCategoria().equalsIgnoreCase(filtroCat)));
                        boolean matchEv = (evFiltro == null || c.getEvento().getIdEvento().equals(evFiltro.getIdEvento()));
                        return matchFecha && matchCat && matchEv;
                    }).collect(Collectors.toList());

            // 3. Actualizar Etiquetas Globales
            if (lblTotalUsuarios != null) lblTotalUsuarios.setText(String.valueOf(usuarios.size()));
            if (lblTotalEventos != null) lblTotalEventos.setText(String.valueOf(eventos.size()));
            if (lblTotalCompras != null) lblTotalCompras.setText(String.valueOf(filtradas.size()));

            // Regla 2: Ingresos Reales (Solo PAGADAS o CONFIRMADAS)
            double ingresosTotales = filtradas.stream()
                    .filter(c -> c.getEstado() == EstadoCompra.PAGADA)
                    .mapToDouble(Compra::getPrecioTotal).sum();
            if (lblIngresosTotales != null) lblIngresosTotales.setText(String.format("$ %,.2f", ingresosTotales));

            if (filtradas.isEmpty()) {
                if (lblTasaCancelacion != null) lblTasaCancelacion.setText("0.0%");
                if (lblIngresosPromedio != null) lblIngresosPromedio.setText("$ 0.00");
            } else {
                long totalFiltradas = filtradas.size();
                long canceladas = filtradas.stream().filter(c -> c.getEstado() == EstadoCompra.CANCELADA).count();
                if (lblTasaCancelacion != null)
                    lblTasaCancelacion.setText(String.format("%.1f%%", (double) canceladas / totalFiltradas * 100));

                // Ingresos Promedio: Ingresos Totales / Cantidad de compras que generaron dinero
                long exitosas = filtradas.stream().filter(c -> c.getEstado() == EstadoCompra.PAGADA).count();
                if (lblIngresosPromedio != null)
                    lblIngresosPromedio.setText(String.format("$ %,.2f", exitosas > 0 ? ingresosTotales / exitosas : 0));

                // Regla 4: Evento más vendido (Por cantidad de entradas reales)
                String topEv = filtradas.stream()
                        .filter(c -> c.getEstado() != EstadoCompra.CANCELADA)
                        .collect(Collectors.groupingBy(c -> c.getEvento().getNombre(),
                                Collectors.summingInt(c -> c.getListaEntradas().size())))
                        .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");
                if (lblEventoMasVendido != null) lblEventoMasVendido.setText(topEv);

                // Regla 5: Usuario más activo (Por cantidad de compras reales realizadas)
                String topUser = filtradas.stream()
                        .collect(Collectors.groupingBy(c -> c.getUsuario().getNombre(), Collectors.counting()))
                        .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");
                if (lblUsuarioMasActivo != null) lblUsuarioMasActivo.setText(topUser);
            }

            // Regla 3: Ocupación Real (Asientos vendidos / Capacidad total)
            String topZona = eventos.stream()
                    .filter(e -> e != null && e.getRecinto() != null)
                    .filter(e -> (filtroCat == null || (e.getCategoria() != null && e.getCategoria().equalsIgnoreCase(filtroCat))) &&
                            (evFiltro == null || e.getIdEvento().equals(evFiltro.getIdEvento())))
                    .flatMap(e -> e.getRecinto().getListaZonas().stream())
                    .filter(z -> z != null)
                    .collect(Collectors.groupingBy(Zona::getNombre, Collectors.averagingDouble(Zona::calcularOcupacion)))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(e -> e.getKey() + String.format(" (%.1f%%)", e.getValue()))
                    .orElse("N/A");

            if (lblZonaMasOcupada != null) lblZonaMasOcupada.setText(topZona);

            // 6. Actualizar Gráficos con datos reales
            actualizarGraficoVentas(filtradas);
            actualizarGraficoOcupacion(eventos, filtroCat, evFiltro);
            actualizarGraficoIngresosCategorias(filtradas);
            actualizarGraficoPeriodos(filtradas);

        } catch (Exception e) {
            System.err.println("Error fatal actualizando métricas: " + e.getMessage());
            e.printStackTrace();
        }    }

    private void actualizarGraficoVentas(List<Compra> filtradas) {
        if (topEventosChart == null) return;
        topEventosChart.setAnimated(false); // Desactivar animación para evitar glitches al limpiar
        topEventosChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tickets Vendidos");

        Map<String, Integer> datos = filtradas.stream()
                .filter(c -> c.getEstado() != EstadoCompra.CANCELADA && c.getEvento() != null)
                .collect(Collectors.groupingBy(c -> c.getEvento().getNombre(),
                        Collectors.summingInt(c -> c.getListaEntradas().size())));

        datos.forEach((nombre, total) -> series.getData().add(new XYChart.Data<>(nombre, total)));
        if (!series.getData().isEmpty()) topEventosChart.getData().add(series);
    }

    private void actualizarGraficoOcupacion(List<Evento> eventos, String cat, Evento ev) {
        if (ocupacionPorZonaChart == null) return;
        ocupacionPorZonaChart.setAnimated(false);
        ocupacionPorZonaChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ocupación %");

        eventos.stream()
                .filter(e -> e != null && e.getRecinto() != null)
                .filter(e -> (cat == null || (e.getCategoria() != null && e.getCategoria().equalsIgnoreCase(cat))) &&
                        (ev == null || e.getIdEvento().equals(ev.getIdEvento())))
                .flatMap(e -> e.getRecinto().getListaZonas().stream())
                .collect(Collectors.groupingBy(Zona::getNombre, Collectors.averagingDouble(Zona::calcularOcupacion)))
                .forEach((zona, porc) -> series.getData().add(new XYChart.Data<>(zona, porc)));

        if (!series.getData().isEmpty()) ocupacionPorZonaChart.getData().add(series);
    }

    private void actualizarGraficoIngresosCategorias(List<Compra> filtradas) {
        if (ingresosServiciosAdicionalesChart == null) return;
        ingresosServiciosAdicionalesChart.setAnimated(false);
        ingresosServiciosAdicionalesChart.getData().clear();

        Map<String, Double> porCat = filtradas.stream()
                .filter(c -> c.getEvento() != null && c.getEstado() == EstadoCompra.PAGADA)
                .collect(Collectors.groupingBy(c -> c.getEvento().getCategoria() != null ? c.getEvento().getCategoria() : "Otros",
                        Collectors.summingDouble(Compra::getPrecioTotal)));

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        porCat.forEach((k, v) -> data.add(new PieChart.Data(k, v)));
        ingresosServiciosAdicionalesChart.setData(data.isEmpty() ? FXCollections.observableArrayList(new PieChart.Data("Sin Datos", 1)) : data);
    }

    private void actualizarGraficoPeriodos(List<Compra> filtradas) {
        if (ventasPorPeriodoChart == null) return;
        ventasPorPeriodoChart.setAnimated(false);
        ventasPorPeriodoChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ventas Diarias");

        filtradas.stream()
                .filter(c -> c.getFechaCreacion() != null)
                .collect(Collectors.groupingBy(c -> c.getFechaCreacion().toLocalDate().toString(), Collectors.counting()))
                .forEach((f, t) -> series.getData().add(new XYChart.Data<>(f, t)));

        if (!series.getData().isEmpty()) ventasPorPeriodoChart.getData().add(series);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}