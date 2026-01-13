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
import packetworldescritorio.modelo.dao.EstadosDAO;
import packetworldescritorio.modelo.dao.SucursalDAO;
import packetworldescritorio.pojo.Estado;
import packetworldescritorio.pojo.Mensaje;
import packetworldescritorio.pojo.Sucursal;
import packetworldescritorio.utilidades.Alertas;
import packetworldescritorio.utilidades.ControladorPrincipal;
import packetworldescritorio.utilidades.Funciones;

public class FXMLFormularioSucursalController implements Initializable, ControladorPrincipal<Sucursal> {

    @FXML
    private Label tfCodigo;
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
    private ComboBox<Estado> tfEstado;
    @FXML
    private Label labelErrorEstado;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private Sucursal sucursal;
    @FXML
    private Label cbEstatus;

    private ObservableList<String> listaObservableSucursales;
    private ObservableList<Estado> listaObservableEstados;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEstados();
        configurarTextField(tfColonia, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ0-9\\s]{0,25}"));
        tfColonia.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfColonia.setText(primerCaracter + resto);
            }
        });
        configurarTextField(tfCalle, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ0-9\\s]{0,25}"));
        tfCalle.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfCalle.setText(primerCaracter + resto);
            }
        });
        configurarTextField(tfNombreCorto, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ0-9\\s]{9,30}"));
        tfNombreCorto.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfNombreCorto.setText(primerCaracter + resto);
            }
        });
        configurarTextField(tfCiudad, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ\\s]{0,25}"));
        tfCiudad.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfCiudad.setText(primerCaracter + resto);
            }
        });
        configurarTextField(tfNumero, Pattern.compile("[a-zA-Z0-9]{0,5}"));
        configurarTextField(tfCodigoPostal, Pattern.compile("[0-9]{0,5}"));
    }

    private void cargarEstados() {
        List<Estado> estados = EstadosDAO.obtenerTodos();
        if (estados != null && !estados.isEmpty()) {
            listaObservableEstados = FXCollections.observableArrayList(estados);
            tfEstado.setItems(listaObservableEstados);
        }
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
        tfCalle.setText(this.sucursal.getCalle());
        tfNumero.setText(this.sucursal.getNumero());
        tfColonia.setText(this.sucursal.getColonia());
        tfCodigoPostal.setText(this.sucursal.getCodigoPostal());
        tfCiudad.setText(this.sucursal.getCiudad());
        int pos = buscarEstado(this.sucursal.getIdEstado());
        tfEstado.getSelectionModel().select(pos);
        cbEstatus.setText(this.sucursal.getEstatus());
    }

    private int buscarEstado(Integer idEstado) {
        for (int i = 0; i < listaObservableEstados.size(); i++) {
            if (listaObservableEstados.get(i).getIdEstado() == idEstado) {
                return i;
            }
        }
        return 0;
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

            Integer idEstado = ((tfEstado.getSelectionModel().getSelectedItem() != null) ? tfEstado.getSelectionModel().getSelectedItem().getIdEstado() : null);

            sucursal.setCodigo(tfCodigo.getText());
            sucursal.setNombreCorto(tfNombreCorto.getText());
            sucursal.setCalle(tfCalle.getText());
            sucursal.setNumero(tfNumero.getText());
            sucursal.setColonia(tfColonia.getText());
            sucursal.setCodigoPostal(tfCodigoPostal.getText());
            sucursal.setCiudad(tfCiudad.getText());
            sucursal.setIdEstado(idEstado);

            if (btnGuardar.getText().equals("Editar")) {
                sucursal.setEstatus(cbEstatus.getText());
                editarDatosSucursal();
            } else {
                sucursal.setEstatus("Activa");
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

        // Limpiar errores
        labelErrorNombreCorto.setText("");
        labelErrorCalle.setText("");
        labelErrorNumero.setText("");
        labelErrorColonia.setText("");
        labelErrorCodigoPostal.setText("");
        labelErrorCiudad.setText("");
        labelErrorEstado.setText("");

        String nombreCorto = tfNombreCorto.getText() != null ? tfNombreCorto.getText().trim() : "";

        if (nombreCorto.isEmpty()) {
            labelErrorNombreCorto.setText("Nombre corto vacío");
            valid = false;
        } else if (nombreCorto.length() < 10) {
            labelErrorNombreCorto.setText("Nombre corto demasiado corto (mínimo 10 caracteres)");
            valid = false;
        } else if (nombreCorto.length() > 50) {
            labelErrorNombreCorto.setText("Nombre corto demasiado largo (máximo 50 caracteres)");
            valid = false;
        }

        String calle = tfCalle.getText() != null ? tfCalle.getText().trim() : "";

        if (calle.isEmpty()) {
            labelErrorCalle.setText("Nombre de calle vacío");
            valid = false;
        } else if (calle.length() > 50) {
            labelErrorCalle.setText("Nombre de calle demasiado largo (máximo 50 caracteres)");
            valid = false;
        }

        String numero = tfNumero.getText() != null ? tfNumero.getText().trim() : "";

        if (numero.isEmpty()) {
            labelErrorNumero.setText("Número vacío");
            valid = false;
        } else if (numero.length() > 5) {
            labelErrorNumero.setText("Número demasiado largo (máximo 5 caracteres)");
            valid = false;
        }

        String colonia = tfColonia.getText() != null ? tfColonia.getText().trim() : "";

        if (colonia.isEmpty()) {
            labelErrorColonia.setText("Colonia vacía");
            valid = false;
        } else if (colonia.length() < 3) {
            labelErrorColonia.setText("Colonia demasiado corta (mínimo 3 caracteres)");
            valid = false;
        } else if (colonia.length() > 50) {
            labelErrorColonia.setText("Colonia demasiado larga (máximo 50 caracteres)");
            valid = false;
        }

        String codigoPostal = tfCodigoPostal.getText() != null ? tfCodigoPostal.getText().trim() : "";

        if (codigoPostal.isEmpty()) {
            labelErrorCodigoPostal.setText("Código postal vacío");
            valid = false;
        } else if (!codigoPostal.matches("\\d{5}")) {
            labelErrorCodigoPostal.setText("El código postal debe tener exactamente 5 dígitos");
            valid = false;
        }

        String ciudad = tfCiudad.getText() != null ? tfCiudad.getText().trim() : "";

        if (ciudad.isEmpty()) {
            labelErrorCiudad.setText("Ciudad vacía");
            valid = false;
        } else if (ciudad.length() < 3) {
            labelErrorCiudad.setText("Ciudad demasiado corta (mínimo 3 caracteres)");
            valid = false;
        } else if (ciudad.length() > 50) {
            labelErrorCiudad.setText("Ciudad demasiado larga (máximo 50 caracteres)");
            valid = false;
        }

        if (tfEstado.getValue() == null) {
            labelErrorEstado.setText("Seleccione un estado");
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

        if (sucursal != null) {
            cargarDatos();
            tfEstatus.setDisable(true);
            btnGuardar.setText("Editar");
        } else {
            btnGuardar.setText("Agregar");
            cbEstatus.setVisible(false);
            tfEstatus.setDisable(false);
            tfEstatus.setText("Activa");
            tfNombreCorto.setText("Sucursal ");
            crearCodigo();
        }
    }

}
