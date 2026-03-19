package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Random;

public class PanelMetricasAdminViewController {

    @FXML
    private ComboBox<String> cbRangoFechas;
    @FXML
    private LineChart<String, Number> ventasPorPeriodoChart;
    @FXML
    private CategoryAxis ventasPeriodoXAxis;
    @FXML
    private NumberAxis ventasPeriodoYAxis;
    @FXML
    private BarChart<String, Number> ocupacionPorZonaChart;
    @FXML
    private CategoryAxis ocupacionZonaXAxis;
    @FXML
    private NumberAxis ocupacionZonaYAxis;
    @FXML
    private PieChart ingresosServiciosAdicionalesChart;
    @FXML
    private Label lblTasaCancelacion;
    @FXML
    private BarChart<String, Number> topEventosChart;
    @FXML
    private CategoryAxis topEventosXAxis;
    @FXML
    private NumberAxis topEventosYAxis;

    @FXML
    private void initialize() {
        cbRangoFechas.setItems(FXCollections.observableArrayList("Últimos 7 días", "Últimos 30 días", "Últimos 3 meses", "Último año"));
        cbRangoFechas.getSelectionModel().selectFirst(); // Seleccionar el primer elemento por defecto
        actualizarMetricas();
    }

    @FXML
    void onActualizarMetricasClick(ActionEvent event) {
        actualizarMetricas();
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) cbRangoFechas.getScene().getWindow();
        stage.close();
    }

    private void actualizarMetricas() {
        // Datos simulados
        Random random = new Random();

        // 1. Ventas por periodo (LineChart)
        ventasPorPeriodoChart.getData().clear();
        XYChart.Series<String, Number> ventasSeries = new XYChart.Series<>();
        ventasSeries.setName("Ventas");
        String[] periodos = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        for (String periodo : periodos) {
            ventasSeries.getData().add(new XYChart.Data<>(periodo, random.nextInt(100) + 50));
        }
        ventasPorPeriodoChart.getData().add(ventasSeries);

        // 2. Ocupación por zona (BarChart)
        ocupacionPorZonaChart.getData().clear();
        XYChart.Series<String, Number> ocupacionSeries = new XYChart.Series<>();
        ocupacionSeries.setName("Ocupación (%)");
        String[] zonas = {"VIP", "General", "Platea", "Palco"};
        for (String zona : zonas) {
            ocupacionSeries.getData().add(new XYChart.Data<>(zona, random.nextInt(100)));
        }
        ocupacionPorZonaChart.getData().add(ocupacionSeries);

        // 3. Ingresos por servicios adicionales (PieChart)
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Parqueadero", random.nextInt(500) + 100),
                new PieChart.Data("Comida", random.nextInt(500) + 100),
                new PieChart.Data("Merchandising", random.nextInt(500) + 100),
                new PieChart.Data("Otros", random.nextInt(200) + 50)
        );
        ingresosServiciosAdicionalesChart.setData(pieChartData);

        // 4. Tasa de cancelación (Label)
        lblTasaCancelacion.setText(String.format("%.2f%%", random.nextDouble() * 10));

        // 5. Top eventos (BarChart)
        topEventosChart.getData().clear();
        XYChart.Series<String, Number> topEventosSeries = new XYChart.Series<>();
        topEventosSeries.setName("Ventas");
        String[] eventos = {"Concierto Rock", "Festival Jazz", "Obra Teatro", "Partido Fútbol"};
        for (String evento : eventos) {
            topEventosSeries.getData().add(new XYChart.Data<>(evento, random.nextInt(1000) + 200));
        }
        topEventosChart.getData().add(topEventosSeries);
    }
}
