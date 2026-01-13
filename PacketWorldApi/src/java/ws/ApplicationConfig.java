package ws;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.WSBajaUnidad.class);
        resources.add(ws.WSCliente.class);
        resources.add(ws.WSColaborador.class);
        resources.add(ws.WSConductorUnidad.class);
        resources.add(ws.WSConsultaEnvio.class);
        resources.add(ws.WSEnvio.class);
        resources.add(ws.WSEnvioConductor.class);
        resources.add(ws.WSEnvioHistorial.class);
        resources.add(ws.WSEstado.class);
        resources.add(ws.WSEstadosEnvio.class);
        resources.add(ws.WSLogin.class);
        resources.add(ws.WSPaquete.class);
        resources.add(ws.WSRol.class);
        resources.add(ws.WSSucursal.class);
        resources.add(ws.WSTipoUnidad.class);
        resources.add(ws.WSUnidad.class);
    }
    
}
