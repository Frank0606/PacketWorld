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
import packetworldescritorio.modelo.dao.EstadosDAO;
import packetworldescritorio.modelo.dao.EstadosEnvioDAO;
import packetworldescritorio.modelo.dao.SucursalDAO;
import packetworldescritorio.pojo.Estado;
import packetworldescritorio.pojo.Sucursal;

public class FXMLFormularioEnviosController implements Initializable, ControladorPrincipal<Envio> {

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox<Cliente> cbCliente;
    @FXML
    private Label labelErrorIdColaborador;
    @FXML
    private Label labelErrorIdCliente;
    @FXML
    private Label labelErrorIdSucursal;

    private String noPersonalColaborador;
    private ObservableList<Cliente> listaObservableClientes;
    private ObservableList<Colaborador> listaObservableColaboradores;
    private ObservableList<Sucursal> listaObservableSucursales;

    private Envio envio;

    @FXML
    private ComboBox<Colaborador> cbColaborador;
    @FXML
    private ComboBox<Sucursal> cbSucursal;
    @FXML
    private Label lblDireccionOrigen;
    @FXML
    private TextField tfNumeroGuia;
    @FXML
    private TextField tfCalle;
    @FXML
    private TextField tfNumero;
    @FXML
    private TextField tfColonia;
    @FXML
    private TextField tfCp;
    @FXML
    private TextField tfCiudad;
    @FXML
    private ComboBox<Estado> cbEstado;
    @FXML
    private Label labelErrorCalle;
    @FXML
    private Label labelErrorNumero;
    @FXML
    private Label labelErrorColonia;
    @FXML
    private Label labelErrorCp;
    @FXML
    private Label labelErrorCiudad;
    @FXML
    private Label labelErrorEstado;
    @FXML
    private Label labelErrorNombre;
    @FXML
    private TextField tfNombre;

    private ObservableList<Estado> listaObservableEstados;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarClientes();
        cargarConductores();
        cargarSucursales();
        cargarEventosCb();
        cargarEstados();

