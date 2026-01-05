package packetworldescritorio;

import packetworldescritorio.modelo.dao.ColaboradoresDAO;
import packetworldescritorio.modelo.dao.Mensaje;
import packetworldescritorio.modelo.dao.RolDAO;
import packetworldescritorio.pojo.Colaborador;
import packetworldescritorio.pojo.Rol;
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

public class FXMLFormularioColaboradorController implements Initializable, ControladorPrincipal<Colaborador> {

    @FXML
    private TextField tfNombreColaborador;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfCURP;
    @FXML
    private TextField tfCorreoElectronico;
    @FXML
    private TextField tfNoPersonal;
    @FXML
    private TextField tfContrasenia;
    @FXML
    private ComboBox<Rol> cbRol;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnCancelar;

    private Colaborador colaborador;
    private ObservableList<Rol> listaObservableRoles;

    @FXML
    private Label labelErrorNombreColaborador;
    @FXML
    private Label labelErrorApellidoM;
    @FXML
    private Label labelErrorCorreo;
    @FXML
    private Label labelErrorApellidoP;
    @FXML
    private Label labelErrorCURP;
    @FXML
    private Label labelErrorNumeroPersonal;
    @FXML
    private Label labelErrorContrasenia;
    @FXML
    private Label labelErrorLicencia;
    @FXML
    private Label labelErrorRol;
    @FXML
    private TextField tfVIM;
    @FXML
    private Label txNumeroLicencia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTiposUsuario();
        configurarTextField(tfCURP, Pattern.compile("[a-zA-Z0-9]{0,18}"));
        configurarTextField(tfVIM, Pattern.compile("[a-zA-Z0-9]{0,9}"));
        configurarTextField(tfNombreColaborador, Pattern.compile("[a-zA-Z]{1,25}(\\s[a-zA-Z]{1,24})?"));
        configurarTextField(tfApellidoPaterno, Pattern.compile("[a-zA-Z]{0,50}"));
        configurarTextField(tfApellidoMaterno, Pattern.compile("[a-zA-Z]{0,50}"));
        //configurarTextField(tfCorreoElectronico, Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2}$"));
        configurarTextField(tfContrasenia, Pattern.compile(".{0,50}"));
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
        tfNombreColaborador.setText(this.colaborador.getNombre());
        tfApellidoPaterno.setText(this.colaborador.getApellidoPaterno());
        tfApellidoMaterno.setText(this.colaborador.getApellidoMaterno());
        tfCURP.setText(this.colaborador.getCurp());
        tfCorreoElectronico.setText(this.colaborador.getCorreo());
        tfNoPersonal.setText(this.colaborador.getNumeroPersonal());
        tfNoPersonal.setDisable(true);
        tfContrasenia.setText(this.colaborador.getContrasena());
        tfVIM.setText(this.colaborador.getNumeroLicencia());
        int posicion = buscarIdRol(this.colaborador.getIdRol());
        cbRol.getSelectionModel().select(posicion);
    }

    @FXML
    private void btnAgregar(ActionEvent event) {
        if (validarCampos()) {
            if (this.colaborador == null) {
                this.colaborador = new Colaborador();
            }

            Integer idRol = ((cbRol.getSelectionModel().getSelectedItem() != null) ? cbRol.getSelectionModel().getSelectedItem().getIdRol() : null);

            colaborador.setNombre(tfNombreColaborador.getText());
            colaborador.setApellidoPaterno(tfApellidoPaterno.getText());
            colaborador.setApellidoMaterno(tfApellidoMaterno.getText());
            colaborador.setCurp(tfCURP.getText().toUpperCase());
            colaborador.setCorreo(tfCorreoElectronico.getText());
            colaborador.setNumeroPersonal(tfNoPersonal.getText());
            colaborador.setContrasena(tfContrasenia.getText());
            colaborador.setIdRol(idRol);
            if (tfVIM.getText() != null && !tfVIM.getText().isEmpty()) {
                colaborador.setNumeroLicencia(tfVIM.getText().toUpperCase());
            } else {
                colaborador.setNumeroLicencia(tfVIM.getText());
            }
            colaborador.setIdRol(idRol);

            if (btnAgregar.getText().equals("Editar")) {
                editarDatosColaborador();
            } else {
                guardarDatosColaborador();
            }
        } else {
            Alertas.mostrarAlertaSimple("Problema con los datos", "Tiene datos incorrectos, revise e intente de nuevo.",
                    Alert.AlertType.ERROR);
        }
    }

    private void editarDatosColaborador() {
        Mensaje msj = ColaboradoresDAO.actualizarColaborador(colaborador);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("El cambio fue exitoso.", "La información del colaborador: " + colaborador.getNombre() + " " + colaborador.getApellidoPaterno() + ", fue actualizado(a) correctamente", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Campos obligatorios.", "Verifica los campos de tu formulario.", Alert.AlertType.WARNING);
        }
    }

    private boolean validarCampos() {
        boolean valid = true;
        labelErrorNombreColaborador.setText("");
        labelErrorApellidoP.setText("");
        labelErrorApellidoM.setText("");
        labelErrorCorreo.setText("");
        labelErrorContrasenia.setText("");
        labelErrorCURP.setText("");
        labelErrorLicencia.setText("");
        labelErrorRol.setText("");

        // Validar nombre del colaborador
        if (tfNombreColaborador.getText() == null || tfNombreColaborador.getText().trim().isEmpty()
                || tfNombreColaborador.getText().length() > 50) {
            labelErrorNombreColaborador.setText("Nombre inválido");
            valid = false;
        }

        // Validar apellido paterno
        if (tfApellidoPaterno.getText() == null || tfApellidoPaterno.getText().trim().isEmpty()
                || tfApellidoPaterno.getText().length() > 50) {
            labelErrorApellidoP.setText("Apellido Paterno inválido");
            valid = false;
        }

        // Validar apellido materno
        if (tfApellidoMaterno.getText() == null || tfApellidoMaterno.getText().trim().isEmpty()
                || tfApellidoMaterno.getText().length() > 50) {
            labelErrorApellidoM.setText("Apellido Materno inválido");
            valid = false;
        }

        // Validar correo electrónico
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,3}$";
        if (tfCorreoElectronico.getText() == null || tfCorreoElectronico.getText().trim().isEmpty()
                || tfCorreoElectronico.getText().length() > 50
                || !tfCorreoElectronico.getText().matches(emailPattern)) {
            labelErrorCorreo.setText("Correo electrónico inválido");
            valid = false;
        }

        // Validar contraseña
        if (tfContrasenia.getText() == null || tfContrasenia.getText().trim().isEmpty()
                || tfContrasenia.getText().length() > 50) {
            labelErrorContrasenia.setText("Contraseña inválida");
            valid = false;
        }

        // Validar CURP
        if (tfCURP.getText() == null || tfCURP.getText().trim().isEmpty()
                || tfCURP.getText().length() > 18) {
            labelErrorCURP.setText("CURP inválido");
            valid = false;
        }

        // Validar VIM si Rol.idRol == 3
        if (cbRol.getValue() != null) {
            if (cbRol.getValue().getIdRol() == 3) {
                if (tfVIM.getText() == null || tfVIM.getText().trim().isEmpty() || tfVIM.getText().length() > 9) {
                    labelErrorLicencia.setText("Número de licencia inválido");
                    valid = false;
                }
            } else {
                labelErrorLicencia.setText("");
            }
            labelErrorRol.setText("");
        } else {
            valid = false;
            labelErrorRol.setText("Seleccione un rol");
        }

        return valid;
    }

    private void guardarDatosColaborador() {
        Mensaje msj = ColaboradoresDAO.agregarColaborador(colaborador);
        if (!msj.isError()) {
            Alertas.mostrarAlertaSimple("Registro exitoso.", "La información del colaborador: " + colaborador.getNombre() + " " + colaborador.getApellidoPaterno() + ", fue registrado(a) correctamente", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Alertas.mostrarAlertaSimple("Error al guardar", msj.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana() {
        AnchorPane contenerdorPrincipal = (AnchorPane) tfContrasenia.getScene().lookup("#contenedorPrincipal");
        Funciones.cargarVista("/clienteescritorio/FXMLColaboradores.fxml", contenerdorPrincipal);
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cargarTiposUsuario() {
        List<Rol> roles = RolDAO.obtenerRol();
        if (roles != null && !roles.isEmpty()) {
            listaObservableRoles = FXCollections.observableArrayList(roles);
            cbRol.setItems(listaObservableRoles);
        } else {
            Alertas.mostrarAlertaSimple("Error al cargar", "Lo sentiento, no se pudo obtener la informacion de Roles",
                    Alert.AlertType.ERROR);
        }
    }

    private int buscarIdRol(int idRol) {
        for (int i = 0; i < listaObservableRoles.size(); i++) {
            if (listaObservableRoles.get(i).getIdRol() == idRol) {
                return i;
            }
        }
        return 0;
    }

    private void crearNoPersonal() {
        List<Colaborador> colaboradores = ColaboradoresDAO.obtenerColaborador();
        String maxNoPersonal = "C000";

        for (Colaborador colaborador : colaboradores) {
            String noPersonal = colaborador.getNumeroPersonal();
            if (noPersonal.compareTo(maxNoPersonal) > 0) {
                maxNoPersonal = noPersonal;
            }
        }

        int numeroMax = Integer.parseInt(maxNoPersonal.substring(1));
        int nuevoNumero = numeroMax + 1;
        String nuevoNoPersonal = "C" + String.format("%03d", nuevoNumero);
        tfNoPersonal.setText(nuevoNoPersonal);
    }

    @Override
    public void setDatos(Colaborador colaborador) {
        this.colaborador = colaborador;
        tfNoPersonal.setEditable(false);
        tfNoPersonal.setDisable(true);

        txNumeroLicencia.setVisible(false);
        tfVIM.setVisible(false);

        if (colaborador != null) {
            cargarDatos();
            btnAgregar.setText("Editar");
            if (cbRol.getSelectionModel().getSelectedItem().getIdRol() == 3) {
                tfVIM.setVisible(true);
                txNumeroLicencia.setVisible(true);
            }
            cbRol.setDisable(true);
        } else {
            btnAgregar.setText("Agregar");
            crearNoPersonal();
            cbRol.setOnAction(event -> {
                if (cbRol.getSelectionModel().getSelectedItem().getIdRol() == 3) {
                    tfVIM.setVisible(true);
                    txNumeroLicencia.setVisible(true);
                } else {
                    tfVIM.setVisible(false);
                    txNumeroLicencia.setVisible(false);
                }
            });
        }
    }
}
