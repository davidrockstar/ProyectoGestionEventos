package co.edu.uniquindio.proyectogestioneventos.service;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import java.io.File;
import java.util.List;

public interface IReporteService {
    /**
     * RF-011: Genera un reporte de compras en formato CSV.
     */
    File generarReporteCSV(List<Compra> compras) throws Exception;

    /**
     * RF-011: Genera un reporte de compras en formato PDF.
     */
    File generarReportePDF(List<Compra> compras) throws Exception;
}
