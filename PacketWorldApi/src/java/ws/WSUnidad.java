package ws;

import dominio.ImpUnidad;
import com.google.gson.Gson;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.Mensaje;
import pojo.Unidad;

@Path("unidad")
public class WSUnidad {

    @GET
    @Path("obtener-todos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Unidad> obtenerTodos() {
        return ImpUnidad.obtenerTodos();
    }

    @GET
    @Path("obtener/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Unidad obtener(@PathParam("id") int id) {
        return ImpUnidad.obtenerPorId(id);
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje registrar(String json) {
        Unidad unidad = new Gson().fromJson(json, Unidad.class);
        return ImpUnidad.registrar(unidad);
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editar(String json) {
        Unidad unidad = new Gson().fromJson(json, Unidad.class);
        return ImpUnidad.editar(unidad);
    }

    @DELETE
    @Path("eliminar/{vin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminar(@PathParam("vin") String vin) {
        return ImpUnidad.eliminar(vin);
    }
}
