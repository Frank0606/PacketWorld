package packetworldescritorio;

import packetworldescritorio.modelo.dao.ClientesDAO;
import packetworldescritorio.modelo.dao.Mensaje;
import packetworldescritorio.pojo.Cliente;
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

public class FXMLFormularioClienteController implements Initializable, ControladorPrincipal<Cliente> {

    @FXML
    private TextField tfNombreCliente;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfCorreoElectronico;
    @FXML
    private TextField tfContrasenia;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnCancelar;

    private Cliente cliente;

    @FXML
    private Label labelErrorNombreCliente;
    @FXML
    private Label labelErrorApellidoM;
    @FXML
    private Label labelErrorContrasenia;
    @FXML
    private Label labelErrorApellidoP;
    @FXML
    private Label labelErrorCalle;
    @FXML
    private Label labelErrorCorreo;
    @FXML
    private Label labelErrorColonia;
    @FXML
    private Label labelErrorTelefono;
    @FXML
    private Label labelErrorCP;
    @FXML
    private TextField tfColonia;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfCodigoPostal;
    @FXML
    private TextField tfCalle;
    @FXML
    private TextField tfNumeroCasa;
    @FXML
    private Label labelErrorNumeroCasa;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTextField(tfNumeroCasa, Pattern.compile("[a-zA-Z0-9]{0,10}"));
        configurarTextField(tfCalle, Pattern.compile("[a-zA-Z0-9]{0,25}"));
        configurarTextField(tfColonia, Pattern.compile("[a-zA-Z0-9]{0,25}"));
        configurarTextField(tfTelefono, Pattern.compile("[0-9]{0,10}"));
        configurarTextField(tfCodigoPostal, Pattern.compile("[0-9]{0,5}"));
        configurarTextField(tfNombreCliente, Pattern.compile("[a-zA-Z]{1,25}(\\s[a-zA-Z]{1,24})?"));
        configurarTextField(tfApellidoPaterno, Pattern.compile("[a-zA-Z]{0,50}"));
        configurarTextField(tfApellidoMaterno, Pattern.compile("[a-zA-Z]{0,50}"));
        //configurarTextField(tfCorreoElectronico, Pattern.compile("^[a-zA-Z0-9._-]{1,25}@[a-zA-Z0-9.-]{1,25}\\.[a-zA-Z]{2,3}$"));
        configurarTextField(tfContrasenia, Pattern.compile(".{0,50}"));
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
        tfNombreCliente.setText(this.cliente.getNombre());
        tfApellidoPaterno.setText(this.cliente.getApellidoPaterno());
        tfApellidoMaterno.setText(this.cliente.getApellidoMaterno());
        tfCorreoElectronico.setText(this.cliente.getCorreo());
        tfCorreoElectronico.setEditable(false);
        tfContrasenia.setText(this.cliente.getContrasena());
        tfColonia.setText(this.cliente.getColonia());
        tfTelefono.setText(this.cliente.getTelefono());
        tfCodigoPostal.setText(this.cliente.getCodigoPostal());
        tfCalle.setText(this.cliente.getCalle());
        tfNumeroCasa.setText(this.cliente.getNumero());
    }

    @FXML
    private void btnAgregar(ActionEvent event) {
        if (validarCampos()) {
            if (cliente == null) {
                this.cliente = new Cliente();
            }
            cliente.setNombre(tfNombreCliente.getText());
            cliente.setApellidoPaterno(tfApellidoPaterno.getText());
            cliente.setApellidoMaterno(tfApellidoMaterno.getText());
            cliente.setCorreo(tfCorreoElectronico.getText());
            cliente.setContrasena(tfContrasenia.getText());
            cliente.setColonia(tfColonia.getText());
            cliente.setTelefono(tfTelefono.getText());
            cliente.setCodigoPostal(tfCodigoPostal.getText());
            cliente.setCalle(tfCalle.getText());
            cliente.setNumero(tfNumeroCasa.getText());
            if (btnAgregar.getText().equals("Editar")) {
                editarDatosCliente();
            } else {
                guardarDatosCliente();
            }
        } else {
            Alertas.mostrarAlertaSimple("Problema", "Algunos campos son incorrectos, revisalos e intenta de nuevo.",
                    Alert.AlertType.ERROR);
        }
    }

    private void editarDatosCliente() {
        Mensaje msj = ClientesDAO.actualizarCliente(cliente);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("El cambio fue exitoso.", "La información del cliente: " + cliente.getNombre() + " " + cliente.getApellidoPaterno() + ", fue actualizado(a) correctamente", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Campos obligatorios.", "Verifica los campos de tu formulario.", Alert.AlertType.WARNING);
        }
    }

    private void guardarDatosCliente() {
        Mensaje msj = ClientesDAO.agregarCliente(cliente);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("Registro exitoso.", "La información del cliente: " + cliente.getNombre() + " " + cliente.getApellidoPaterno() + ", fue registrado(a) correctamente", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Error al guardar", msj.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana() {
        AnchorPane contenerdorPrincipal = (AnchorPane) tfContrasenia.getScene().lookup("#contenedorPrincipal");
        Funciones.cargarVista("/clienteescritorio/FXMLClientes.fxml", contenerdorPrincipal);
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private boolean validarCampos() {
        boolean valid = true;
        labelErrorNombreCliente.setText("");
        labelErrorApellidoP.setText("");
        labelErrorApellidoM.setText("");
        labelErrorCorreo.setText("");
        labelErrorContrasenia.setText("");
        labelErrorColonia.setText("");
        labelErrorTelefono.setText("");
        labelErrorCP.setText("");
        labelErrorCalle.setText("");
        labelErrorNumeroCasa.setText("");

        // Validar nombre del cliente
        if (tfNombreCliente.getText() == null || tfNombreCliente.getText().trim().isEmpty()
                || tfNombreCliente.getText().length() > 50) {
            labelErrorNombreCliente.setText("Nombre inválido");
            valid = false;
        }

        // Validar apellido paterno
        if (tfApellidoPaterno.getText() == null || tfApellidoPaterno.getText().trim().isEmpty()
                || tfApellidoPaterno.getText().length() > 50) {
            labelErrorApellidoP.setText("Apellido Paterno inválido");
            valid = false;
        }

        // Validar apellido materno
        if (tfApellidoMaterno.getText() == null || tfApellidoMaterno.getText().trim().isEmpty()
                || tfApellidoMaterno.getText().length() > 50) {
            labelErrorApellidoM.setText("Apellido Materno inválido");
            valid = false;
        }

        // Validar correo electrónico
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,3}$";
        if (tfCorreoElectronico.getText() == null || tfCorreoElectronico.getText().trim().isEmpty()
                || tfCorreoElectronico.getText().length() > 50
                || !tfCorreoElectronico.getText().matches(emailPattern)) {
            labelErrorCorreo.setText("Correo electrónico inválido");
            valid = false;
        }

        // Validar contraseña
        if (tfContrasenia.getText() == null || tfContrasenia.getText().trim().isEmpty()
                || tfContrasenia.getText().length() > 50) {
            labelErrorContrasenia.setText("Contraseña inválida");
            valid = false;
        }

        // Validar colonia
        if (tfColonia.getText() == null || tfColonia.getText().trim().isEmpty()
                || tfColonia.getText().length() > 25) {
            labelErrorColonia.setText("Colonia inválida");
            valid = false;
        }

        // Validar teléfono
        if (tfTelefono.getText() == null || tfTelefono.getText().trim().isEmpty()
                || tfTelefono.getText().length() != 10) {
            labelErrorTelefono.setText("Teléfono inválido");
            valid = false;
        }

        // Validar código postal
        if (tfCodigoPostal.getText() == null || tfCodigoPostal.getText().trim().isEmpty()
                || tfCodigoPostal.getText().length() != 5) {
            labelErrorCP.setText("Código Postal inválido");
            valid = false;
        }

        // Validar calle
        if (tfCalle.getText() == null || tfCalle.getText().trim().isEmpty()
                || tfCalle.getText().length() > 25) {
            labelErrorCalle.setText("Calle inválida");
            valid = false;
        }

        // Validar número de casa
        if (tfNumeroCasa.getText() == null || tfNumeroCasa.getText().trim().isEmpty()
                || tfNumeroCasa.getText().length() > 10) {
            labelErrorNumeroCasa.setText("Número de casa inválido");
            valid = false;
        }

        return valid;
    }

    @Override
    public void setDatos(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null) {
            cargarDatos();
            tfCorreoElectronico.setEditable(false);
            tfCorreoElectronico.setDisable(true);
            btnAgregar.setText("Editar");
        } else {
            btnAgregar.setText("Agregar");
        }
    }
}
