package packetworldescritorio;

import packetworldescritorio.modelo.dao.EnviosDAO;
import packetworldescritorio.modelo.dao.PaquetesDAO;
import packetworldescritorio.modelo.dao.Mensaje;
import packetworldescritorio.pojo.Envio;
import packetworldescritorio.pojo.Paquete;
import packetworldescritorio.utilidades.Alertas;
import packetworldescritorio.utilidades.Funciones;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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
import static packetworldescritorio.utilidades.Alertas.mostrarAlertaConfirmacion;

public class FXMLPaquetesController implements Initializable {

    @FXML
    private TableView<Paquete> tablaPaquetes;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colPeso;
    @FXML
    private TableColumn colProfundidad;
    @FXML
    private TableColumn colAlto;
    @FXML
    private TableColumn colAncho;
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

    private ObservableList<Paquete> paquetes;
    private List<Envio> envios;

    @FXML
    private TableColumn colNumeroGuia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacion();
        cargarEnvios();
    }

    private void cargarEnvios() {
        List<Envio> envios = EnviosDAO.obtenerEnvios();
        if (envios != null && !envios.isEmpty()) {
            this.envios = FXCollections.observableArrayList(envios);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar", "Lo sentiento, no se pudo obtener la informacion de Envios",
                    Alert.AlertType.ERROR);
        }
    }

    private void configurarTabla() {
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colProfundidad.setCellValueFactory(new PropertyValueFactory<>("profundidad"));
        colAlto.setCellValueFactory(new PropertyValueFactory<>("alto"));
        colAncho.setCellValueFactory(new PropertyValueFactory<>("ancho"));
        colNumeroGuia.setCellValueFactory(new PropertyValueFactory<>("numeroGuia"));
    }

    private void cargarInformacion() {
        paquetes = FXCollections.observableArrayList();
        List<Paquete> WSList = PaquetesDAO.obtenerPaquetes();
        if (WSList != null) {
            paquetes.addAll(WSList);
            tablaPaquetes.setItems(paquetes);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar.",
                    "Lo sentimos, por el momento no se puede cargar la información "
                    + "de los paquetes, por favor, inténtalo más tarde.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnBuscar(ActionEvent event) {
        if (!barraBusqueda.getText().trim().isEmpty()) {
            ObservableList<Paquete> resultadosBusqueda = FXCollections.observableArrayList();
            String textoBusqueda = barraBusqueda.getText().trim().toUpperCase();

            for (Paquete paquete : paquetes) {
                for (Envio envio : envios) {
                    if (envio.getIdEnvio() == paquete.getIdEnvio()) {
                        paquete.setNumeroGuia(envio.getNumeroGuia().toUpperCase());
                        break;
                    }
                }
            }

            for (Paquete paquete : paquetes) {
                if (paquete.getNumeroGuia().contains(textoBusqueda)) {
                    resultadosBusqueda.add(paquete);
                }
            }

            if (!resultadosBusqueda.isEmpty()) {
                tablaPaquetes.setItems(resultadosBusqueda);
            } else {
                Alertas.mostrarAlertaSimple("No encontrado", "No se encontró ningún paquete con el número de guía proporcionado.", Alert.AlertType.INFORMATION);
            }
        } else {
            tablaPaquetes.setItems(paquetes);
        }
    }

    @FXML
    private void btnAgregar(ActionEvent event) {
        Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioPaquetes.fxml",
                (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), null,
                new FXMLFormularioPaquetesController());
    }

    @FXML
    private void btnEditar(ActionEvent event) {
        Paquete paquete = tablaPaquetes.getSelectionModel().getSelectedItem();
        if (paquete != null) {
            Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioPaquetes.fxml",
                    (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), paquete,
                    new FXMLFormularioPaquetesController());
        } else {
            Alertas.mostrarAlertaSimple("Seleccionar paquete.", "Para editar debes seleccionar un paquete de la tabla.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnEliminar(ActionEvent event) {
        Paquete paquete = tablaPaquetes.getSelectionModel().getSelectedItem();
        if (paquete != null) {
            eliminarPaquete(paquete.getIdPaquete());
        } else {
            Alertas.mostrarAlertaSimple("Seleccionar paquete.", "Para eliminar debes seleccionar un paquete de la tabla.", Alert.AlertType.WARNING);
        }
    }

    private void eliminarPaquete(int idPaquete) {
        boolean confirmado = mostrarAlertaConfirmacion("Confirmar eliminación",
                "¿Está seguro de que desea eliminar este paquete?");
        if (confirmado) {
            confirmado = mostrarAlertaConfirmacion("Confirmar eliminación",
                    "¿Realmente está seguro de que desea eliminar este paquete?");
            if (confirmado) {
                Mensaje msj = PaquetesDAO.eliminarPaquete(idPaquete);
                if (!msj.isError()) {
                    Alertas.mostrarAlertaSimple("Paquete eliminado", "El paquete ha sido eliminado correctamente.", Alert.AlertType.INFORMATION);
                    cargarInformacion();
                } else {
                    Alertas.mostrarAlertaSimple("Error al eliminar.", msj.getMensaje(), Alert.AlertType.WARNING);
                }
            }
        }
    }
}
