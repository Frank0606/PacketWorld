package ws;

import com.google.gson.Gson;
import dominio.ImpPaquete;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.Mensaje;
import pojo.Paquete;

@Path("paquete")
public class WSPaquete {
    
    @GET
    @Path("obtener-todos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Paquete> obtenerPaquetes() {
        return ImpPaquete.obtenerPaquetes();
    }

    @GET
    @Path("obtener-por-envio/{idEnvio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Paquete> obtenerPaquetesEnvio(@PathParam("idEnvio") int idEnvio) {
        return ImpPaquete.obtenerPaquetesPorEnvio(idEnvio);
    }

    @GET
    @Path("obtener-por-ID/{idPaquete}")
    @Produces(MediaType.APPLICATION_JSON)
    public Paquete obtenerPaqueteId(@PathParam("idPaquete") int idPaquete) {
        return ImpPaquete.obtenerPaquetePorId(idPaquete);
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje registrar(String json) {
        Gson gson = new Gson();
        Paquete paquete = gson.fromJson(json, Paquete.class);
        return ImpPaquete.registrarPaquete(paquete);
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editar(String json) {
        Gson gson = new Gson();
        Paquete paquete = gson.fromJson(json, Paquete.class);
        return ImpPaquete.editarPaquete(paquete);
    }

    @DELETE
    @Path("eliminar/{idPaquete}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminar(@PathParam("idPaquete") int idPaquete) {
        return ImpPaquete.eliminarPaquete(idPaquete);
    }
}
