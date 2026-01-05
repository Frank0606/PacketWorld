package packetworldescritorio;

import packetworldescritorio.modelo.dao.ColaboradoresDAO;
import packetworldescritorio.modelo.dao.EnviosDAO;
import packetworldescritorio.modelo.dao.Mensaje;
import packetworldescritorio.pojo.Colaborador;
import packetworldescritorio.pojo.Envio;
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

public class FXMLColaboradoresController implements Initializable {

    @FXML
    private TableView<Colaborador> tablaColaboradores;
    @FXML
    private TableColumn colCorreo;
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

    private ObservableList<Colaborador> colaboradores;
    private List<Envio> envios;

    @FXML
    private TableColumn colNoPersonal;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApellidoPaterno;
    @FXML
    private TableColumn colCurp;
    @FXML
    private TableColumn colRol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacion();
        cargarEnvios();
    }

    @FXML
    private void btnEliminar(ActionEvent event) {
        Colaborador colaborador = tablaColaboradores.getSelectionModel().getSelectedItem();
        if (colaborador != null) {
            eliminarColaborador(colaborador);
        } else {
            Alertas.mostrarAlertaSimple("Seleccionar colaborador.", "Para eliminar debes seleccionar un colaborador de la tabla.", Alert.AlertType.WARNING);
        }
    }

    private void eliminarColaborador(Colaborador colaborador) {
        if (colaborador.getIdRol() == 3) { // Solo verificar si es un conductor
            if (colaborador.getIdUnidad() == 0) {
                for (Envio envio : envios) {
                    if (envio.getIdColaborador() == colaborador.getIdColaborador()) {
                        Alertas.mostrarAlertaSimple("Error al eliminar", "No se puede eliminar a un conductor que "
                                + "tenga envíos asignados. Elimine los envíos primero.", Alert.AlertType.ERROR);
                        return;
                    }
                }
                Mensaje msj = ColaboradoresDAO.eliminarColaborador(colaborador.getNumeroPersonal());
                if (!msj.isError()) {
                    Alertas.mostrarAlertaSimple("Colaborador eliminado", "El colaborador ha sido eliminado correctamente.", Alert.AlertType.INFORMATION);
                    cargarInformacion();
                } else {
                    Alertas.mostrarAlertaSimple("Error al eliminar.", "No se pudo eliminar el colaborador, intente nuevamente.", Alert.AlertType.WARNING);
                }
            } else {
                Alertas.mostrarAlertaSimple("Error al eliminar", "No se puede eliminar a un conductor que "
                        + "tenga una unidad asignada. Libere la unidad primero.", Alert.AlertType.ERROR);
            }
        } else {
            Mensaje msj = ColaboradoresDAO.eliminarColaborador(colaborador.getNumeroPersonal());
            if (!msj.isError()) {
                Alertas.mostrarAlertaSimple("Colaborador eliminado", "El colaborador ha sido eliminado correctamente.", Alert.AlertType.INFORMATION);
                cargarInformacion();
            } else {
                Alertas.mostrarAlertaSimple("Error al eliminar.", "No se pudo eliminar el colaborador, intente nuevamente.", Alert.AlertType.WARNING);
            }
        }
    }

    private void cargarEnvios() {
        List<Envio> envios = EnviosDAO.obtenerEnvios();
        if (envios != null && !envios.isEmpty()) {
            this.envios = FXCollections.observableArrayList(envios);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar", "Lo sentiento, no se pudo obtener la informacion de Roles",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnEditar(ActionEvent event) {
        Colaborador colaborador = tablaColaboradores.getSelectionModel().getSelectedItem();
        if (colaborador != null) {
            Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioColaborador.fxml",
                    (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), colaborador,
                    new FXMLFormularioColaboradorController());
        } else {
            Alertas.mostrarAlertaSimple("Seleccionar colaborador.", "Para editar debes seleccionar un colaborador "
                    + "de la tabla.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnAgregar(ActionEvent event) {
        Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioColaborador.fxml",
                (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), null,
                new FXMLFormularioColaboradorController());
    }

    private void configurarTabla() {
        colNoPersonal.setCellValueFactory(new PropertyValueFactory("noPersonal"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colCurp.setCellValueFactory(new PropertyValueFactory("curp"));
        colRol.setCellValueFactory(new PropertyValueFactory("nombreRol"));
    }

    private void cargarInformacion() {
        colaboradores = FXCollections.observableArrayList();
        List<Colaborador> WSList = ColaboradoresDAO.obtenerColaborador();
        if (WSList != null) {
            colaboradores.addAll(WSList);
            tablaColaboradores.setItems(colaboradores);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar.",
                    "Lo sentimos, por el momento no se puede cargar la información de los colaboradores, por favor, intentalo más tarde.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnBuscar(ActionEvent event) {
        if (!barraBusqueda.getText().trim().isEmpty()) {
            ObservableList<Colaborador> resultadosBusqueda = FXCollections.observableArrayList();
            String textoBusqueda = barraBusqueda.getText().trim().toUpperCase();
            for (Colaborador colaborador : colaboradores) {
                String noPersonal = colaborador.getNumeroPersonal().chars()
                        .mapToObj(c -> Character.isLetter(c) ? Character.toUpperCase((char) c) : (char) c)
                        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                        .toString();

                String nombre = colaborador.getNombre().chars()
                        .mapToObj(c -> Character.isLetter(c) ? Character.toUpperCase((char) c) : (char) c)
                        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                        .toString();

                String rol = colaborador.getNombreRol().chars()
                        .mapToObj(c -> Character.isLetter(c) ? Character.toUpperCase((char) c) : (char) c)
                        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                        .toString();

                String barraBusquedaTexto = barraBusqueda.getText().trim().chars().mapToObj(c -> Character.isLetter(c)
                        ? Character.toUpperCase((char) c) : (char) c).collect(StringBuilder::new, StringBuilder::append,
                        StringBuilder::append).toString();

                if (noPersonal.startsWith(barraBusquedaTexto)) {
                    resultadosBusqueda.add(colaborador);
                } else {
                    if (nombre.startsWith(barraBusquedaTexto)) {
                        resultadosBusqueda.add(colaborador);
                    } else {
                        if (rol.startsWith(barraBusquedaTexto)) {
                            resultadosBusqueda.add(colaborador);
                        }
                    }
                }
            }
            if (!resultadosBusqueda.isEmpty()) {
                tablaColaboradores.setItems(resultadosBusqueda);
            } else {
                Alertas.mostrarAlertaSimple("No encontrado", "No se encontró ningún colaborador con el número de personal proporcionado.", Alert.AlertType.INFORMATION);
                tablaColaboradores.setItems(null);
            }
        } else {
            tablaColaboradores.setItems(colaboradores);
        }
    }
}
