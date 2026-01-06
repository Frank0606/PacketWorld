package packetworldescritorio;

import packetworldescritorio.modelo.dao.EnviosDAO;
import packetworldescritorio.modelo.dao.Mensaje;
import packetworldescritorio.pojo.Envio;
import packetworldescritorio.utilidades.Alertas;
import packetworldescritorio.utilidades.ControladorPrincipal;
import packetworldescritorio.utilidades.Funciones;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FXMLFormularioCambiarEstatusController implements Initializable, ControladorPrincipal<Envio> {

    @FXML
    private ComboBox<String> cbEstadoNuevo;
    @FXML
    private Button btnAsignar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField tfEnvio;
    @FXML
    private Label labelEstadoActual;

    private List<String> estados;
    private Envio envio;

    @FXML
    private Label labelErrorEstadoNuevo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEstados();
    }

    private void cargarInformacion() {
        labelEstadoActual.setText(this.envio.getEstatus());
        tfEnvio.setDisable(true);
        tfEnvio.setText(this.envio.getNumeroGuia());
    }

    private void cargarEstados() {
        estados = new ArrayList();
        estados.add("Pendiente");
        estados.add("En tránsito");
        estados.add("Detenido");
        estados.add("Entregado");
        estados.add("Cancelado");
        ObservableList<String> estadoTemp = FXCollections.observableArrayList(estados);
        cbEstadoNuevo.setItems(estadoTemp);
    }

    @FXML
    private void btnCambiar(ActionEvent event) {
        if (validarCampos()) {
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
            String fecha = ahora.format(formato);
            String noPersonalColaborador = envio.getCalleDestino();
            String nuevoEstado = cbEstadoNuevo.getSelectionModel().getSelectedItem().toUpperCase();
            Envio envio = new Envio();
            envio.setEstatus(nuevoEstado);
            envio.setNumeroGuia(this.envio.getNumeroGuia());
            //envio.setHistorialEstados(this.envio.getHistorialEstados() + nuevoEstado + "_" + fecha + "_"
            //        + noPersonalColaborador + ":");

            Mensaje msj = EnviosDAO.cambiarEstatusEnvio(envio);

            if (!msj.isError()) {
                Alertas.mostrarAlertaSimple("Actualizacion de estatus exitoso", "Se cambio el estatus del envio a "
                        + nuevoEstado.toLowerCase() + " exitosamente.", Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Alertas.mostrarAlertaSimple("Problema en la actualizacion",
                        "No se pudo realizar el cambio de estatus. Intente de nuevo mas tarde.", Alert.AlertType.ERROR);
            }
        } else {
            Alertas.mostrarAlertaSimple("Error en los campos", "Tienes campos con errores. Corrigelos e intenta de nuevo.",
                    Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos() {
        boolean valid = true;
        labelErrorEstadoNuevo.setText("");

        if (cbEstadoNuevo.getSelectionModel().getSelectedItem().isEmpty()
                || cbEstadoNuevo.getSelectionModel().getSelectedItem() == null) {
            valid = false;
            labelErrorEstadoNuevo.setText("Selecciona un estado.");
        }

        return valid;
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        AnchorPane contenerdorPrincipal = (AnchorPane) cbEstadoNuevo.getScene().lookup("#contenedorPrincipal");
        Funciones.cargarVista("/packetworldescritorio/FXMLEnvios.fxml", contenerdorPrincipal);
    }

    @Override
    public void setDatos(Envio envio) {
        if (envio == null) {
            this.envio = new Envio();
        } else {
            this.envio = envio;
        }
        cargarInformacion();
    }

}