        configurarTextField(tfNombre, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ\\s]{0,50}"));
        tfNombre.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfNombre.setText(primerCaracter + resto);
            }
        });

        configurarTextField(tfCalle, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ0-9\\s]{0,100}"));
        tfCalle.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfCalle.setText(primerCaracter + resto);
            }
        });

        configurarTextField(tfNumero, Pattern.compile("[a-zA-Z0-9]{0,20}"));
        tfNumero.textProperty().addListener((obs, old, neu)
                -> tfNumero.setText(neu.toUpperCase())
        );

        configurarTextField(tfColonia, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ0-9\\s]{0,100}"));
        tfColonia.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfColonia.setText(primerCaracter + resto);
            }
        });

        configurarTextField(tfCp, Pattern.compile("[0-9]{0,5}"));

        configurarTextField(tfCiudad, Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ\\s]{0,50}"));
        tfCiudad.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String primerCaracter = newValue.substring(0, 1).toUpperCase();
                String resto = newValue.substring(1);
                tfCiudad.setText(primerCaracter + resto);
            }
        });

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

    private void cargarEstados() {
        List<Estado> estados = EstadosDAO.obtenerTodos();
        if (estados != null && !estados.isEmpty()) {
            listaObservableEstados = FXCollections.observableArrayList(estados);
            cbEstado.setItems(listaObservableEstados);
        }
    }

    @FXML
    private void btnGuardar(ActionEvent event) {
        if (validarCampos()) {
            if (this.envio == null) {
                this.envio = new Envio();
            }

            Cliente cliente = ((cbCliente.getSelectionModel().getSelectedItem() != null)
                    ? cbCliente.getSelectionModel().getSelectedItem() : null);

            Colaborador colaborador = ((cbColaborador.getSelectionModel().getSelectedItem() != null)
                    ? cbColaborador.getSelectionModel().getSelectedItem() : null);

            Sucursal sucursal = ((cbSucursal.getSelectionModel().getSelectedItem() != null)
                    ? cbSucursal.getSelectionModel().getSelectedItem() : null);

            Estado estado = ((cbEstado.getSelectionModel().getSelectedItem() != null)
                    ? cbEstado.getSelectionModel().getSelectedItem() : null);

            envio.setIdCliente(cliente.getIdCliente());
            envio.setIdColaborador(colaborador.getIdColaborador());
            envio.setIdSucursalOrigen(sucursal.getIdSucursal());
            envio.setIdEstadoDestino(estado.getIdEstado());
            envio.setIdEstadosEnvio(EstadosEnvioDAO.obtenerPorId(1).getIdEstadosEnvio());

            envio.setNombreDestinatario(tfNombre.getText());
            envio.setCalleDestino(tfCalle.getText());
            envio.setNumeroDestino(tfNumero.getText());
            envio.setColoniaDestino(tfColonia.getText());
            envio.setCpDestino(tfCp.getText());
            envio.setNumeroGuia(tfNumeroGuia.getText());
            envio.setCiudadDestino(tfCiudad.getText());

            if (btnGuardar.getText().equals("Editar")) {
                editarDatosEnvio();
            } else {
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

    private void cargarEventosCb() {
        cbSucursal.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblDireccionOrigen.setText(
                        newVal.getCalle() + " #" + newVal.getNumero() + "\n"
                        + newVal.getColonia() + " " + newVal.getCodigoPostal() + "\n"
                        + newVal.getCiudad() + ", " + EstadosDAO.obtenerEstado(newVal.getIdEstado()).getEstadoMexico()
                );
            }
        });
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

        // Limpiar errores
        labelErrorIdCliente.setText("");
        labelErrorIdColaborador.setText("");
        labelErrorIdSucursal.setText("");
        labelErrorNombre.setText("");
        labelErrorCalle.setText("");
        labelErrorNumero.setText("");
        labelErrorColonia.setText("");
        labelErrorCp.setText("");
        labelErrorCiudad.setText("");
        labelErrorEstado.setText("");

        /* ================= COMBOS ================= */
        if (cbCliente.getValue() == null) {
            labelErrorIdCliente.setText("Seleccione un cliente válido.");
            valid = false;
        }

        if (cbColaborador.getValue() == null) {
            labelErrorIdColaborador.setText("Seleccione un conductor válido.");
            valid = false;
        }

        if (cbSucursal.getValue() == null) {
            labelErrorIdSucursal.setText("Seleccione una sucursal válida.");
            valid = false;
        }

        /* ================= NOMBRE DESTINATARIO ================= */
        String nombre = tfNombre.getText() != null ? tfNombre.getText().trim() : "";
        if (nombre.isEmpty()) {
            labelErrorNombre.setText("Nombre vacío.");
            valid = false;
        } else if (nombre.length() < 3) {
            labelErrorNombre.setText("Nombre demasiado corto (mínimo 3 letras).");
            valid = false;
        } else if (nombre.length() > 30) {
            labelErrorNombre.setText("Nombre demasiado largo (máximo 30 letras).");
            valid = false;
        }

        /* ================= CALLE ================= */
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

        /* ================= NÚMERO ================= */
        String numero = tfNumero.getText() != null ? tfNumero.getText().trim() : "";
        if (numero.isEmpty()) {
            labelErrorNumero.setText("Número vacío.");
            valid = false;
        } else if (numero.length() > 10) {
            labelErrorNumero.setText("Número demasiado largo (máximo 10 caracteres).");
            valid = false;
        }

        /* ================= COLONIA ================= */
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

        /* ================= CÓDIGO POSTAL ================= */
        String cp = tfCp.getText() != null ? tfCp.getText().trim() : "";
        if (cp.isEmpty()) {
            labelErrorCp.setText("Código postal vacío.");
            valid = false;
        } else if (!cp.matches("\\d{5}")) {
            labelErrorCp.setText("Código postal inválido (5 dígitos).");
            valid = false;
        }

        /* ================= CIUDAD ================= */
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

        /* ================= ESTADO ================= */
        if (cbEstado.getValue() == null) {
            labelErrorEstado.setText("Seleccione un estado válido.");
            valid = false;
        }

        return valid;
    }

    private void guardarDatosEnvio() {
        Mensaje msj = EnviosDAO.agregarEnvio(envio);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("Registro exitoso.", "La información del envio fue registrada correctamente",
                    Alert.AlertType.INFORMATION);
            Alertas.mostrarAlertaSimple("Atencion", "Recuerde crear y agregar los paquetes al envio recien creado en la seccion Paquetes.\nDespues, regrese a esta seccion y confirme el envio en el boton COnfirmar para obtener su costo total y comenzar el recorrido.",
                    Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Error al guardar", msj.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void cargarDatos() {
        int posicion = buscarCliente(this.envio.getIdCliente());
        cbCliente.getSelectionModel().select(posicion);
        posicion = buscarColaborador(this.envio.getIdColaborador());
        cbColaborador.getSelectionModel().select(posicion);
        posicion = buscarSucursal(this.envio.getIdSucursalOrigen());
        cbSucursal.getSelectionModel().select(posicion);
        tfNombre.setText(this.envio.getNombreDestinatario());
        tfCalle.setText(this.envio.getCalleDestino());
        tfNumero.setText(this.envio.getNumeroDestino());
        tfColonia.setText(this.envio.getColoniaDestino());
        tfCp.setText(this.envio.getCpDestino());
        tfCiudad.setText(this.envio.getCiudadDestino());
        posicion = buscarIdEstado(this.envio.getIdEstadoDestino());
        cbEstado.getSelectionModel().select(posicion);
    }

    private int buscarIdEstado(int idEstado) {
        for (int i = 0; i < listaObservableEstados.size(); i++) {
            if (listaObservableEstados.get(i).getIdEstado() == idEstado) {
                return i;
            }
        }
        return 0;
    }

    private void cerrarVentana() {
        AnchorPane contenerdorPrincipal = (AnchorPane) cbCliente.getScene().lookup("#contenedorPrincipal");
        Funciones.cargarVista("/packetworldescritorio/FXMLEnvios.fxml", contenerdorPrincipal);
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
                if (colaborador.getIdRol() == 3 && colaborador.getIdUnidad() != null) {
                    conductores.add(colaborador);
                }
            }
            listaObservableColaboradores = FXCollections.observableArrayList(conductores);
            cbColaborador.setItems(listaObservableColaboradores);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar", "Lo sentiento, no se pudo obtener la informacion de los conductores",
                    Alert.AlertType.ERROR);
        }
    }

    private void cargarSucursales() {
        List<Sucursal> sucursales = SucursalDAO.obtenerSucursales();
        if (sucursales != null && !sucursales.isEmpty()) {
            listaObservableSucursales = FXCollections.observableArrayList(sucursales);
            cbSucursal.setItems(listaObservableSucursales);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar", "Lo sentiento, no se pudo obtener la informacion de las sucursales.",
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
        for (int i = 0; i < listaObservableColaboradores.size(); i++) {
            if (listaObservableColaboradores.get(i).getIdColaborador() == idColaborador) {
                return i;
            }
        }
        return 0;
    }

    private int buscarSucursal(Integer idSucursal) {
        for (int i = 0; i < listaObservableSucursales.size(); i++) {
            if (listaObservableSucursales.get(i).getIdSucursal() == idSucursal) {
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

        int numeroMax = Integer.parseInt(maxNumeroGuia.substring(4));
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
            tfNumeroGuia.setText(this.envio.getNumeroGuia());
        } else {
            btnGuardar.setText("Agregar");
            generarNumeroGuia();
        }
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonalColaborador = noPersonal;
    }

}
