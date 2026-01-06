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

        configurarTextField(tfCalle, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ0-9\\s]{0,25}"));
        tfCalle.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfCalle.setText(primerCaracter + resto);
            }
        });

        configurarTextField(tfColonia, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ\\s]{0,25}"));
        tfColonia.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfColonia.setText(primerCaracter + resto);
            }
        });

        configurarTextField(tfTelefono, Pattern.compile("[0-9]{0,10}"));

        configurarTextField(tfCodigoPostal, Pattern.compile("[0-9]{0,5}"));

        configurarTextField(tfNombreCliente, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ\\s]{0,25}"));
        tfNombreCliente.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfNombreCliente.setText(primerCaracter + resto);
            }
        });

        configurarTextField(tfApellidoPaterno, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ\\s]{0,25}"));
        tfApellidoPaterno.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfApellidoPaterno.setText(primerCaracter + resto);
            }
        });

        configurarTextField(tfApellidoMaterno, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ\\s]{0,25}"));
        tfApellidoMaterno.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfApellidoMaterno.setText(primerCaracter + resto);
            }
        });

        configurarTextField(tfCorreoElectronico, Pattern.compile("[a-zA-Z0-9._%+-@]{0,50}"));

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
        Funciones.cargarVista("/packetworldescritorio/FXMLClientes.fxml", contenerdorPrincipal);
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

        if (tfNombreCliente.getText() == null || tfNombreCliente.getText().trim().isEmpty()
                || tfNombreCliente.getText().length() < 3 || tfNombreCliente.getText().length() > 50) {
            labelErrorNombreCliente.setText("Nombre inválido (mínimo 3 letras)");
            valid = false;
        }

        String apellidoP = tfApellidoPaterno.getText() != null ? tfApellidoPaterno.getText().trim() : "";
        String apellidoM = tfApellidoMaterno.getText() != null ? tfApellidoMaterno.getText().trim() : "";

        if (apellidoP.isEmpty() && apellidoM.isEmpty()) {
            labelErrorApellidoP.setText("Debe ingresar al menos un apellido");
            labelErrorApellidoM.setText("Debe ingresar al menos un apellido");
            valid = false;
        } else {
            if (!apellidoP.isEmpty() && (apellidoP.length() < 2 || apellidoP.length() > 50)) {
                labelErrorApellidoP.setText("Apellido Paterno inválido (mínimo 2 letras)");
                valid = false;
            }
            if (!apellidoM.isEmpty() && (apellidoM.length() < 2 || apellidoM.length() > 50)) {
                labelErrorApellidoM.setText("Apellido Materno inválido (mínimo 2 letras)");
                valid = false;
            }
        }

        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$";
        if (tfCorreoElectronico.getText() == null || tfCorreoElectronico.getText().trim().isEmpty()
                || tfCorreoElectronico.getText().length() > 50
                || !tfCorreoElectronico.getText().matches(emailPattern)) {
            labelErrorCorreo.setText("Correo electrónico inválido");
            valid = false;
        }

        if (tfContrasenia.getText() == null || tfContrasenia.getText().trim().isEmpty()
                || tfContrasenia.getText().length() > 50) {
            labelErrorContrasenia.setText("Contraseña inválida");
            valid = false;
        }

        if (tfColonia.getText() == null || tfColonia.getText().trim().isEmpty()
                || tfColonia.getText().length() < 2 || tfColonia.getText().length() > 25) {
            labelErrorColonia.setText("Colonia inválida (mínimo 2 letras)");
            valid = false;
        }

        if (tfTelefono.getText() == null || tfTelefono.getText().trim().isEmpty()
                || tfTelefono.getText().length() != 10) {
            labelErrorTelefono.setText("Teléfono inválido (debe tener 10 dígitos)");
            valid = false;
        }

        if (tfCodigoPostal.getText() == null || tfCodigoPostal.getText().trim().isEmpty()
                || tfCodigoPostal.getText().length() != 5) {
            labelErrorCP.setText("Código Postal inválido (debe tener 5 dígitos)");
            valid = false;
        }

        if (tfCalle.getText() == null || tfCalle.getText().trim().isEmpty()
                || tfCalle.getText().length() < 2 || tfCalle.getText().length() > 25) {
            labelErrorCalle.setText("Calle inválida (mínimo 2 letras)");
            valid = false;
        }

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
