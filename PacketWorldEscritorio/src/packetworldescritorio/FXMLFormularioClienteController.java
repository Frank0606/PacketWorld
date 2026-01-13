package packetworldescritorio;

import packetworldescritorio.modelo.dao.ClientesDAO;
import packetworldescritorio.modelo.dao.Mensaje;
import packetworldescritorio.pojo.Cliente;
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
import packetworldescritorio.modelo.dao.EstadosDAO;
import packetworldescritorio.pojo.Estado;

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
    private Button btnAgregar;
    @FXML
    private Button btnCancelar;

    private Cliente cliente;
    private ObservableList<Estado> listaObservableEstados;
    
    @FXML
    private Label labelErrorNombreCliente;
    @FXML
    private Label labelErrorApellidoM;
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
    @FXML
    private Label labelErrorCiudad;
    @FXML
    private TextField tfCiudad;
    @FXML
    private Label labelErrorEstado;
    @FXML
    private ComboBox<Estado> tfEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEstados();
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

        configurarTextField(tfCiudad, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ\\s]{0,25}"));
        tfCiudad.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfCiudad.setText(primerCaracter + resto);
            }
        });

        configurarTextField(tfCorreoElectronico, Pattern.compile("[a-zA-Z0-9._%+-@]{0,50}"));
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
        tfNombreCliente.setText(this.cliente.getNombre());
        tfApellidoPaterno.setText(this.cliente.getApellidoPaterno());
        tfApellidoMaterno.setText(this.cliente.getApellidoMaterno());
        tfCorreoElectronico.setText(this.cliente.getCorreo());
        tfCorreoElectronico.setEditable(false);
        tfColonia.setText(this.cliente.getColonia());
        tfTelefono.setText(this.cliente.getTelefono());
        tfCodigoPostal.setText(this.cliente.getCodigoPostal());
        tfCiudad.setText(this.cliente.getCiudad());
        int posicion = buscarEstado(this.cliente.getIdEstado());
        tfEstado.getSelectionModel().select(posicion);
        tfCalle.setText(this.cliente.getCalle());
        tfNumeroCasa.setText(this.cliente.getNumero());
    }
    
    private int buscarEstado(Integer idEstado) {
        for (int i = 0; i < listaObservableEstados.size(); i++) {
            if (listaObservableEstados.get(i).getIdEstado() == idEstado) {
                return i;
            }
        }
        return 0;
    }

    @FXML
    private void btnAgregar(ActionEvent event) {
        if (validarCampos()) {
            if (cliente == null) {
                this.cliente = new Cliente();
            }
            
            Integer idEstado = ((tfEstado.getSelectionModel().getSelectedItem() != null) ? tfEstado.getSelectionModel().getSelectedItem().getIdEstado() : null);
            
            cliente.setNombre(tfNombreCliente.getText());
            cliente.setApellidoPaterno(tfApellidoPaterno.getText());
            cliente.setApellidoMaterno(tfApellidoMaterno.getText());
            cliente.setCorreo(tfCorreoElectronico.getText());
            cliente.setColonia(tfColonia.getText());
            cliente.setTelefono(tfTelefono.getText());
            cliente.setCodigoPostal(tfCodigoPostal.getText());
            cliente.setCiudad(tfCiudad.getText());
            cliente.setIdEstado(idEstado);
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
        AnchorPane contenerdorPrincipal = (AnchorPane) tfApellidoPaterno.getScene().lookup("#contenedorPrincipal");
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
        labelErrorColonia.setText("");
        labelErrorTelefono.setText("");
        labelErrorCP.setText("");
        labelErrorCiudad.setText("");
        labelErrorEstado.setText("");
        labelErrorCalle.setText("");
        labelErrorNumeroCasa.setText("");

        // Validar nombre del cliente
        String nombre = tfNombreCliente.getText() != null ? tfNombreCliente.getText().trim() : "";
        if (nombre.isEmpty()) {
            labelErrorNombreCliente.setText("Nombre vacío.");
            valid = false;
        } else if (nombre.length() < 3) {
            labelErrorNombreCliente.setText("Nombre demasiado corto (mínimo 3 letras).");
            valid = false;
        } else if (nombre.length() > 30) {
            labelErrorNombreCliente.setText("Nombre demasiado largo (máximo 30 letras).");
            valid = false;
        }

        // Validar apellidos: al menos uno debe estar lleno
        String apellidoP = tfApellidoPaterno.getText() != null ? tfApellidoPaterno.getText().trim() : "";
        String apellidoM = tfApellidoMaterno.getText() != null ? tfApellidoMaterno.getText().trim() : "";

        if (apellidoP.isEmpty() && apellidoM.isEmpty()) {
            labelErrorApellidoP.setText("Debe ingresar al menos un apellido.");
            labelErrorApellidoM.setText("Debe ingresar al menos un apellido.");
            valid = false;
        } else {
            if (!apellidoP.isEmpty()) {
                if (apellidoP.length() < 2) {
                    labelErrorApellidoP.setText("El apellido paterno es demasiado corto (mínimo 2 letras).");
                    valid = false;
                } else if (apellidoP.length() > 30) {
                    labelErrorApellidoP.setText("El apellido paterno es demasiado largo (máximo 30 letras).");
                    valid = false;
                }
            } else {
                labelErrorApellidoP.setText("El apellido paterno está vacío.");
                valid = false;
            }

            if (!apellidoM.isEmpty()) {
                if (apellidoM.length() < 2) {
                    labelErrorApellidoM.setText("El apellido materno es demasiado corto (mínimo 2 letras).");
                    valid = false;
                } else if (apellidoM.length() > 30) {
                    labelErrorApellidoM.setText("El apellido materno es demasiado largo (máximo 30 letras).");
                    valid = false;
                }
            } else {
                labelErrorApellidoM.setText("El apellido materno está vacío.");
                valid = false;
            }
        }

        // Validar correo electrónico
        String correo = tfCorreoElectronico.getText() != null ? tfCorreoElectronico.getText().trim() : "";
        if (correo.isEmpty()) {
            labelErrorCorreo.setText("Debe ingresar un correo electrónico.");
            valid = false;
        } else if (correo.length() > 40) {
            labelErrorCorreo.setText("El correo electrónico es demasiado largo (máximo 40 caracteres).");
            valid = false;
        } else if (!correo.contains("@")) {
            labelErrorCorreo.setText("El correo electrónico debe contener '@'.");
            valid = false;
        } else if (!correo.matches(".*\\.[A-Za-z]{2,}$")) {
            labelErrorCorreo.setText("El correo electrónico debe contener un dominio válido (ejemplo: .com, .mx).");
            valid = false;
        } else if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            labelErrorCorreo.setText("Formato de correo electrónico inválido.");
            valid = false;
        }

        // Validar colonia
        String colonia = tfColonia.getText() != null ? tfColonia.getText().trim() : "";
        if (colonia.isEmpty()) {
            labelErrorColonia.setText("Colonia vacía.");
            valid = false;
        } else if (colonia.length() < 2) {
            labelErrorColonia.setText("Colonia demasiado corta (mínimo 2 letras).");
            valid = false;
        } else if (colonia.length() > 25) {
            labelErrorColonia.setText("Colonia demasiado larga (máximo 25 letras).");
            valid = false;
        }

        // Validar teléfono
        String telefono = tfTelefono.getText() != null ? tfTelefono.getText().trim() : "";
        if (telefono.isEmpty()) {
            labelErrorTelefono.setText("Teléfono vacío.");
            valid = false;
        } else if (telefono.length() != 10) {
            labelErrorTelefono.setText("Teléfono inválido (debe tener exactamente 10 dígitos).");
            valid = false;
        }

        // Validar código postal
        String cp = tfCodigoPostal.getText() != null ? tfCodigoPostal.getText().trim() : "";
        if (cp.isEmpty()) {
            labelErrorCP.setText("Código postal vacío.");
            valid = false;
        } else if (cp.length() != 5) {
            labelErrorCP.setText("Código postal inválido (debe tener exactamente 5 dígitos).");
            valid = false;
        }

        // Validar ciudad
        String ciudad = tfCiudad.getText() != null ? tfCiudad.getText().trim() : "";
        if (ciudad.isEmpty()) {
            labelErrorCiudad.setText("Ciudad vacía.");
            valid = false;
        } else if (ciudad.length() < 3) {
            labelErrorCiudad.setText("Ciudad demasiado corta (mínimo 3 letras).");
            valid = false;
        } else if (ciudad.length() > 25) {
            labelErrorCiudad.setText("Ciudad demasiado larga (máximo 25 letras).");
            valid = false;
        }

        // Validar estado
        if (tfEstado.getValue() == null) {
            labelErrorEstado.setText("Seleccione un tipo de unidad válido");
            valid = false;
        }

        // Validar calle
        String calle = tfCalle.getText() != null ? tfCalle.getText().trim() : "";
        if (calle.isEmpty()) {
            labelErrorCalle.setText("Calle vacía.");
            valid = false;
        } else if (calle.length() < 2) {
            labelErrorCalle.setText("Calle demasiado corta (mínimo 2 letras).");
            valid = false;
        } else if (calle.length() > 25) {
            labelErrorCalle.setText("Calle demasiado larga (máximo 25 letras).");
            valid = false;
        }

        // Validar número de casa
        String numeroCasa = tfNumeroCasa.getText() != null ? tfNumeroCasa.getText().trim() : "";
        if (numeroCasa.isEmpty()) {
            labelErrorNumeroCasa.setText("Número de casa vacío.");
            valid = false;
        } else if (numeroCasa.length() > 10) {
            labelErrorNumeroCasa.setText("Número de casa demasiado largo (máximo 10 caracteres).");
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
