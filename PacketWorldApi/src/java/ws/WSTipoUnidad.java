package ws;

import dominio.ImpTipoUnidad;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.TipoUnidad;

@Path("tipoUnidad")
public class WSTipoUnidad {

    @Path("todos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TipoUnidad> obtencionRol() {
        return ImpTipoUnidad.obtenerUnidad();
    }
}
