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
import packetworldescritorio.modelo.dao.SucursalDAO;
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
    private Label lblDireccionDestino;
    @FXML
    private ComboBox<Sucursal> cbSucursal;
    @FXML
    private Label lblDireccionOrigen;
    @FXML
    private TextField tfNumeroGuia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarClientes();
        cargarConductores();
        cargarSucursales();
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
            
            Integer idSucursal = ((cbSucursal.getSelectionModel().getSelectedItem() != null)
                    ? cbSucursal.getSelectionModel().getSelectedItem().getIdSucursal() : null);

            envio.setIdCliente(idCliente);
            envio.setIdColaborador(idColaborador);
            envio.setIdSucursalOrigen(idSucursal);

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
        
        labelErrorIdCliente.setText("");
        labelErrorIdColaborador.setText("");
        labelErrorIdSucursal.setText("");

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
        
        // Validar colaborador
        if (cbSucursal.getValue() == null) {
            labelErrorIdSucursal.setText("Seleccione un conductor válido");
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
        int posicion = buscarCliente(this.envio.getIdCliente());
        cbCliente.getSelectionModel().select(posicion);
        posicion = buscarColaborador(this.envio.getIdColaborador());
        cbColaborador.getSelectionModel().select(posicion);
        posicion = buscarSucursal(this.envio.getIdSucursalOrigen());
        cbSucursal.getSelectionModel().select(posicion);
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
                if (colaborador.getIdRol() == 3 && (colaborador.getIdUnidad() != null || colaborador.getIdUnidad() != 0)) {
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
        } else {
            btnGuardar.setText("Agregar");
            generarNumeroGuia();
        }
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonalColaborador = noPersonal;
    }

}
