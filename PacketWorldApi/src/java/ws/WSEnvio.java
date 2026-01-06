package ws;

import com.google.gson.Gson;
import dominio.ImpEnvio;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.Envio;
import pojo.Mensaje;

@Path("envio")
public class WSEnvio {

    @GET
    @Path("obtener-todos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Envio> obtenerTodos() {
        return ImpEnvio.obtenerEnvios();
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje registrar(String json) {
        Gson gson = new Gson();
        Envio envio = gson.fromJson(json, Envio.class);
        return ImpEnvio.registrarEnvio(envio);
    }

    @GET
    @Path("obtener/{guia}")
    @Produces(MediaType.APPLICATION_JSON)
    public Envio obtenerPorGuia(@PathParam("guia") String guia) {
        return ImpEnvio.obtenerPorGuia(guia);
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editar(String json) {
        Gson gson = new Gson();
        Envio envio = gson.fromJson(json, Envio.class);
        return ImpEnvio.editarEnvio(envio);
    }

    @DELETE
    @Path("eliminar/{guia}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminar(@PathParam("guia") String guia) {
        return ImpEnvio.eliminar(guia);
    }
    
    @GET
    @Path("guia-direccion/{guia}")
    @Produces(MediaType.APPLICATION_JSON)
    public Envio obtenerEnvioGuiaDireccion(@PathParam("guia") String guia) {
        return ImpEnvio.obtenerEnvioGuiaDireccion(guia);
    }

}
