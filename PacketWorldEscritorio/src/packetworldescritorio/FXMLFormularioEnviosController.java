package packetworldescritorio;

import packetworldescritorio.modelo.dao.ClientesDAO;
import packetworldescritorio.modelo.dao.ColaboradoresDAO;
import packetworldescritorio.modelo.dao.EnviosDAO;
import packetworldescritorio.modelo.dao.Mensaje;
import packetworldescritorio.pojo.Cliente;
import packetworldescritorio.pojo.Colaborador;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;

public class FXMLFormularioEnviosController implements Initializable, ControladorPrincipal<Envio> {

    @FXML
    private TextField tfCalle;
    @FXML
    private TextField tfCostoEnvio;
    @FXML
    private TextField tfNumeroCasa;
    @FXML
    private TextField tfColonia;
    @FXML
    private TextField tfCP;
    @FXML
    private TextField tfCiudad;
    @FXML
    private TextField tfEstado;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label labelErrorCalle;
    @FXML
    private Label labelErrorCostoEnvio;
    @FXML
    private Label labelErrorNumeroCasa;
    @FXML
    private Label labelErrorColonia;
    @FXML
    private Label labelErrorCP;
    @FXML
    private Label labelErrorCiudad;
    @FXML
    private Label labelErrorEstado;
    @FXML
    private Label labelErrorIdCliente;
    @FXML
    private ComboBox<Cliente> cbCliente;

    private Envio envio;
    private ObservableList<Cliente> listaObservableClientes;
    private ObservableList<Colaborador> listaObservableConductores;
    private String noPersonalColaborador;

    @FXML
    private TextField tfNumeroGuia;
    @FXML
    private Label labelErrorIdColaborador;
    @FXML
    private ComboBox<Colaborador> cbColaborador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarClientes();
        cargarConductores();
        configurarTextField(tfCalle, Pattern.compile("[a-zA-Z0-9 ]{0,50}"));
        configurarTextField(tfCostoEnvio, Pattern.compile("\\d{0,10}([\\.]\\d{0,2})?"));
        configurarTextField(tfNumeroCasa, Pattern.compile("[a-zA-Z0-9]{0,10}"));
        configurarTextField(tfColonia, Pattern.compile("[a-zA-Z0-9 ]{0,50}"));
        configurarTextField(tfCP, Pattern.compile("[0-9]{0,5}"));
        configurarTextField(tfCiudad, Pattern.compile("[a-zA-Z ]{0,50}"));
        configurarTextField(tfEstado, Pattern.compile("[a-zA-Z ]{0,50}"));

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
            if (this.envio == null) {
                this.envio = new Envio();
            }

            Integer idCliente = ((cbCliente.getSelectionModel().getSelectedItem() != null)
                    ? cbCliente.getSelectionModel().getSelectedItem().getIdCliente() : null);

            Integer idColaborador = ((cbColaborador.getSelectionModel().getSelectedItem() != null)
                    ? cbColaborador.getSelectionModel().getSelectedItem().getIdColaborador() : null);

            envio.setCalleDestino(tfCalle.getText().toUpperCase());
            
            
            
            envio.setNumeroDestino(tfNumeroCasa.getText().toUpperCase());
            envio.setColoniaDestino(tfColonia.getText().toUpperCase());
            envio.setCpDestino(tfCP.getText());
            envio.setCiudadDestino(tfCiudad.getText().toUpperCase());
            envio.setEstadoDestino(tfEstado.getText().toUpperCase());
            envio.setNumeroGuia(tfNumeroGuia.getText().toUpperCase());

            envio.setIdCliente(idCliente);
            envio.setIdColaborador(idColaborador);

