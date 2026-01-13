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
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import packetworldescritorio.modelo.dao.EnvioHistorialDAO;
import packetworldescritorio.modelo.dao.EstadosEnvioDAO;
import packetworldescritorio.pojo.EnvioHistorial;
import packetworldescritorio.pojo.EstadosEnvio;

public class FXMLFormularioCambiarEstatusController implements Initializable, ControladorPrincipal<Envio> {

    @FXML
    private ComboBox<EstadosEnvio> cbEstadoNuevo;
    @FXML
    private Button btnAsignar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label tfEnvio;
    @FXML
    private Label labelEstadoActual;

    private List<EstadosEnvio> estados;
    private Envio envio;

    @FXML
    private Label labelErrorEstadoNuevo;
    @FXML
    private TextArea taComentario;
    @FXML
    private Label labelErrorComentario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEstados();
        configurarTextField(taComentario, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ\\s]{0,255}"));
        taComentario.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                taComentario.setText(primerCaracter + resto);
            }
        });
    }

    private void configurarTextField(TextArea textField, Pattern pattern) {
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

    private void cargarInformacion() {
        labelEstadoActual.setText(EstadosEnvioDAO.obtenerPorId(envio.getIdEstadosEnvio()).getEstadoEnvio());
        tfEnvio.setText(this.envio.getNumeroGuia());
    }

    private void cargarEstados() {
        estados = EstadosEnvioDAO.obtenerTodos();

        List<EstadosEnvio> estadosFiltrados = new ArrayList<>();
        for (EstadosEnvio estado : estados) {
            String nombre = estado.getEstadoEnvio().toLowerCase();
            if (!nombre.equals("procesado") && !nombre.equals("recibido en sucursal")) {
                estadosFiltrados.add(estado);
            }
        }

        ObservableList<EstadosEnvio> estadoTemp = FXCollections.observableArrayList(estadosFiltrados);
        cbEstadoNuevo.setItems(estadoTemp);
    }

    @FXML
    private void btnCambiar(ActionEvent event) {
        if (validarCampos()) {
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String fecha = ahora.format(formato);

            EnvioHistorial envio = new EnvioHistorial();

            envio.setIdEstadosEnvio(cbEstadoNuevo.getSelectionModel().getSelectedItem().getIdEstadosEnvio());
            envio.setIdColaborador(this.envio.getIdColaborador());
            envio.setFechaCambio(fecha);
            envio.setIdEnvio(this.envio.getIdEnvio());
            envio.setComentario(taComentario.getText());

            Mensaje msj = EnvioHistorialDAO.registrar(envio);

            if (!msj.isError()) {
                Alertas.mostrarAlertaSimple("Actualizacion de estatus exitoso", "Se cambio el estatus del envio a "
                        + cbEstadoNuevo.getSelectionModel().getSelectedItem().getEstadoEnvio().toLowerCase() + " exitosamente.", Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Alertas.mostrarAlertaSimple("Problema en la actualizacion",
                        "No se pudo realizar el cambio de estatus. Intente de nuevo mas tarde.\n" + msj.getMensaje(), Alert.AlertType.ERROR);
            }
        } else {
            Alertas.mostrarAlertaSimple("Error en los campos", "Tienes campos con errores. Corrigelos e intenta de nuevo.",
                    Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos() {
        boolean valid = true;
        labelErrorEstadoNuevo.setText("");
        labelErrorComentario.setText("");
        EstadosEnvio estadoSeleccionado = cbEstadoNuevo.getSelectionModel().getSelectedItem();
        if (estadoSeleccionado == null) {
            valid = false;
            labelErrorEstadoNuevo.setText("Selecciona un estado.");
        } else {
            String nombreEstado = estadoSeleccionado.getEstadoEnvio().toLowerCase();
            if ((nombreEstado.equals("detenido") || nombreEstado.equals("cancelado"))
                    && (taComentario.getText() == null || taComentario.getText().trim().isEmpty())) {
                valid = false;
                labelErrorComentario.setText("El comentario es obligatorio para estados detenidos o cancelados.");
            }
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
