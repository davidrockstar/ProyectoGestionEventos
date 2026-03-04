package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import javafx.fxml.FXML;

public class AdministradorViewController {

    @FXML
    void onCerrarSesion() {
        MyApplication.goToLogin();
    }
}
