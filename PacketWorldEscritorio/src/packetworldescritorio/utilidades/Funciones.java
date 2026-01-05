package packetworldescritorio.utilidades;

import packetworldescritorio.FXMLEnviosController;
import packetworldescritorio.FXMLFormularioEnviosController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Funciones {

    public static <T> void cargarVistaConDatos(String fxml, AnchorPane contenedorPrincipal, T datos, ControladorPrincipal<T> controlador) {
        try {
            FXMLLoader loader = new FXMLLoader(Funciones.class.getResource(fxml));
            AnchorPane newLoadedPane = loader.load();
            controlador = loader.getController();
            controlador.setDatos(datos);
            contenedorPrincipal.getChildren().clear();
            contenedorPrincipal.getChildren().add(newLoadedPane);
            AnchorPane.setTopAnchor(newLoadedPane, 0.0);
            AnchorPane.setBottomAnchor(newLoadedPane, 0.0);
            AnchorPane.setLeftAnchor(newLoadedPane, 0.0);
            AnchorPane.setRightAnchor(newLoadedPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void cargarVista(String fxml, AnchorPane contenedorPrincipal) {
        try {
            FXMLLoader loader = new FXMLLoader(Funciones.class.getResource(fxml));
            AnchorPane newLoadedPane = loader.load();
            contenedorPrincipal.getChildren().clear();
            contenedorPrincipal.getChildren().add(newLoadedPane);
            AnchorPane.setTopAnchor(newLoadedPane, 0.0);
            AnchorPane.setBottomAnchor(newLoadedPane, 0.0);
            AnchorPane.setLeftAnchor(newLoadedPane, 0.0);
            AnchorPane.setRightAnchor(newLoadedPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void cargarVista(String fxml, AnchorPane contenedorPrincipal, String noPersonal) {
        try {
            FXMLLoader loader = new FXMLLoader(Funciones.class.getResource(fxml));
            AnchorPane newLoadedPane = loader.load();
            if (fxml.equals("/clienteescritorio/FXMLEnvios.fxml") && noPersonal != null) {
                FXMLEnviosController enviosController = loader.getController();
                enviosController.setNoPersonal(noPersonal);
            }
            contenedorPrincipal.getChildren().clear();
            contenedorPrincipal.getChildren().add(newLoadedPane);
            AnchorPane.setTopAnchor(newLoadedPane, 0.0);
            AnchorPane.setBottomAnchor(newLoadedPane, 0.0);
            AnchorPane.setLeftAnchor(newLoadedPane, 0.0);
            AnchorPane.setRightAnchor(newLoadedPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void cargarVistaConDatos(String fxml, AnchorPane contenedorPrincipal, T datos, 
            String noPersonal, ControladorPrincipal<T> controlador) {
        try {
            FXMLLoader loader = new FXMLLoader(Funciones.class.getResource(fxml));
            AnchorPane newLoadedPane = loader.load();
            controlador = loader.getController();
            if (controlador instanceof FXMLFormularioEnviosController && noPersonal != null) {
                ((FXMLFormularioEnviosController) controlador).setNoPersonal(noPersonal);
            }
            controlador.setDatos(datos);
            contenedorPrincipal.getChildren().clear();
            contenedorPrincipal.getChildren().add(newLoadedPane);
            AnchorPane.setTopAnchor(newLoadedPane, 0.0);
            AnchorPane.setBottomAnchor(newLoadedPane, 0.0);
            AnchorPane.setLeftAnchor(newLoadedPane, 0.0);
            AnchorPane.setRightAnchor(newLoadedPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void cargarStage(String fxml, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Funciones.class.getResource(fxml));
            AnchorPane newLoadedPane = loader.load();
            Scene scene = new Scene(newLoadedPane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
