package packetworldescritorio;

import packetworldescritorio.modelo.dao.EnviosDAO;
import packetworldescritorio.modelo.dao.Mensaje;
import packetworldescritorio.modelo.dao.PaquetesDAO;
import packetworldescritorio.pojo.Envio;
import packetworldescritorio.pojo.Paquete;
import packetworldescritorio.utilidades.Alertas;
import packetworldescritorio.utilidades.ControladorPrincipal;
import packetworldescritorio.utilidades.Funciones;
import java.net.URL;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import packetworldescritorio.modelo.dao.EstadosDAO;
import packetworldescritorio.modelo.dao.EstadosEnvioDAO;

public class FXMLFormularioPaquetesController implements Initializable, ControladorPrincipal<Paquete> {

    @FXML
    private TextArea tfDescripcion;
    @FXML
    private TextField tfPeso;
    @FXML
    private TextField tfProfundidad;
    @FXML
    private TextField tfAlto;
    @FXML
    private TextField tfAncho;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label labelErrorDescripcion;
    @FXML
    private Label labelErrorPeso;
    @FXML
    private Label labelErrorProfundidad;
    @FXML
    private Label labelErrorAlto;
    @FXML
    private Label labelErrorAncho;
    @FXML
    private Label labelErrorIdEnvio;

    private Paquete paquete;
    private ObservableList<Envio> listaObservableEnvios;

    @FXML
    private ComboBox<Envio> cbIdEnvios;
    @FXML
    private Label lblDetallesEnvio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEnvios();
        cargarEventosCb();
        configurarTextField(tfDescripcion, Pattern.compile("[a-zA-Z0-9\\.,;:!?()\\- ]{0,255}"));
        configurarTextField(tfPeso, Pattern.compile("\\d{0,10}([\\.]\\d{0,2})?"));
        configurarTextField(tfProfundidad, Pattern.compile("\\d{0,10}([\\.]\\d{0,2})?"));
        configurarTextField(tfAlto, Pattern.compile("\\d{0,10}([\\.]\\d{0,2})?"));
        configurarTextField(tfAncho, Pattern.compile("\\d{0,10}([\\.]\\d{0,2})?"));
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

