package ws;

import dominio.ImpTipoUnidad;
import com.google.gson.Gson;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.Mensaje;
import pojo.TipoUnidad;

@Path("tipounidad")
public class WSTipoUnidad {

    @GET
    @Path("obtener-Todos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TipoUnidad> obtenerTodos() {
        return ImpTipoUnidad.obtenerTodos();
    }

    @GET
    @Path("obtener/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TipoUnidad obtener(@PathParam("id") int id) {
        return ImpTipoUnidad.obtenerPorId(id);
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje registrar(String json) {
        TipoUnidad tipo = new Gson().fromJson(json, TipoUnidad.class);
        return ImpTipoUnidad.registrar(tipo);
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editar(String json) {
        TipoUnidad tipo = new Gson().fromJson(json, TipoUnidad.class);
        return ImpTipoUnidad.editar(tipo);
    }

    @DELETE
    @Path("eliminar{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminar(@PathParam("id") int id) {
        return ImpTipoUnidad.eliminar(id);
    }
}
