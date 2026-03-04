package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.MyApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class UsuarioViewController {

    @FXML
    void onCerrarSesion() {
        MyApplication.goToLogin();
    }
}
