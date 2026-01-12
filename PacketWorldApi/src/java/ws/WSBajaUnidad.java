package ws;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import dominio.ImpBajaUnidad;
import pojo.BajaUnidad;
import pojo.Mensaje;

@Path("baja-unidad")
public class WSBajaUnidad {

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje registrarBajaUnidad(BajaUnidad bajaUnidad) {
        return ImpBajaUnidad.registrarBajaUnidad(bajaUnidad);
    }

}
