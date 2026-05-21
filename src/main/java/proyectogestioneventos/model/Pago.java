package proyectogestioneventos.model;

import proyectogestioneventos.model.enums.EstadoPago;
import java.time.LocalDateTime;

public class Pago {
    private Long idPago;
    private Double monto;
    private LocalDateTime fechaPago;
    private EstadoPago estado;
    private String referenciaTransaccion;
    private String metodoPago;

    public Pago() {
        this.estado = EstadoPago.PENDIENTE;
    }

    public Pago(Long idPago, Double monto, LocalDateTime fechaPago, EstadoPago estado, String referenciaTransaccion, String metodoPago) {
        this.idPago = idPago;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.estado = estado;
        this.referenciaTransaccion = referenciaTransaccion;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters
    public Long getIdPago() {
        return idPago;
    }

    public void setIdPago(Long idPago) {
        this.idPago = idPago;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public String getReferenciaTransaccion() {
        return referenciaTransaccion;
    }

    public void setReferenciaTransaccion(String referenciaTransaccion) {
        this.referenciaTransaccion = referenciaTransaccion;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "idPago=" + idPago +
                ", monto=" + monto +
                ", fechaPago=" + fechaPago +
                ", estado=" + estado +
                ", referenciaTransaccion='" + referenciaTransaccion + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                '}';
    }
}