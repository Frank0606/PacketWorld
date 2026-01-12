package packetworldescritorio;

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
import packetworldescritorio.pojo.Mensaje;
import packetworldescritorio.modelo.dao.SucursalDAO;
import packetworldescritorio.pojo.Sucursal;
import packetworldescritorio.utilidades.Alertas;
import static packetworldescritorio.utilidades.Alertas.mostrarAlertaConfirmacion;
import packetworldescritorio.utilidades.Funciones;

public class FXMLSucursalController implements Initializable {

    @FXML
    private TableView<Sucursal> tablaSucursales;
    @FXML
    private TableColumn colCodigo;
    @FXML
    private TableColumn colNombreCorto;
    @FXML
    private TableColumn colEstatus;
    @FXML
    private TableColumn colCiudad;
    @FXML
    private TableColumn colEstado;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnBuscar;

    private ObservableList<Sucursal> sucursales;

    @FXML
    private TableColumn<Sucursal, String> colDireccion;
    @FXML
    private TextField barraBusqueda;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacion();
    }
    
    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory("codigo"));
        colNombreCorto.setCellValueFactory(new PropertyValueFactory("nombreCorto"));
        colDireccion.setCellValueFactory(cellData -> {
            Sucursal c = cellData.getValue();
            String nombreCompleto = c.getCalle() + " " + c.getNumero() + " " + c.getColonia();
            return new SimpleStringProperty(nombreCompleto);
        });

        colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        colCiudad.setCellValueFactory(new PropertyValueFactory("ciudad"));
        colEstado.setCellValueFactory(new PropertyValueFactory("estado"));
    }
    
    private void cargarInformacion() {
        sucursales = FXCollections.observableArrayList();
        List<Sucursal> WSList = SucursalDAO.obtenerSucursales();
        if (WSList != null) {
            sucursales.addAll(WSList);
            tablaSucursales.setItems(sucursales);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar.",
                    "Lo sentimos, por el momento no se puede cargar la información de las sucursales, por favor, intentalo más tarde.", Alert.AlertType.ERROR);
        }
        barraBusqueda.setText("");
    }

    @FXML
    private void btnAgregar(ActionEvent event) {
        Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioSucursal.fxml",
                (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), null,
                new FXMLFormularioSucursalController());
    }

    @FXML
    private void btnEditar(ActionEvent event) {
        Sucursal sucursal = tablaSucursales.getSelectionModel().getSelectedItem();
        if (sucursal != null) {
            Funciones.cargarVistaConDatos("/packetworldescritorio/FXMLFormularioSucursal.fxml",
                    (AnchorPane) barraBusqueda.getScene().lookup("#contenedorPrincipal"), sucursal,
                    new FXMLFormularioSucursalController());
        } else {
            Alertas.mostrarAlertaSimple("Seleccionar sucursal.", "Para editar debes seleccionar una sucursal "
                    + "de la tabla.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnEliminar(ActionEvent event) {
        Sucursal sucursal = tablaSucursales.getSelectionModel().getSelectedItem();
        if (sucursal != null) {
            eliminarSucursal(sucursal);
        } else {
            Alertas.mostrarAlertaSimple("Seleccionar sucursal.", "Para eliminar debes seleccionar un sucursal de la tabla.", Alert.AlertType.WARNING);
        }
    }

    private void eliminarSucursal(Sucursal sucursal) {
        Mensaje msj = new Mensaje();
        boolean confirmado = mostrarAlertaConfirmacion("Confirmar eliminación",
                "¿Está seguro de que desea eliminar esta sucursal?");
        if (confirmado) {
            confirmado = mostrarAlertaConfirmacion("Confirmar eliminación",
                    "¿Realmente está seguro de que desea eliminar esta sucursal?");
            if (confirmado) {
                msj = SucursalDAO.darBajaSucursal(sucursal.getIdSucursal());
                if (!msj.isError()) {
                    Alertas.mostrarAlertaSimple("Sucursal eliminada", "La sucursal ha sido eliminado correctamente.", Alert.AlertType.INFORMATION);
                    cargarInformacion();
                } else {
                    Alertas.mostrarAlertaSimple("Error al eliminar.", "No se pudo eliminar la sucursal, intente nuevamente.", Alert.AlertType.WARNING);
                }
            }
        } else {
            Alertas.mostrarAlertaSimple("Error al eliminar.", "No se pudo eliminar el sucursal, intente nuevamente.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnBuscar(ActionEvent event) {
        if (!barraBusqueda.getText().trim().isEmpty()) {
            ObservableList<Sucursal> resultadosBusqueda = FXCollections.observableArrayList();
            String barraBusquedaTexto = barraBusqueda.getText().trim().toUpperCase();

            for (Sucursal sucursal : sucursales) {
                String codigo = sucursal.getCodigo().toUpperCase();
                String nombreCorto = sucursal.getNombreCorto().toUpperCase();
                String estatus = sucursal.getEstatus().toUpperCase();
                String estado = sucursal.getEstado().toUpperCase();

                if (codigo.contains(barraBusquedaTexto)
                        || nombreCorto.contains(barraBusquedaTexto)
                        || estatus.contains(barraBusquedaTexto)
                        || estatus.contains(barraBusquedaTexto)) {
                    resultadosBusqueda.add(sucursal);
                }
            }

            if (!resultadosBusqueda.isEmpty()) {
                tablaSucursales.setItems(resultadosBusqueda);
            } else {
                Alertas.mostrarAlertaSimple("No encontrada",
                        "No se encontró ningúna sucursal con el criterio proporcionado.",
                        Alert.AlertType.INFORMATION);
                tablaSucursales.setItems(null);
            }
        } else {
            tablaSucursales.setItems(sucursales);
        }
    }

}
