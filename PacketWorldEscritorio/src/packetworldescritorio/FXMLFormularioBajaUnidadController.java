package packetworldescritorio;

import packetworldescritorio.modelo.dao.BajasUnidadDAO;
import packetworldescritorio.modelo.dao.Mensaje;
import packetworldescritorio.modelo.dao.UnidadesDAO;
import packetworldescritorio.pojo.BajaUnidad;
import packetworldescritorio.pojo.Unidad;
import packetworldescritorio.utilidades.Alertas;
import packetworldescritorio.utilidades.ControladorPrincipal;
import packetworldescritorio.utilidades.Funciones;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;

public class FXMLFormularioBajaUnidadController implements Initializable, ControladorPrincipal<Unidad> {

    @FXML
    private Label labelID;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private Label labelError;
    @FXML
    private Button btnContinuar;
    @FXML
    private Button btnCancelar;

    private Unidad unidad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTextField(tfDescripcion, Pattern.compile("[a-zA-Z0-9\\s\\.,;:!?()\\-áéíóúÁÉÍÓÚñÑ]{0,255}"));
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
    private void btnContinuar(ActionEvent event) {
        if (verificarCampo()) {
            BajaUnidad baja = new BajaUnidad();
            baja.setVin(unidad.getVin());
            baja.setMotivo(tfDescripcion.getText());
            registrarBaja(baja);
        }
    }

    private void registrarBaja(BajaUnidad baja) {
        Mensaje msj = BajasUnidadDAO.agregarBajaUnidad(baja);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("Registro exitoso.", "La baja de unidad fue registrada correctamente",
                    Alert.AlertType.INFORMATION);
            
            msj = UnidadesDAO.eliminarUnidad(unidad.getVin());

            if (!msj.isError()) {
                Alertas.mostrarAlertaSimple("Unidad eliminado", "El unidad ha sido eliminado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                Alertas.mostrarAlertaSimple("Error al eliminar.", "No se pudo eliminar la unidad, intente nuevamente. ", Alert.AlertType.WARNING);
            }
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Error al guardar", msj.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private boolean verificarCampo() {
        boolean valid = true;
        labelError.setText("");

        if (tfDescripcion.getText().isEmpty() || tfDescripcion.getText() == null) {
            valid = false;
            labelError.setText("Escriba algo como motivo de la baja.");
        } else {
            if (tfDescripcion.getText().length() < 20) {
                valid = false;
                labelError.setText("El motivo es muy corto.");
            }
        }

        return valid;
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        AnchorPane contenerdorPrincipal = (AnchorPane) labelID.getScene().lookup("#contenedorPrincipal");
        Funciones.cargarVista("/clienteescritorio/FXMLUnidades.fxml", contenerdorPrincipal);
    }

    @Override
    public void setDatos(Unidad unidad) {
        this.unidad = unidad;
        if (this.unidad != null) {
            labelID.setText("Motivo de baja de unidad " + this.unidad.getMarca() + " "
                    + this.unidad.getModelo() + " " + this.unidad.getAnio() + ":");

        }
    }

}
