package packetworldescritorio;

import packetworldescritorio.modelo.dao.EnviosDAO;
import packetworldescritorio.modelo.dao.ColaboradoresDAO;
import packetworldescritorio.modelo.dao.Mensaje;
import packetworldescritorio.modelo.dao.PaquetesDAO;
import packetworldescritorio.pojo.Colaborador;
import packetworldescritorio.pojo.Envio;
import packetworldescritorio.pojo.Paquete;
import packetworldescritorio.utilidades.Alertas;
import packetworldescritorio.utilidades.Funciones;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class FXMLEnviosController implements Initializable {

    @FXML
    private TableView<Envio> tablaEnvios;
    @FXML
    private TableColumn<Envio, String> colOrigen;
    @FXML
    private TableColumn colNumeroGuia;
    @FXML
    private TableColumn colCostoEnvio;
    @FXML
    private TableColumn colEstatus;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private TextField barraBusqueda;

    private ObservableList<Envio> envios;
    private List<Paquete> paquetes;
    private String noPersonalColaborador;

    @FXML
    private TableColumn colDestino;
    @FXML
    private TableColumn colNombreCliente;
    @FXML
    private TableColumn<Envio, String> colConductor;
    @FXML
    private Button btnEstatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacion();
        cargarPaquetes();
    }

    private void configurarTabla() {

        colConductor.setCellValueFactory(cellData -> {
            Envio envio = cellData.getValue();

            Colaborador colaborador = ColaboradoresDAO.obtenerColaboradorId(envio.getIdColaborador());

            String nombreColaborador = "";
            if (colaborador != null) {
                nombreColaborador = colaborador.getNombre() + " "
                        + colaborador.getApellidoPaterno() + " "
                        + colaborador.getApellidoMaterno();
            }

            return new SimpleStringProperty(nombreColaborador);
        });
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        colOrigen.setCellValueFactory(cellData -> {
            Envio envio = cellData.getValue();
            String direccionCompleta = envio.getCalleDestino() + " " + envio.getNumeroDestino() + ", "
                    + envio.getColoniaDestino() + ", " + envio.getCpDestino();
            return new SimpleStringProperty(direccionCompleta);
        });
        colNumeroGuia.setCellValueFactory(new PropertyValueFactory<>("numeroGuia"));
        colCostoEnvio.setCellValueFactory(new PropertyValueFactory<>("costoTotal"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        colNombreCliente.setCellValueFactory(new PropertyValueFactory<>("nombreDestino"));
    }

    private void cargarInformacion() {
        envios = FXCollections.observableArrayList();
        List<Envio> WSList = EnviosDAO.obtenerEnvios();
        if (WSList != null) {
            envios.addAll(WSList);
            tablaEnvios.setItems(envios);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar.",
                    "Lo sentimos, por el momento no se puede cargar la información "
                    + "de los envíos, por favor, inténtalo más tarde.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnBuscar(ActionEvent event) {
        if (!barraBusqueda.getText().trim().isEmpty()) {
            ObservableList<Envio> resultadosBusqueda = FXCollections.observableArrayList();
            for (Envio envio : envios) {
                String idNumeroGuia = envio.getNumeroGuia().chars().mapToObj(c -> Character.isLetter(c)
                        ? Character.toUpperCase((char) c) : (char) c).collect(StringBuilder::new,
                        StringBuilder::append, StringBuilder::append).toString();

                String barraBusquedaTexto = barraBusqueda.getText().trim().chars().mapToObj(c -> Character.isLetter(c)
                        ? Character.toUpperCase((char) c) : (char) c).collect(StringBuilder::new, StringBuilder::append,
                        StringBuilder::append).toString();

                if (idNumeroGuia.startsWith(barraBusquedaTexto)) {
                    resultadosBusqueda.add(envio);
                }
            }
            if (!resultadosBusqueda.isEmpty()) {
                tablaEnvios.setItems(resultadosBusqueda);
            } else {
                Alertas.mostrarAlertaSimple("No encontrado", "No se encontró ningún envío con el ID proporcionado.", Alert.AlertType.INFORMATION);
            }
        } else {
            tablaEnvios.setItems(envios);
        }
    }

    @FXML
    private void btnAgregar(ActionEvent event) {
        Funciones.cargarVistaConDatos("/clienteescritorio/FXMLFormularioEnvios.fxml",
                (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), null, noPersonalColaborador,
                new FXMLFormularioEnviosController());
    }

    @FXML
    private void btnEditar(ActionEvent event) {
        Envio envio = tablaEnvios.getSelectionModel().getSelectedItem();
        if (envio != null) {
            Funciones.cargarVistaConDatos("/clienteescritorio/FXMLFormularioEnvios.fxml",
                    (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), envio,
                    new FXMLFormularioEnviosController());
        } else {
            Alertas.mostrarAlertaSimple("Seleccionar envío.", "Para editar debes seleccionar un envío de la tabla.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnEliminar(ActionEvent event) {
        Envio envio = tablaEnvios.getSelectionModel().getSelectedItem();
        if (envio != null) {
            eliminarEnvio(envio.getNumeroGuia());
        } else {
            Alertas.mostrarAlertaSimple("Seleccionar envío.", "Para eliminar debes seleccionar un envío de la tabla.", Alert.AlertType.WARNING);
        }
    }

    private void eliminarEnvio(String numeroGuia) {
        Mensaje msj = EnviosDAO.eliminarEnvio(numeroGuia);
        for (Paquete paquete : paquetes) {
            if (paquete.getNumeroGuia().equals(numeroGuia)) {
                Alertas.mostrarAlertaSimple("Error al eliminar.", "El envio tiene paquetes asociados. "
                        + "Eliminalos para poner eliminar el envio.", Alert.AlertType.WARNING);
            }
            return;
        }

        if (!msj.isError()) {
            System.out.println("No hay errores");
            Alertas.mostrarAlertaSimple("Envío eliminado", "El envío ha sido eliminado correctamente.", Alert.AlertType.INFORMATION);
            cargarInformacion();
        } else {
            Alertas.mostrarAlertaSimple("Error al eliminar.", "No se pudo eliminar el envio. Intente de nuevo mas tarde.", Alert.AlertType.WARNING);
        }
    }

    private void cargarPaquetes() {
        List<Paquete> paquetes = PaquetesDAO.obtenerPaquetes();
        if (paquetes != null && !paquetes.isEmpty()) {
            this.paquetes = FXCollections.observableArrayList(paquetes);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar", "Lo sentiento, no se pudo obtener la informacion de paquetes",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnEstatus(ActionEvent event) {
        Envio envio = tablaEnvios.getSelectionModel().getSelectedItem();
        if (envio != null) {
            envio.setCalleDestino(noPersonalColaborador);
            Funciones.cargarVistaConDatos("/clienteescritorio/FXMLFormularioCambiarEstatus.fxml",
                    (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), envio,
                    new FXMLFormularioCambiarEstatusController());
        } else {
            Alertas.mostrarAlertaSimple("Seleccionar envío.", "Para editar debes seleccionar un envío de la tabla.", Alert.AlertType.WARNING);
        }
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonalColaborador = noPersonal;
    }
}
