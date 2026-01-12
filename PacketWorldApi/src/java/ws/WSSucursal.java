package ws;

import dominio.ImpSucursal;
import com.google.gson.Gson;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.Mensaje;
import pojo.Sucursal;

@Path("sucursal")
public class WSSucursal {

    private final Gson gson = new Gson();

    @GET
    @Path("obtener-todos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sucursal> obtenerSucursales() {
        return ImpSucursal.obtenerSucursales();
    }

    @GET
    @Path("obtener/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Sucursal obtenerPorId(@PathParam("id") int id) {
        return ImpSucursal.obtenerPorId(id);
    }

    @GET
    @Path("codigo/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Sucursal obtenerPorCodigo(@PathParam("codigo") String codigo) {
        return ImpSucursal.obtenerPorCodigo(codigo);
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje registrar(String json) {
        Sucursal s = gson.fromJson(json, Sucursal.class);
        return ImpSucursal.registrar(s);
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editar(String json) {
        Sucursal s = gson.fromJson(json, Sucursal.class);
        return ImpSucursal.editar(s);
    }

    @PUT
    @Path("baja/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje darBaja(@PathParam("id") int id) {
        return ImpSucursal.darBaja(id);
    }
    
    @DELETE
    @Path("eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminarSucursal(@PathParam("id") int id) {
        return ImpSucursal.eliminarSucursal(id);
    }
}
