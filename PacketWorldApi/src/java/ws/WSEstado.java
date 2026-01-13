package ws;

import com.google.gson.Gson;
import dominio.ImpEstado;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Estado;
import pojo.Mensaje;

@Path("estado")
public class WSEstado {

    private final Gson gson = new Gson();

    @GET
    @Path("obtener-todos")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public List<Estado> obtenerTodos() {
        return ImpEstado.obtenerEstados();
    }

    @GET
    @Path("obtener-id/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Estado obtenerPorId(@PathParam("id") int idEstado) {
        Estado e = ImpEstado.obtenerPorId(idEstado);
        if (e == null) throw new BadRequestException("Estado no encontrado.");
        return e;
    }

    @GET
    @Path("nombre/{nombre}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public List<Estado> buscarPorNombre(@PathParam("nombre") String nombre) {
        return ImpEstado.buscarPorNombre(nombre);
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Mensaje registrar(String json) {
        Estado estado = gson.fromJson(json, Estado.class);
        return ImpEstado.registrar(estado);
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Mensaje editar(String json) {
        Estado estado = gson.fromJson(json, Estado.class);
        return ImpEstado.editar(estado);
    }

    @DELETE
    @Path("eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Mensaje eliminar(@PathParam("id") int idEstado) {
        return ImpEstado.eliminar(idEstado);
    }
}