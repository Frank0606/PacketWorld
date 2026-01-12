package packetworldescritorio;

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
import packetworldescritorio.modelo.dao.SucursalDAO;
import packetworldescritorio.pojo.Mensaje;
import packetworldescritorio.pojo.Sucursal;
import packetworldescritorio.utilidades.Alertas;
import packetworldescritorio.utilidades.ControladorPrincipal;
import packetworldescritorio.utilidades.Funciones;

public class FXMLFormularioSucursalController implements Initializable, ControladorPrincipal<Sucursal> {

    @FXML
    private TextField tfCodigo;
    @FXML
    private Label labelErrorCodigo;
    @FXML
    private TextField tfNombreCorto;
    @FXML
    private Label labelErrorNombreCorto;
    @FXML
    private Label tfEstatus;
    @FXML
    private Label labelErrorEstatus;
    @FXML
    private TextField tfCalle;
    @FXML
    private Label labelErrorCalle;
    @FXML
    private TextField tfNumero;
    @FXML
    private Label labelErrorNumero;
    @FXML
    private TextField tfColonia;
    @FXML
    private Label labelErrorColonia;
    @FXML
    private TextField tfCodigoPostal;
    @FXML
    private Label labelErrorCodigoPostal;
    @FXML
    private TextField tfCiudad;
    @FXML
    private Label labelErrorCiudad;
    @FXML
    private TextField tfEstado;
    @FXML
    private Label labelErrorEstado;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private Sucursal sucursal;
    @FXML
    private ComboBox<String> cbEstatus;

