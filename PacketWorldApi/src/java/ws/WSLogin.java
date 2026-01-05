package ws;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import dominio.ImpLogin;
import pojo.LoginColaborador;

@Path("login")
public class WSLogin {

    @Context
    private UriInfo context;

    public WSLogin() {
    }

    @Path("colaborador")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public LoginColaborador iniciarSesionColaborador(
            @FormParam("numeroPersonal") String numeroPersonal,
            @FormParam("contrasena") String contrasena) {

        if (numeroPersonal != null && !numeroPersonal.isEmpty()
                && contrasena != null && !contrasena.isEmpty()) {

            return ImpLogin.validarSesionColaborador(numeroPersonal, contrasena);
        }

        throw new BadRequestException("Parámetros inválidos.");
    }
}
