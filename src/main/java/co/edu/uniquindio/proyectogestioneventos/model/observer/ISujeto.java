package co.edu.uniquindio.proyectogestioneventos.model.observer;

/**
 * Interfaz para el patrón Observer (Sujeto).
 * Define los métodos para gestionar observadores.
 */
public interface ISujeto {
    void agregarObservador(IObservador observador);
    void eliminarObservador(IObservador observador);
    void notificarObservadores(String mensaje);
}
