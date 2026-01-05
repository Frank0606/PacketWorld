package ws;

import dominio.ImpRol;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import pojo.Rol;

@Path("rol")
public class WSRol {
    @Context
    private UriInfo context;
    
    public WSRol (){
    }
    
    @Path("obtener-todos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Rol> obtencionRoles(){
        return ImpRol.obtenerRoles();
    }
}
