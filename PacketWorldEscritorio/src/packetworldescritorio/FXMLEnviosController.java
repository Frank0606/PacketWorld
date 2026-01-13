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
import packetworldescritorio.modelo.dao.ClientesDAO;
import packetworldescritorio.modelo.dao.EstadosDAO;
import packetworldescritorio.modelo.dao.EstadosEnvioDAO;
import packetworldescritorio.modelo.dao.SucursalDAO;
import packetworldescritorio.pojo.Cliente;
import packetworldescritorio.pojo.Sucursal;
import static packetworldescritorio.utilidades.Alertas.mostrarAlertaConfirmacion;

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
    private TableColumn<Envio, String> colEstatus;
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
    private TableColumn<Envio, String> colDestino;
    @FXML
    private TableColumn<Envio, String> colNombreCliente;
    @FXML
    private TableColumn<Envio, String> colConductor;
    @FXML
    private Button btnEstatus;
    @FXML
    private Button btnConfirmar;

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
        colOrigen.setCellValueFactory(cellData -> {
            Envio envio = cellData.getValue();
            Sucursal sucursalDestino = SucursalDAO.obtenerPorId(envio.getIdSucursalOrigen());
            if (sucursalDestino != null) {
                String direccionCompleta = sucursalDestino.getCalle() + " " + sucursalDestino.getNumero() + ", "
                        + sucursalDestino.getColonia() + ", " + sucursalDestino.getCodigoPostal() + ", "
                        + sucursalDestino.getCiudad() + ", " + EstadosDAO.obtenerEstado(sucursalDestino.getIdEstado()).getEstadoMexico();
                return new SimpleStringProperty(direccionCompleta);
            } else {
                return new SimpleStringProperty("Sucursal no encontrada");
            }
        });
        
        //Esto se tiene que modificar con la nueva direccion que se le de al envio que no es el del cliente
        colDestino.setCellValueFactory(cellData -> {
            Envio envio = cellData.getValue();
            String direccionCompleta = envio.getCalleDestino() + " " + envio.getNumeroDestino() + ", "
                    + envio.getColoniaDestino() + ", " + envio.getCpDestino() + ", "
                    + envio.getCiudadDestino() + ", " + EstadosDAO.obtenerEstado(envio.getIdEstadoDestino()).getEstadoMexico();
            return new SimpleStringProperty(direccionCompleta);
        });
        
        
        
        colNumeroGuia.setCellValueFactory(new PropertyValueFactory<>("numeroGuia"));
        colCostoEnvio.setCellValueFactory(new PropertyValueFactory<>("costoTotal"));
        colEstatus.setCellValueFactory(cellData -> {
            Envio envio = cellData.getValue();
            String estadoEnvio = EstadosEnvioDAO.obtenerPorId(envio.getIdEstadosEnvio()).getEstadoEnvio();
            return new SimpleStringProperty(estadoEnvio);
        });
        colNombreCliente.setCellValueFactory(cellData -> {
            Envio envio = cellData.getValue();
            Cliente cliente = ClientesDAO.obtenerClientesId(envio.getIdCliente());
            return new SimpleStringProperty(cliente.getNombre() + " " + cliente.getApellidoPaterno() + " " + cliente.getApellidoMaterno());
        });
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
            String barraBusquedaTexto = barraBusqueda.getText().trim().toUpperCase();
            
            for (Envio envio : envios) {
                String idNumeroGuia = envio.getNumeroGuia().toUpperCase();

                if (idNumeroGuia.contains(barraBusquedaTexto)) {
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
        Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioEnvios.fxml",
                (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), null, noPersonalColaborador,
                new FXMLFormularioEnviosController());
    }

    @FXML
    private void btnEditar(ActionEvent event) {
        Envio envio = tablaEnvios.getSelectionModel().getSelectedItem();
        if (envio != null) {
            Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioEnvios.fxml",
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
        boolean confirmado = mostrarAlertaConfirmacion("Confirmar eliminación",
                "¿Está seguro de que desea eliminar este envio?");
        if (confirmado) {
            confirmado = mostrarAlertaConfirmacion("Confirmar eliminación",
                    "¿Realmente está seguro de que desea eliminar este envio?");
            if (confirmado) {
                Mensaje msj = EnviosDAO.eliminarEnvio(numeroGuia);
                for (Paquete paquete : paquetes) {
                    if (paquete.getNumeroGuia().equals(numeroGuia)) {
                        Alertas.mostrarAlertaSimple("Error al eliminar.",
                                "El envío tiene paquetes asociados. Elimínalos primero.", Alert.AlertType.WARNING);
                        return;
                    }
                }
                if (!msj.isError()) {
                    Alertas.mostrarAlertaSimple("Envío eliminado", "El envío ha sido eliminado correctamente.", Alert.AlertType.INFORMATION);
                    cargarInformacion();
                } else {
                    Alertas.mostrarAlertaSimple("Error al eliminar.", "No se pudo eliminar el envio. Intente de nuevo mas tarde.", Alert.AlertType.WARNING);
                }
            }
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
            if (!EstadosEnvioDAO.obtenerPorId(envio.getIdEstadosEnvio()).getEstadoEnvio().equals("recibido en sucursal")) {
                if (!EstadosEnvioDAO.obtenerPorId(envio.getIdEstadosEnvio()).getEstadoEnvio().equals("entregado")) {
                    envio.setCalleDestino(noPersonalColaborador);
                    Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioCambiarEstatus.fxml",
                            (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), envio,
                            new FXMLFormularioCambiarEstatusController());
                } else {
                    Alertas.mostrarAlertaSimple("Acción incorrecta", "No se puede cambiar el estado del envio a un envio que ya esta entregado.", Alert.AlertType.WARNING);
                }
            } else {
                Alertas.mostrarAlertaSimple("Acción incorrecta", "No se puede cambiar el estado del envio a un envio que no ha sido confirmado.", Alert.AlertType.WARNING);
            }
        } else {
            Alertas.mostrarAlertaSimple("Seleccionar envío.", "Para editar debes seleccionar un envío de la tabla.", Alert.AlertType.WARNING);
        }
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonalColaborador = noPersonal;
    }

    @FXML
    private void btnCompletarCostos(ActionEvent event) {
        Envio envio = tablaEnvios.getSelectionModel().getSelectedItem();
        Mensaje msj = new Mensaje();
        if (envio != null) {
            if (EstadosEnvioDAO.obtenerPorId(envio.getIdEstadosEnvio()).getEstadoEnvio().equals("recibido en sucursal")) {
                boolean confirmado = mostrarAlertaConfirmacion("Confirmar paquetes",
                        "¿Está seguro de continuar con la asignacion de costos del envio?\nAsegurese de tener todos lo paquetes correspondientes asignados a este envio");
                if (confirmado) {
                    msj = EnviosDAO.completarCostos(envio);
                    if (!msj.isError()) {
                        Alertas.mostrarAlertaSimple("Envío completado", "El costo total del envio es de: $" + EnviosDAO.obtenerEnvioGuia(envio.getNumeroGuia()).getCostoTotal() + "\nSu envio ya comenzo.", Alert.AlertType.INFORMATION);
                        cargarInformacion();
                    } else {
                        Alertas.mostrarAlertaSimple("Error al comfirmar.", "No se pudo confirmar el envio. Intente de nuevo mas tarde.\n" + msj.getMensaje(), Alert.AlertType.WARNING);
                    }
                }
            } else {
                Alertas.mostrarAlertaSimple("Envio ya confirmado", "El envio seleccionado ya fue confirmado y cuenta con costos. Seleccione uno sin confirmar.", Alert.AlertType.WARNING);
            }
        } else {
            Alertas.mostrarAlertaSimple("Seleccionar envío.", "Para confirmar un envio debes seleccionar uno de la tabla.", Alert.AlertType.WARNING);
        }
    }
}
