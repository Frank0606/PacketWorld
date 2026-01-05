package ws;

import dominio.ImpConductorUnidad;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.Mensaje;

@Path("conductor-unidad")
public class WSConductorUnidad {

    @POST
    @Path("asignar/{idConductor}/{idUnidad}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje asignarUnidad(
            @PathParam("idConductor") int idConductor,
            @PathParam("idUnidad") int idUnidad) {
        return ImpConductorUnidad.asignarUnidad(idConductor, idUnidad);
    }

    @DELETE
    @Path("liberar/{idConductor}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje liberarUnidad(@PathParam("idConductor") int idConductor) {
        return ImpConductorUnidad.eliminarAsignacion(idConductor);
    }
}