    private void cargarEventosCb() {
        cbIdEnvios.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblDetallesEnvio.setText(
                        "Cliente: " + newVal.getNombreDestinatario() + "\nDestino: "
                        + newVal.getCalleDestino() + " #" + newVal.getNumeroDestino() + "\n"
                        + newVal.getColoniaDestino() + " " + newVal.getCpDestino() + "\n"
                        + newVal.getCiudadDestino() + ", " + EstadosDAO.obtenerEstado(newVal.getIdEstadoDestino())
                );
            }
        });
    }

    @FXML
    private void btnGuardar(ActionEvent event) {
        if (validarCampos()) {
            if (this.paquete == null) {
                this.paquete = new Paquete();
            }

            int idEnvio = ((cbIdEnvios.getSelectionModel().getSelectedItem() != null) ? cbIdEnvios.getSelectionModel().getSelectedItem().getIdEnvio() : null);

            paquete.setDescripcion(tfDescripcion.getText());
            paquete.setPeso(Float.parseFloat(tfPeso.getText()));
            paquete.setProfundidad(Float.parseFloat(tfProfundidad.getText()));
            paquete.setAlto(Float.parseFloat(tfAlto.getText()));
            paquete.setAncho(Float.parseFloat(tfAncho.getText()));
            paquete.setNumeroGuia(cbIdEnvios.getSelectionModel().getSelectedItem().getNumeroGuia());
            paquete.setIdEnvio(idEnvio);

            if (btnGuardar.getText().equals("Editar")) {
                editarDatosPaquete();
            } else {
                guardarDatosPaquete();
            }
        } else {
            Alertas.mostrarAlertaSimple("Problema con los datos", "Tiene datos incorrectos, revise e intente de nuevo.",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void editarDatosPaquete() {
        Mensaje msj = PaquetesDAO.actualizarPaquete(paquete);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("Cambio exitoso.", "La información del paquete fue actualizada "
                    + "correctamente", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Campos obligatorios.", "Verifica los campos de tu formulario.", Alert.AlertType.WARNING);
        }
    }

    private boolean validarCampos() {

        boolean valid = true;

        labelErrorDescripcion.setText("");
        labelErrorPeso.setText("");
        labelErrorProfundidad.setText("");
        labelErrorAlto.setText("");
        labelErrorAncho.setText("");
        labelErrorIdEnvio.setText("");

        String descripcion = tfDescripcion.getText() != null ? tfDescripcion.getText().trim() : "";

        if (descripcion.isEmpty()) {
            labelErrorDescripcion.setText("Descripción vacía");
            valid = false;
        } else if (descripcion.length() > 255) {
            labelErrorDescripcion.setText("Descripción demasiado larga (máximo 255 caracteres)");
            valid = false;
        }

        String pesoText = tfPeso.getText() != null ? tfPeso.getText().trim() : "";

        if (pesoText.isEmpty()) {
            labelErrorPeso.setText("Peso vacío");
            valid = false;
        } else if (!pesoText.matches("\\d+(\\.\\d{1,2})?")) {
            labelErrorPeso.setText("Peso debe ser un número válido con hasta 2 decimales");
            valid = false;
        } else if (pesoText.length() > 10) {
            labelErrorPeso.setText("Peso demasiado largo");
            valid = false;
        }

        String profundidadText = tfProfundidad.getText() != null ? tfProfundidad.getText().trim() : "";

        if (profundidadText.isEmpty()) {
            labelErrorProfundidad.setText("Profundidad vacía");
            valid = false;
        } else if (!profundidadText.matches("\\d+(\\.\\d{1,2})?")) {
            labelErrorProfundidad.setText("Profundidad debe ser un número válido con hasta 2 decimales");
            valid = false;
        } else if (profundidadText.length() > 10) {
            labelErrorProfundidad.setText("Profundidad demasiado larga");
            valid = false;
        }

        String altoText = tfAlto.getText() != null ? tfAlto.getText().trim() : "";

        if (altoText.isEmpty()) {
            labelErrorAlto.setText("Alto vacío");
            valid = false;
        } else if (!altoText.matches("\\d+(\\.\\d{1,2})?")) {
            labelErrorAlto.setText("Alto debe ser un número válido con hasta 2 decimales");
            valid = false;
        } else if (altoText.length() > 10) {
            labelErrorAlto.setText("Alto demasiado largo");
            valid = false;
        }

        String anchoText = tfAncho.getText() != null ? tfAncho.getText().trim() : "";

        if (anchoText.isEmpty()) {
            labelErrorAncho.setText("Ancho vacío");
            valid = false;
        } else if (!anchoText.matches("\\d+(\\.\\d{1,2})?")) {
            labelErrorAncho.setText("Ancho debe ser un número válido con hasta 2 decimales");
            valid = false;
        } else if (anchoText.length() > 10) {
            labelErrorAncho.setText("Ancho demasiado largo");
            valid = false;
        }

        if (cbIdEnvios.getValue() == null) {
            labelErrorIdEnvio.setText("Seleccione un ID de envío");
            valid = false;
        }

        return valid;
    }

    private void guardarDatosPaquete() {
        Mensaje msj = PaquetesDAO.agregarPaquete(paquete);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("Registro exitoso.", "La información del paquete fue registrada correctamente",
                    Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Error al guardar", msj.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void cargarDatos() {
        tfDescripcion.setText(this.paquete.getDescripcion());
        tfPeso.setText(String.valueOf(this.paquete.getPeso()));
        tfProfundidad.setText(String.valueOf(this.paquete.getProfundidad()));
        tfAlto.setText(String.valueOf(this.paquete.getAlto()));
        tfAncho.setText(String.valueOf(this.paquete.getAncho()));
        int posicion = buscarIdEnvio(this.paquete.getIdEnvio());
        cbIdEnvios.getSelectionModel().select(posicion);
    }

    private void cerrarVentana() {
        AnchorPane contenerdorPrincipal = (AnchorPane) tfPeso.getScene().lookup("#contenedorPrincipal");
        Funciones.cargarVista("/packetworldescritorio/FXMLPaquetes.fxml", contenerdorPrincipal);
    }

    private void cargarEnvios() {
        List<Envio> envios = EnviosDAO.obtenerEnvios();
        envios.removeIf(envio -> !EstadosEnvioDAO.obtenerPorId(envio.getIdEstadosEnvio()).getEstadoEnvio().equals("recibido en sucursal"));
        if (envios != null && !envios.isEmpty()) {
            listaObservableEnvios = FXCollections.observableArrayList(envios);
            cbIdEnvios.setItems(listaObservableEnvios);
        }
    }

    private int buscarIdEnvio(int idEnvio) {
        for (int i = 0; i < listaObservableEnvios.size(); i++) {
            if (listaObservableEnvios.get(i).getIdEnvio() == idEnvio) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void setDatos(Paquete paquete) {
        this.paquete = paquete;
        if (paquete != null) {
            cargarDatos();
            cbIdEnvios.setDisable(true);
            btnGuardar.setText("Editar");
        } else {
            btnGuardar.setText("Agregar");
        }
    }

}
