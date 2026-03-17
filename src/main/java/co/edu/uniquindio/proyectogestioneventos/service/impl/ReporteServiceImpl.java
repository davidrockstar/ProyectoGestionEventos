package co.edu.uniquindio.proyectogestioneventos.service.impl;

import co.edu.uniquindio.proyectogestioneventos.model.Compra;
import co.edu.uniquindio.proyectogestioneventos.service.IReporteService;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class ReporteServiceImpl implements IReporteService {

    @Override
    public File generarReporteCSV(List<Compra> compras) throws Exception {
        File file = File.createTempFile("reporte_compras", ".csv");
        try (FileWriter writer = new FileWriter(file)) {
            writer.append("ID Compra,Fecha,Evento,Total,Estado\n");
            for (Compra compra : compras) {
                writer.append(compra.getIdCompra()).append(',')
                        .append(compra.getFechaCreacion().toLocalDate().toString()).append(',')
                        .append(compra.getEvento().getNombre()).append(',')
                        .append(String.valueOf(compra.getTotal())).append(',')
                        .append(compra.getEstadoCompra().toString()).append('\n');
            }
        }
        return file;
    }

    @Override
    public File generarReportePDF(List<Compra> compras) throws Exception {
        // La generación de PDF requiere una librería externa como iText o Apache PDFBox.
        // Por simplicidad, simulamos la creación de un archivo vacío.
        File file = File.createTempFile("reporte_compras", ".pdf");
        System.out.println("Simulando generación de PDF en: " + file.getAbsolutePath());
        // Aquí iría la lógica real con la librería de PDF.
        return file;
    }
}
