package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.util.NavegacionUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardUsuarioController {

    @FXML
    void onExplorarEventos(ActionEvent event) {
        NavegacionUtil.navegar(event, "/co/edu/uniquindio/proyectogestioneventos/usuario/cliente/explorar-eventos.fxml", "Explorar Eventos");
    }

    @FXML
    void onVerHistorial(ActionEvent event) {
        NavegacionUtil.navegar(event, "/co/edu/uniquindio/proyectogestioneventos/usuario/cliente/historial-compras.fxml", "Mi Historial");
    }

    @FXML
    void onVerPerfil(ActionEvent event) {
        NavegacionUtil.navegar(event, "/co/edu/uniquindio/proyectogestioneventos/usuario/cliente/perfil.fxml", "Mi Perfil");
    }
    
    @FXML
    void onCerrarSesion(ActionEvent event) {
        NavegacionUtil.navegar(event, "/co/edu/uniquindio/proyectogestioneventos/login.fxml", "Login");
    }
}