            if (btnGuardar.getText().equals("Editar")) {
                
                editarDatosEnvio();
            } else {
                envio.setEstatus("PENDIENTE");

                LocalDateTime ahora = LocalDateTime.now();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
                String fecha = ahora.format(formato);

                //envio.setHistorialEstados(envio.getEstatus() + "_" + fecha + "_" + noPersonalColaborador + ":");
                
                guardarDatosEnvio();
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

    private void editarDatosEnvio() {
        Mensaje msj = EnviosDAO.actualizarEnvio(envio);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("Cambio exitoso.", "La información del envio fue actualizada "
                    + "correctamente", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Campos obligatorios.", "Verifica los campos de tu formulario.", Alert.AlertType.WARNING);
        }
    }

    private boolean validarCampos() {
        boolean valid = true;
        labelErrorCalle.setText("");
        labelErrorCostoEnvio.setText("");
        labelErrorNumeroCasa.setText("");
        labelErrorColonia.setText("");
        labelErrorCP.setText("");
        labelErrorEstado.setText("");
        labelErrorCiudad.setText("");
        labelErrorIdCliente.setText("");
        labelErrorIdColaborador.setText("");

        // Validar calle
        if (tfCalle.getText() == null || tfCalle.getText().trim().isEmpty()
                || tfCalle.getText().length() > 25) {
            labelErrorCalle.setText("Calle inválida");
            valid = false;
        }

        // Validar costo de envío
        try {
            Double costoEnvio = Double.valueOf(tfCostoEnvio.getText());
            if (costoEnvio == null || tfCostoEnvio.getText().trim().isEmpty()
                    || tfCostoEnvio.getText().length() > 10 || !tfCostoEnvio.getText().matches("\\d+(\\.\\d{1,2})?")) {
                labelErrorCostoEnvio.setText("Costo de envío inválido");
                valid = false;
            }
        } catch (NumberFormatException e) {
            labelErrorCostoEnvio.setText("Costo de envío debe ser un número válido");
            valid = false;
        }

        // Validar número de casa
        if (tfNumeroCasa.getText() == null || tfNumeroCasa.getText().trim().isEmpty()
                || tfNumeroCasa.getText().length() > 10) {
            labelErrorNumeroCasa.setText("Número de casa inválido");
            valid = false;
        }

        // Validar colonia
        if (tfColonia.getText() == null || tfColonia.getText().trim().isEmpty()
                || tfColonia.getText().length() > 25) {
            labelErrorColonia.setText("Colonia inválida");
            valid = false;
        }

        // Validar código postal
        if (tfCP.getText() == null || tfCP.getText().trim().isEmpty()
                || tfCP.getText().length() != 5) {
            labelErrorCP.setText("Código Postal inválido");
            valid = false;
        }

        // Validar ciudad
        if (tfCiudad.getText() == null || tfCiudad.getText().trim().isEmpty()
                || tfCiudad.getText().length() > 25) {
            labelErrorCiudad.setText("Ciudad inválida");
            valid = false;
        }

        // Validar estado
        if (tfEstado.getText() == null || tfEstado.getText().trim().isEmpty()
                || tfEstado.getText().length() > 25) {
            labelErrorEstado.setText("Estado inválido");
            valid = false;
        }

        // Validar cliente
        if (cbCliente.getValue() == null) {
            labelErrorIdCliente.setText("Seleccione un cliente válido");
            valid = false;
        }

        // Validar colaborador
        if (cbColaborador.getValue() == null) {
            labelErrorIdColaborador.setText("Seleccione un conductor válido");
            valid = false;
        }

        return valid;
    }

    private void guardarDatosEnvio() {
        Mensaje msj = EnviosDAO.agregarEnvio(envio);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("Registro exitoso.", "La información del envio fue registrada correctamente",
                    Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Error al guardar", msj.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void cargarDatos() {
        tfCalle.setText(this.envio.getCalleDestino());
        
        tfCostoEnvio.setText(String.valueOf(this.envio.getCostoTotal()));
        
        tfNumeroCasa.setText(this.envio.getNumeroDestino());
        tfColonia.setText(this.envio.getColoniaDestino());
        tfCP.setText(this.envio.getCpDestino());
        tfCiudad.setText(this.envio.getCiudadDestino());
        tfEstado.setText(this.envio.getEstadoDestino());
        tfNumeroGuia.setText(this.envio.getNumeroGuia());
        int posicion = buscarCliente(this.envio.getIdCliente());
        cbCliente.getSelectionModel().select(posicion);
        posicion = buscarColaborador(this.envio.getIdColaborador());
        cbColaborador.getSelectionModel().select(posicion);
    }

    private void cerrarVentana() {
        AnchorPane contenerdorPrincipal = (AnchorPane) tfCalle.getScene().lookup("#contenedorPrincipal");
        Funciones.cargarVista("/clienteescritorio/FXMLEnvios.fxml", contenerdorPrincipal);
    }

    private void cargarClientes() {
        List<Cliente> clientes = ClientesDAO.obtenerClientes();
        if (clientes != null && !clientes.isEmpty()) {
            listaObservableClientes = FXCollections.observableArrayList(clientes);
            cbCliente.setItems(listaObservableClientes);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar", "Lo sentiento, no se pudo obtener la informacion de los clientes",
                    Alert.AlertType.ERROR);
        }
    }

    private void cargarConductores() {
        List<Colaborador> colaboradores = ColaboradoresDAO.obtenerColaborador();
        List<Colaborador> conductores = new ArrayList();
        if (colaboradores != null && !colaboradores.isEmpty()) {
            for (Colaborador colaborador : colaboradores) {
                if (colaborador.getIdRol() == 3 && colaborador.getIdUnidad() != 0) {
                    conductores.add(colaborador);
                }
            }
            listaObservableConductores = FXCollections.observableArrayList(conductores);
            cbColaborador.setItems(listaObservableConductores);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar", "Lo sentiento, no se pudo obtener la informacion de los conductores",
                    Alert.AlertType.ERROR);
        }
    }

    private int buscarCliente(Integer idCliente) {
        for (int i = 0; i < listaObservableClientes.size(); i++) {
            if (listaObservableClientes.get(i).getIdCliente().equals(idCliente)) {
                return i;
            }
        }
        return 0;
    }

    private int buscarColaborador(Integer idColaborador) {
        for (int i = 0; i < listaObservableConductores.size(); i++) {
            if (listaObservableConductores.get(i).getIdColaborador() == idColaborador) {
                return i;
            }
        }
        return 0;
    }

    private void generarNumeroGuia() {
        List<Envio> envios = EnviosDAO.obtenerEnvios();
        String maxNumeroGuia = "GUIA000";

        for (Envio envio : envios) {
            String numeroGuia = envio.getNumeroGuia();
            if (numeroGuia.compareTo(maxNumeroGuia) > 0) {
                maxNumeroGuia = numeroGuia;
            }
        }

        int numeroMax = Integer.parseInt(maxNumeroGuia.substring(1));
        int nuevoNumero = numeroMax + 1;
        String nuevoNumeroGuia = "GUIA" + String.format("%03d", nuevoNumero);
        tfNumeroGuia.setText(nuevoNumeroGuia);
    }

    @Override
    public void setDatos(Envio envio) {
        this.envio = envio;
        tfNumeroGuia.setDisable(true);
        if (this.envio != null) {
            cargarDatos();
            btnGuardar.setText("Editar");
        } else {
            btnGuardar.setText("Agregar");
            generarNumeroGuia();
        }
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonalColaborador = noPersonal;
    }

}
