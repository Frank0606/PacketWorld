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
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;

public class FXMLFormularioPaquetesController implements Initializable, ControladorPrincipal<Paquete> {

    @FXML
    private TextField tfDescripcion;
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
    private Label labelErrorIdPaquete;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEnvios();
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

        // Validar descripción
        if (tfDescripcion.getText() == null || tfDescripcion.getText().trim().isEmpty()
                || tfDescripcion.getText().length() > 255) {
            labelErrorDescripcion.setText("Descripción inválida");
            valid = false;
        }

        // Validar peso
        try {
            Double peso = Double.valueOf(tfPeso.getText());
            if (peso == null || tfPeso.getText().trim().isEmpty()
                    || tfPeso.getText().length() > 10 || !tfPeso.getText().matches("\\d+(\\.\\d{1,2})?")) {
                labelErrorPeso.setText("Peso inválido");
                valid = false;
            }
        } catch (NumberFormatException e) {
            labelErrorPeso.setText("Peso debe ser un número válido con hasta 2 decimales");
            valid = false;
        }

        // Validar profundidad
        try {
            Double profundidad = Double.valueOf(tfProfundidad.getText());
            if (profundidad == null || tfProfundidad.getText().trim().isEmpty()
                    || tfProfundidad.getText().length() > 10 || !tfProfundidad.getText().matches("\\d+(\\.\\d{1,2})?")) {
                labelErrorProfundidad.setText("Profundidad inválida");
                valid = false;
            }
        } catch (NumberFormatException e) {
            labelErrorProfundidad.setText("Profundidad debe ser un número válido con hasta 2 decimales");
            valid = false;
        }

        // Validar alto
        try {
            Double alto = Double.valueOf(tfAlto.getText());
            if (alto == null || tfAlto.getText().trim().isEmpty()
                    || tfAlto.getText().length() > 10 || !tfAlto.getText().matches("\\d+(\\.\\d{1,2})?")) {
                labelErrorAlto.setText("Alto inválido");
                valid = false;
            }
        } catch (NumberFormatException e) {
            labelErrorAlto.setText("Alto debe ser un número válido con hasta 2 decimales");
            valid = false;
        }

        // Validar ancho
        try {
            Double ancho = Double.valueOf(tfAncho.getText());
            if (ancho == null || tfAncho.getText().trim().isEmpty()
                    || tfAncho.getText().length() > 10 || !tfAncho.getText().matches("\\d+(\\.\\d{1,2})?")) {
                labelErrorAncho.setText("Ancho inválido");
                valid = false;
            }
        } catch (NumberFormatException e) {
            labelErrorAncho.setText("Ancho debe ser un número válido con hasta 2 decimales");
            valid = false;
        }

        // Validar id del paquete
        if (cbIdEnvios.getValue() == null) {
            labelErrorIdEnvio.setText("Seleccione un ID de envío válido");
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
        if (envios != null && !envios.isEmpty()) {
            listaObservableEnvios = FXCollections.observableArrayList(envios);
            cbIdEnvios.setItems(listaObservableEnvios);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar", "Lo sentiento, no se pudo obtener la informacion de Roles",
                    Alert.AlertType.ERROR);
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
