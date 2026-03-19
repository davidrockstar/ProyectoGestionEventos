package co.edu.uniquindio.proyectogestioneventos.model.observer;

/**
 * Interfaz para el patrón Observer (Observador).
 * Define el método que será llamado cuando el sujeto notifique un cambio.
 */
public interface IObservador {
    void actualizar(String mensaje);
}
