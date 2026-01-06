package packetworldescritorio;

import packetworldescritorio.modelo.dao.Mensaje;
import packetworldescritorio.modelo.dao.TiposUnidadesDAO;
import packetworldescritorio.modelo.dao.UnidadesDAO;
import packetworldescritorio.pojo.TipoUnidad;
import packetworldescritorio.pojo.Unidad;
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

public class FXMLFormularioUnidadController implements Initializable, ControladorPrincipal<Unidad> {

    @FXML
    private TextField tfMarca;
    @FXML
    private TextField tfModelo;
    @FXML
    private TextField tfAnio;
    @FXML
    private TextField tfVin;
    @FXML
    private ComboBox<TipoUnidad> cbTipoUnidad;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label labelErrorMarca;
    @FXML
    private Label labelErrorModelo;
    @FXML
    private Label labelErrorAnio;
    @FXML
    private Label labelErrorVin;
    @FXML
    private Label labelErrorTipoUnidad;

    private Unidad unidad;
    private ObservableList<TipoUnidad> listaObservableTipoUnidad;
    @FXML
    private Label labelNoIdentificacion;
    @FXML
    private Label labelNoId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTipoUnidad();
        configurarTextField(tfMarca, Pattern.compile("[a-zA-Z]{0,25}"));
        configurarTextField(tfModelo, Pattern.compile("[a-zA-Z0-9]{0,25}"));
        configurarTextField(tfAnio, Pattern.compile("[0-9]{0,4}"));
        configurarTextField(tfVin, Pattern.compile("[a-zA-Z0-9]{0,17}"));
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
            if (this.unidad == null) {
                this.unidad = new Unidad();
            }

            String noIdentificacion = tfAnio.getText() + tfVin.getText().substring(0, 4);

            Integer idTipoUnidad = ((cbTipoUnidad.getSelectionModel().getSelectedItem() != null) ? cbTipoUnidad.getSelectionModel().getSelectedItem().getIdTipoUnidad() : null);

            unidad.setMarca(tfMarca.getText().toUpperCase());
            unidad.setModelo(tfModelo.getText().toUpperCase());
            unidad.setAnio(Integer.parseInt(tfAnio.getText()));
            unidad.setVin(tfVin.getText().toUpperCase());
            unidad.setNoIdentificacion(noIdentificacion.toUpperCase());
            unidad.setIdTipoUnidad(idTipoUnidad);

            if (btnGuardar.getText().equals("Editar")) {
                editarDatosUnidad();
            } else {
                guardarDatosUnidad();
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

    private void editarDatosUnidad() {
        Mensaje msj = UnidadesDAO.actualizarUnidad(unidad);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("Cambio exitoso.", "La información del unidad fue actualizada "
                    + "correctamente", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Campos obligatorios.", "Verifica los campos de tu formulario.", Alert.AlertType.WARNING);
        }
    }

    private boolean validarCampos() {
        boolean valid = true;
        int anioActual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        labelErrorMarca.setText("");
        labelErrorModelo.setText("");
        labelErrorAnio.setText("");
        labelErrorVin.setText("");
        labelErrorTipoUnidad.setText("");

        // Validar marca
        if (tfMarca.getText() == null || tfMarca.getText().trim().isEmpty()
                || tfMarca.getText().length() > 50) {
            labelErrorMarca.setText("Marca inválida");
            valid = false;
        }

        // Validar modelo
        if (tfModelo.getText() == null || tfModelo.getText().trim().isEmpty()
                || tfModelo.getText().length() > 50) {
            labelErrorModelo.setText("Modelo inválido");
            valid = false;
        }

        // Validar año
        try {
            int anio = Integer.parseInt(tfAnio.getText());
            if (tfAnio.getText() == null || tfAnio.getText().trim().isEmpty()
                    || tfAnio.getText().length() != 4 || anio < 1980 || anio > anioActual) {
                labelErrorAnio.setText("Año inválido");
                valid = false;
            }
        } catch (NumberFormatException e) {
            labelErrorAnio.setText("Año debe ser un número válido");
            valid = false;
        }

        // Validar VIN
        if (tfVin.getText() == null || tfVin.getText().trim().isEmpty()
                || tfVin.getText().length() > 17) {
            labelErrorVin.setText("VIN inválido");
            valid = false;
        }

        // Validar tipo de unidad
        if (cbTipoUnidad.getValue() == null) {
            labelErrorTipoUnidad.setText("Seleccione un tipo de unidad válido");
            valid = false;
        }

        return valid;
    }

    private void guardarDatosUnidad() {
        Mensaje msj = UnidadesDAO.agregarUnidad(unidad);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("Registro exitoso.", "La información del unidad fue registrada correctamente. " + msj.getMensaje(),
                    Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Error al guardar", msj.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void cargarDatos() {
        tfMarca.setText(this.unidad.getMarca());
        tfModelo.setText(this.unidad.getModelo());
        tfAnio.setText(String.valueOf(this.unidad.getAnio()));
        tfVin.setText(String.valueOf(this.unidad.getVin()));
        labelNoIdentificacion.setText(String.valueOf(this.unidad.getNoIdentificacion()));
        int posicion = buscarTipoUnidad(this.unidad.getIdTipoUnidad());
        cbTipoUnidad.getSelectionModel().select(posicion);
    }

    private void cerrarVentana() {
        AnchorPane contenerdorPrincipal = (AnchorPane) tfMarca.getScene().lookup("#contenedorPrincipal");
        Funciones.cargarVista("/packetworldescritorio/FXMLUnidades.fxml", contenerdorPrincipal);
    }

    private void cargarTipoUnidad() {
        List<TipoUnidad> tiposUnidad = TiposUnidadesDAO.obtenerTiposUnidad();
        if (tiposUnidad != null && !tiposUnidad.isEmpty()) {
            listaObservableTipoUnidad = FXCollections.observableArrayList(tiposUnidad);
            cbTipoUnidad.setItems(listaObservableTipoUnidad);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar", "Lo sentiento, no se pudo obtener la informacion de los tipos de unidades",
                    Alert.AlertType.ERROR);
        }
    }

    private int buscarTipoUnidad(Integer idTipoUnidad) {
        for (int i = 0; i < listaObservableTipoUnidad.size(); i++) {
            if (listaObservableTipoUnidad.get(i).getIdTipoUnidad() == idTipoUnidad) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void setDatos(Unidad unidad) {
        this.unidad = unidad;

        if (unidad != null) {
            cargarDatos();
            tfVin.setDisable(true);
            btnGuardar.setText("Editar");
        } else {
            btnGuardar.setText("Agregar");
            labelNoIdentificacion.setVisible(false);
            labelNoId.setVisible(false);
        }
    }
}
