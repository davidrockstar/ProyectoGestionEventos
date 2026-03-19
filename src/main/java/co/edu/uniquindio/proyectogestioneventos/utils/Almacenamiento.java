package co.edu.uniquindio.proyectogestioneventos.utils;

public class Almacenamiento {
    private static Almacenamiento instance;

    private Almacenamiento() {
        // Cargar datos iniciales
        DatosIniciales.cargar();
    }

    public static Almacenamiento getInstance() {
        if (instance == null) {
            instance = new Almacenamiento();
        }
        return instance;
    }
}
