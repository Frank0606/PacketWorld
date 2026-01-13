package ws;

import dominio.ImpEstadosEnvio;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.EstadosEnvio;
import pojo.Mensaje;

@Path("estados-envio")
public class WSEstadosEnvio {

    @GET
    @Path("obtener-todos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EstadosEnvio> obtenerTodos() {
        return ImpEstadosEnvio.obtenerTodos();
    }

    @GET
    @Path("obtener/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public EstadosEnvio obtenerPorId(@PathParam("id") int id) {
        return ImpEstadosEnvio.obtenerPorId(id);
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje registrar(EstadosEnvio estado) {
        return ImpEstadosEnvio.registrar(estado);
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editar(EstadosEnvio estado) {
        return ImpEstadosEnvio.editar(estado);
    }

    @DELETE
    @Path("eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminar(@PathParam("id") int id) {
        return ImpEstadosEnvio.eliminar(id);
    }
}
