package packetworldescritorio;

import packetworldescritorio.modelo.dao.UnidadesDAO;
import packetworldescritorio.pojo.Colaborador;
import packetworldescritorio.pojo.Unidad;
import packetworldescritorio.pojo.TipoUnidad;
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

public class FXMLUnidadesController implements Initializable {

    @FXML
    private TableColumn colVin;
    @FXML
    private TableColumn colNoIdentificacion;
    @FXML
    private TableColumn colMarca;
    @FXML
    private TableColumn colModelo;
    @FXML
    private TableColumn colAnio;
    @FXML
    private TableColumn<TipoUnidad, String> colTipoUnidad;
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
    @FXML
    private Button btnAsignar;
    @FXML
    private TableView<Unidad> tablaUnidades;

    private ObservableList<Unidad> unidades;
    @FXML
    private TableColumn<Colaborador, String> colColaborador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacion();
    }

    @FXML
    private void btnBuscar(ActionEvent event) {
        if (!barraBusqueda.getText().trim().isEmpty()) {
            ObservableList<Unidad> resultadosBusqueda = FXCollections.observableArrayList();
            String textoBusqueda = barraBusqueda.getText().trim().toUpperCase();
            for (Unidad unidad : unidades) {
                String vin = unidad.getVin().chars()
                        .mapToObj(c -> Character.isLetter(c) ? Character.toUpperCase((char) c) : (char) c)
                        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                        .toString();

                String marca = unidad.getMarca().chars()
                        .mapToObj(c -> Character.isLetter(c) ? Character.toUpperCase((char) c) : (char) c)
                        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                        .toString();

                String noIdentificacion = unidad.getNoIdentificacion().chars()
                        .mapToObj(c -> Character.isLetter(c) ? Character.toUpperCase((char) c) : (char) c)
                        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                        .toString();

                String textoBusquedaUpperCase = textoBusqueda.chars()
                        .mapToObj(c -> Character.isLetter(c) ? Character.toUpperCase((char) c) : (char) c)
                        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                        .toString();

                if (vin.startsWith(textoBusquedaUpperCase)) {
                    resultadosBusqueda.add(unidad);
                } else {
                    if (marca.startsWith(textoBusquedaUpperCase)) {
                        resultadosBusqueda.add(unidad);
                    } else {
                        if (noIdentificacion.startsWith(textoBusquedaUpperCase)) {
                            resultadosBusqueda.add(unidad);
                        }
                    }
                }
            }
            if (!resultadosBusqueda.isEmpty()) {
                tablaUnidades.setItems(resultadosBusqueda);
            } else {
                Alertas.mostrarAlertaSimple("No encontrado", "No se encontró ningún unidad con el número de personal proporcionado.", Alert.AlertType.INFORMATION);
                tablaUnidades.setItems(null);
            }
        } else {
            tablaUnidades.setItems(unidades);
        }
    }

    @FXML
    private void btnAgregar(ActionEvent event) {
        Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioUnidad.fxml",
                (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), null,
                new FXMLFormularioUnidadController());
    }

    @FXML
    private void btnEditar(ActionEvent event) {
        Unidad unidad = tablaUnidades.getSelectionModel().getSelectedItem();
        if (unidad != null) {
            Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioUnidad.fxml",
                    (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), unidad,
                    new FXMLFormularioUnidadController());
        } else {
            Alertas.mostrarAlertaSimple("Seleccionar unidad.", "Para editar debes seleccionar un unidad "
                    + "de la tabla.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnEliminar(ActionEvent event) {
        Unidad unidad = tablaUnidades.getSelectionModel().getSelectedItem();
        if (unidad != null) {
            eliminarUnidad(unidad.getVin());
        } else {
            Alertas.mostrarAlertaSimple("Seleccionar unidad.", "Para eliminar debes seleccionar un unidad de la tabla.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnAsignar(ActionEvent event) {
        Unidad unidad = tablaUnidades.getSelectionModel().getSelectedItem();
        if (unidad != null) {
            Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioAsignarUnidad.fxml",
                    (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), unidad,
                    new FXMLFormularioAsignarUnidadController());
        } else {
            Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioAsignarUnidad.fxml",
                    (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), null,
                    new FXMLFormularioAsignarUnidadController());
        }
    }

    private void eliminarUnidad(String vin) {
        Unidad unidadBaja = null;

        for (Unidad unidad : unidades) {
            if (unidad.getVin().equals(vin)) {
                unidadBaja = unidad;
                break;
            }
        }

        if (unidadBaja != null) {
            if (unidadBaja.getNumeroPersonal() != null && !unidadBaja.getNumeroPersonal().isEmpty()) {
                Alertas.mostrarAlertaSimple("Error al eliminar.", "No se puede eliminar una unidad con conductor asignado. Libere la unidad primero.",
                        Alert.AlertType.WARNING);
            } else {
                Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioBajaUnidad.fxml",
                        (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), unidadBaja,
                        new FXMLFormularioBajaUnidadController());
            }
        } else {
            Alertas.mostrarAlertaSimple("Error al buscar.", "No se encontró la unidad con el VIN especificado.",
                    Alert.AlertType.WARNING);
        }
    }

    private void cargarInformacion() {
        unidades = FXCollections.observableArrayList();
        List<Unidad> WSList = UnidadesDAO.obtenerUnidad();
        if (WSList != null) {
            unidades.addAll(WSList);
            tablaUnidades.setItems(unidades);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar.",
                    "Lo sentimos, por el momento no se puede cargar la información de los unidades, por favor, intentalo más tarde.", Alert.AlertType.ERROR);
        }
    }

    private void configurarTabla() {
        colVin.setCellValueFactory(new PropertyValueFactory("vin"));
        colNoIdentificacion.setCellValueFactory(new PropertyValueFactory("noIdentificacion"));
        colMarca.setCellValueFactory(new PropertyValueFactory("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory("modelo"));
        colAnio.setCellValueFactory(new PropertyValueFactory("anio"));
        colTipoUnidad.setCellValueFactory(new PropertyValueFactory("tipoUnidad"));
        colColaborador.setCellValueFactory(new PropertyValueFactory("numeroPersonal"));
    }
}
