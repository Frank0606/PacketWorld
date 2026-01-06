package packetworldescritorio;

import packetworldescritorio.modelo.dao.IniciarSesionDAO;
import packetworldescritorio.pojo.IniciarSesion;
import packetworldescritorio.utilidades.Alertas;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tfNoPersonal;
    @FXML
    private PasswordField tfContrasena;
    @FXML
    private Button btnIngresar;
    @FXML
    private Label labelErrorNoPersonal;
    @FXML
    private Label labelErrorContrasena;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTextField(tfNoPersonal, Pattern.compile("[empEMP0-9]{0,6}"));
        tfNoPersonal.textProperty().addListener((obs, old, neu) -> tfNoPersonal.setText(neu.toUpperCase()));
        configurarTextField(tfContrasena, Pattern.compile(".{0,50}"));
    }

    private void configurarTextField(TextField textField, Pattern pattern) {
        TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
            String newText = change.getControlNewText();
            if (pattern.matcher(newText).matches()) {
                return change;
            } else {
                return null;
            }
        });
        textField.setTextFormatter(formatter);
    }

    @FXML
    private void clickIniciarSesion(ActionEvent event) {
        String noPersonal = tfNoPersonal.getText();
        String contrasenia = tfContrasena.getText();

        if (validarCamposIniciarSesion(noPersonal, contrasenia)) {
            verificarCredenciales(noPersonal, contrasenia);
        }
    }

    private boolean validarCamposIniciarSesion(String noPersonal, String contrasenia) {
        boolean valido = true;
        labelErrorNoPersonal.setText("");
        labelErrorContrasena.setText("");

        if (noPersonal.length() < 6 || noPersonal.length() > 6 || !noPersonal.startsWith("EMP")
                || noPersonal.isEmpty()) {
            valido = false;
            labelErrorNoPersonal.setText("No. de personal no valido");
        }
        if (contrasenia.isEmpty()) {
            valido = false;
            labelErrorContrasena.setText("Contraseña no valida");
        }

        return valido;
    }

    private void verificarCredenciales(String noPersonal, String contrasenia) {
        IniciarSesion respuestaIniciarSesion = IniciarSesionDAO.iniciarSesion(noPersonal, contrasenia);
        if (!respuestaIniciarSesion.getError()) {
            Alertas.mostrarAlertaSimple("¡Credenciales correctas!",
                    "Bienvenido " + respuestaIniciarSesion.getColaborador().getNombre(),
                    Alert.AlertType.INFORMATION);
            cambiarPantallaPrincipal(noPersonal);
        } else {
            Alertas.mostrarAlertaSimple("Problema", respuestaIniciarSesion.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void cambiarPantallaPrincipal(String noPersonal) {
        try {
            Stage escenarioBase = (Stage) tfNoPersonal.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPrincipal.fxml"));
            Parent principal = loader.load();
            FXMLPrincipalController controlador = loader.getController();
            controlador.setNoPersonal(noPersonal);
            Scene escenaPrincipal = new Scene(principal);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Sistema - Principal");
            escenarioBase.show();
        } catch (IOException ex) {
            Alertas.mostrarAlertaSimple("Error", "En este momento no se pudo ingresar a la pantalla inicial", Alert.AlertType.ERROR);
        }
    }

}