    private ObservableList<String> listaObservableSucursales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTextField(tfColonia, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ0-9\\s]{0,25}"));
        tfColonia.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfColonia.setText(primerCaracter + resto);
            }
        });
        configurarTextField(tfCalle, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ0-9\\s]{0,50}"));
        tfCalle.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfCalle.setText(primerCaracter + resto);
            }
        });
        configurarTextField(tfNombreCorto, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ0-9\\s]{0,50}"));
        tfNombreCorto.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfNombreCorto.setText(primerCaracter + resto);
            }
        });
        configurarTextField(tfCiudad, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ\\s]{0,50}"));
        tfCiudad.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfCiudad.setText(primerCaracter + resto);
            }
        });
        configurarTextField(tfEstado, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ\\s]{0,50}"));
        tfEstado.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfEstado.setText(primerCaracter + resto);
            }
        });
        configurarTextField(tfNumero, Pattern.compile("[0-9]{0,5}"));
        configurarTextField(tfCodigoPostal, Pattern.compile("[0-9]{0,5}"));
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

    private void cargarDatos() {
        int cont = 0, posicion = 0;
        tfCodigo.setText(this.sucursal.getCodigo());
        tfNombreCorto.setText(this.sucursal.getNombreCorto());

        listaObservableSucursales = FXCollections.observableArrayList("Activa", "Inactiva");
        cbEstatus.setItems(listaObservableSucursales);
        List<Sucursal> sucursales = SucursalDAO.obtenerSucursales();
        for (Sucursal suc : sucursales) {
            for (String estatus : listaObservableSucursales) {
                cont++;
                if (suc.getEstatus().equals(estatus)) {
                    posicion = cont;
                }
            }
            cont = 0;
        }
        
        cbEstatus.getSelectionModel().select(posicion-1);

        tfCalle.setText(this.sucursal.getCalle());
        tfNumero.setText(this.sucursal.getNumero());
        tfColonia.setText(this.sucursal.getColonia());
        tfCodigoPostal.setText(this.sucursal.getCodigoPostal());
        tfCiudad.setText(this.sucursal.getCiudad());
        tfEstado.setText(this.sucursal.getEstado());
    }

    private void editarDatosSucursal() {
        Mensaje msj = SucursalDAO.editarSucursal(sucursal);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("El cambio fue exitoso.", "La información de la sucursal: " + sucursal.getNombreCorto() + ", fue actualizado(a) correctamente", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Campos obligatorios.", "Verifica los campos de tu formulario.", Alert.AlertType.WARNING);
        }
    }

    private void guardarDatosSucursal() {
        Mensaje msj = SucursalDAO.registrarSucursal(sucursal);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("Registro exitoso.", "La información de la sucursal: " + sucursal.getNombreCorto() + ", fue registrado(a) correctamente", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Error al guardar", msj.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana() {
        AnchorPane contenerdorPrincipal = (AnchorPane) tfCodigo.getScene().lookup("#contenedorPrincipal");
        Funciones.cargarVista("/packetworldescritorio/FXMLSucursal.fxml", contenerdorPrincipal);
    }

    @FXML
    private void btnGuardar(ActionEvent event) {
        if (validarCampos()) {
            if (this.sucursal == null) {
                this.sucursal = new Sucursal();
            }

            String estatus = ((cbEstatus.getSelectionModel().getSelectedItem() != null)
                    ? cbEstatus.getSelectionModel().getSelectedItem()
                    : null);
            if(estatus == null || estatus.isEmpty()){
                estatus = tfEstatus.getText();
            }
            
            sucursal.setCodigo(tfCodigo.getText());
            sucursal.setNombreCorto(tfNombreCorto.getText());
            sucursal.setEstatus(estatus);
            sucursal.setCalle(tfCalle.getText());
            sucursal.setNumero(tfNumero.getText());
            sucursal.setColonia(tfColonia.getText());
            sucursal.setCodigoPostal(tfCodigoPostal.getText());
            sucursal.setCiudad(tfCiudad.getText());
            sucursal.setEstado(tfEstado.getText());

            if (btnGuardar.getText().equals("Editar")) {
                editarDatosSucursal();
            } else {
                guardarDatosSucursal();
            }
        } else {
            Alertas.mostrarAlertaSimple("Problema con los datos",
                    "Tiene datos incorrectos, revise e intente de nuevo.",
                    Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos() {
        boolean valid = true;
        labelErrorNombreCorto.setText("");
        labelErrorCalle.setText("");
        labelErrorNumero.setText("");
        labelErrorColonia.setText("");
        labelErrorCodigoPostal.setText("");
        labelErrorCiudad.setText("");
        labelErrorEstado.setText("");

        if (tfNombreCorto.getText() == null || tfNombreCorto.getText().trim().isEmpty()
                || (tfNombreCorto.getText().length() < 10 || tfNombreCorto.getText().length() > 50)) {
            labelErrorNombreCorto.setText("Nombre corto inválido");
            valid = false;
        }

        if (tfCalle.getText() == null || tfCalle.getText().trim().isEmpty()
                || (tfCalle.getText().length() < 1 || tfCalle.getText().length() > 50)) {
            labelErrorCalle.setText("Nombre de calle inválido");
            valid = false;
        }

        if (tfNumero.getText() == null || tfNumero.getText().trim().isEmpty()
                || (tfNumero.getText().length() < 1 || tfNumero.getText().length() > 5)) {
            labelErrorNumero.setText("Número inválido");
            valid = false;
        }

        if (tfColonia.getText() == null || tfColonia.getText().trim().isEmpty()
                || (tfColonia.getText().length() < 3 || tfColonia.getText().length() > 50)) {
            labelErrorColonia.setText("Colonia inválido");
            valid = false;
        }

        if (tfCodigoPostal.getText() == null || tfCodigoPostal.getText().trim().isEmpty()
                || (tfCodigoPostal.getText().length() != 5)) {
            labelErrorCodigoPostal.setText("Colonia inválido");
            valid = false;
        }

        if (tfCiudad.getText() == null || tfCiudad.getText().trim().isEmpty()
                || (tfCiudad.getText().length() < 3 || tfCiudad.getText().length() > 50)) {
            labelErrorCiudad.setText("Colonia inválido");
            valid = false;
        }

        if (tfEstado.getText() == null || tfEstado.getText().trim().isEmpty()
                || (tfEstado.getText().length() < 3 || tfEstado.getText().length() > 50)) {
            labelErrorEstado.setText("Colonia inválido");
            valid = false;
        }

        return valid;
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void crearCodigo() {
        List<Sucursal> sucursales = SucursalDAO.obtenerSucursales();
        String maxCodigo = "SUC000";

        for (Sucursal suc : sucursales) {
            String codigo = suc.getCodigo();
            if (codigo.compareTo(maxCodigo) > 0) {
                maxCodigo = codigo;
            }
        }

        int numeroMax = Integer.parseInt(maxCodigo.substring(3));
        int nuevoNumero = numeroMax + 1;
        String nuevoNoPersonal = "SUC" + String.format("%03d", nuevoNumero);
        tfCodigo.setText(nuevoNoPersonal);
    }

    @Override
    public void setDatos(Sucursal sucursal) {
        this.sucursal = sucursal;
        tfCodigo.setEditable(false);
        tfCodigo.setDisable(true);

        //txNumeroLicencia.setVisible(false);
        //tfVIM.setVisible(false);
        if (sucursal != null) {
            cargarDatos();
            tfEstatus.setDisable(true);
            btnGuardar.setText("Editar");
        } else {
            btnGuardar.setText("Agregar");
            cbEstatus.setVisible(false);
            tfEstatus.setDisable(false);
            tfEstatus.setText("Activa");
            crearCodigo();
        }
    }

}
