package co.edu.uniquindio.proyectogestioneventos.controller;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import co.edu.uniquindio.proyectogestioneventos.service.IReporteService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.ReporteServiceImpl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReportesController {

    private final IReporteService reporteService = new ReporteServiceImpl();
    private ObservableList<Compra> comprasParaReporte;

    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaFin;

    public void setCompras(ObservableList<Compra> compras) {
        this.comprasParaReporte = compras;
    }

    @FXML
    void onDescargarCSVClick(ActionEvent event) {
        List<Compra> comprasFiltradas = filtrarComprasPorFecha();

        if (comprasFiltradas.isEmpty()) {
            mostrarAlerta("Advertencia", "No hay compras para generar el reporte en el rango de fechas seleccionado.", Alert.AlertType.WARNING);
            return;
        }
        try {
            File csvFile = reporteService.generarReporteCSV(comprasFiltradas);
            mostrarAlerta("Reporte Generado", "Reporte CSV guardado en: " + csvFile.getAbsolutePath(), Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al generar el reporte CSV: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onDescargarPDFClick(ActionEvent event) {
        List<Compra> comprasFiltradas = filtrarComprasPorFecha();
        if (comprasFiltradas.isEmpty()) {
            mostrarAlerta("Advertencia", "No hay compras para generar el reporte en el rango de fechas seleccionado.", Alert.AlertType.WARNING);
            return;
        }
        try {
            File pdfFile = reporteService.generarReportePDF(comprasFiltradas);
            mostrarAlerta("Reporte Generado", "Reporte PDF guardado en: " + pdfFile.getAbsolutePath(), Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al generar el reporte PDF: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private List<Compra> filtrarComprasPorFecha() {
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        if (comprasParaReporte == null || comprasParaReporte.isEmpty()) {
            return List.of();
        }

        return comprasParaReporte.stream()
                .filter(compra -> {
                    LocalDate fechaCompra = compra.getFechaCreacion().toLocalDate();
                    boolean cumpleInicio = (fechaInicio == null || !fechaCompra.isBefore(fechaInicio));
                    boolean cumpleFin = (fechaFin == null || !fechaCompra.isAfter(fechaFin));
                    return cumpleInicio && cumpleFin;
                })
                .collect(Collectors.toList());
    }

    @FXML
    void onVolverClick(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
