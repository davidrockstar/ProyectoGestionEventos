package proyectogestioneventos.controller;

import proyectogestioneventos.model.Compra;
import proyectogestioneventos.model.Taquilla;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import java.util.Map;
import java.util.stream.Collectors;

public class MetricasController {

    @FXML private PieChart chartVentasPorEvento;
    @FXML private BarChart<String, Number> chartIngresos;

    @FXML
    public void initialize() {
        cargarMetricas();
    }

    private void cargarMetricas() {
        // 1. Distribución de Ventas (PieChart)
        Map<String, Long> ventas = Taquilla.getInstance().getCompras().stream()
                .collect(Collectors.groupingBy(c -> c.getEvento().getNombre(), Collectors.counting()));

        chartVentasPorEvento.getData().addAll(ventas.entrySet().stream()
                .map(e -> new PieChart.Data(e.getKey(), e.getValue()))
                .collect(Collectors.toList()));

        // 2. Ingresos Totales por Evento (BarChart)
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Recaudación ($)");

        Taquilla.getInstance().getCompras().stream()
                .collect(Collectors.groupingBy(c -> c.getEvento().getNombre(), Collectors.summingDouble(Compra::getPrecioTotal)))
                .forEach((nombre, total) -> series.getData().add(new XYChart.Data<>(nombre, total)));

        chartIngresos.getData().add(series);
    }
}