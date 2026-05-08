package co.edu.uniquindio.proyectogestioneventos.viewcontroller;

import co.edu.uniquindio.proyectogestioneventos.model.Usuario;
import co.edu.uniquindio.proyectogestioneventos.model.enums.Rol;
import co.edu.uniquindio.proyectogestioneventos.service.IUsuarioService;
import co.edu.uniquindio.proyectogestioneventos.service.impl.UsuarioServiceImpl;
import co.edu.uniquindio.proyectogestioneventos.util.NavegacionUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.Optional;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    
    private final IUsuarioService usuarioService = new UsuarioServiceImpl();

    @FXML
    void onLoginAction(ActionEvent event) {
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        Optional<Usuario> usuarioOpt = usuarioService.autenticarUsuario(email, pass);

        if (usuarioOpt.isPresent()) {
            Usuario user = usuarioOpt.get();
            
            if (user.getRol() == Rol.ADMINISTRADOR) {
                // Flujo Admin
                NavegacionUtil.navegar(event, "/co/edu/uniquindio/proyectogestioneventos/usuario/administrador/dashboard-admin.fxml", "Panel de Administración");
            } else {
                // Flujo Usuario
                NavegacionUtil.navegar(event, "/co/edu/uniquindio/proyectogestioneventos/usuario/cliente/dashboard-usuario.fxml", "Mi Dashboard");
            }
        } else {
            System.out.println("Credenciales incorrectas");
            // Aquí podrías mostrar una alerta JavaFX
        }
    }
}