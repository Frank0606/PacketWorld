package packetworldescritorio;

import packetworldescritorio.utilidades.Alertas;
import packetworldescritorio.utilidades.Funciones;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLPrincipalController implements Initializable {

    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Button btnColaboradores;
    @FXML
    private Button btnClientes;
    @FXML
    private Button btnUnidades;
    @FXML
    private Button btnEnvios;
    @FXML
    private Button btnPaquetes;
    @FXML
    private AnchorPane contenedorPrincipal;

    private String noPersonalColaborador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnColaboradores.setOnAction(e -> Funciones.cargarVista("/packetworldescritorio/FXMLColaboradores.fxml", contenedorPrincipal));
        btnClientes.setOnAction(e -> Funciones.cargarVista("/packetworldescritorio/FXMLClientes.fxml", contenedorPrincipal));
        btnUnidades.setOnAction(e -> Funciones.cargarVista("/packetworldescritorio/FXMLUnidades.fxml", contenedorPrincipal));
        btnEnvios.setOnAction(e -> Funciones.cargarVista("/packetworldescritorio/FXMLEnvios.fxml", contenedorPrincipal, noPersonalColaborador));
        btnPaquetes.setOnAction(e -> Funciones.cargarVista("/packetworldescritorio/FXMLPaquetes.fxml", contenedorPrincipal));
        btnCerrarSesion.setOnAction(e -> cerrarSesion());
    }

    private void cerrarSesion() {
        Alertas.mostrarAlertaSimple("Cerrar sesión", "¿Estas seguro de cerrar sesión y salir?", Alert.AlertType.CONFIRMATION);
        Funciones.cargarStage("/packetworldescritorio/FXMLInicioSesion.fxml", (Stage) btnPaquetes.getScene().getWindow());
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonalColaborador = noPersonal;
    }
}
