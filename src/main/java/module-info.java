module co.edu.uniquindio.proyectogestioneventos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens co.edu.uniquindio.proyectogestioneventos to javafx.fxml;
    exports co.edu.uniquindio.proyectogestioneventos;
    exports co.edu.uniquindio.proyectogestioneventos.viewcontroller;
    opens co.edu.uniquindio.proyectogestioneventos.viewcontroller to javafx.fxml;
    exports co.edu.uniquindio.proyectogestioneventos.model;
    opens co.edu.uniquindio.proyectogestioneventos.model to javafx.fxml;
    exports co.edu.uniquindio.proyectogestioneventos.controller;
    opens co.edu.uniquindio.proyectogestioneventos.controller to javafx.fxml;
    exports co.edu.uniquindio.proyectogestioneventos.model.enums;
}