package ws;

import com.google.gson.Gson;
import dominio.ImpEnvioHistorial;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.EnvioHistorial;
import pojo.Mensaje;

@Path("envio-historial")
public class WSEnvioHistorial {

    @GET
    @Path("obtener/{idEnvio}")
    @Produces(MediaType.APPLICATION_JSON)
    public java.util.List<EnvioHistorial> obtener(@PathParam("idEnvio") int idEnvio) {
        return ImpEnvioHistorial.obtenerHistorial(idEnvio);
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje registrar(String json) {
        Gson gson = new Gson();
        EnvioHistorial h = gson.fromJson(json, EnvioHistorial.class);
        return ImpEnvioHistorial.registrarEstatus(h);
    }
}
