package ws;

import dominio.ImpEnvioConductor;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.Mensaje;

@Path("envio-conductor")
public class WSEnvioConductor {

    @POST
    @Path("asignar/{idEnvio}/{idConductor}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje asignarEnvio(
            @PathParam("idEnvio") int idEnvio,
            @PathParam("idConductor") int idConductor) {
        return ImpEnvioConductor.asignarEnvio(idEnvio, idConductor);
    }

    @DELETE
    @Path("remover/{idEnvio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje removerAsignacion(@PathParam("idEnvio") int idEnvio) {
        return ImpEnvioConductor.removerAsignacion(idEnvio);
    }
}
