package co.edu.uniquindio.proyectogestioneventos.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.Event;
import java.io.IOException;

public class NavegacionUtil {

    public static void navegar(Event event, String fxmlPath, String titulo) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(NavegacionUtil.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Obtener el Stage actual a partir del evento
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Configurar la nueva escena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.centerOnScreen();
            stage.show();
            
        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + fxmlPath);
            e.printStackTrace();
        }
    }
}