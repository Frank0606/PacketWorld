package ws;

import dominio.*;
import java.util.HashMap;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.Envio;

@Path("consulta-envio")
public class WSConsultaEnvio {

    @Path("guia/{numeroGuia}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String, Object> consultarPorGuia(@PathParam("numeroGuia") String guia) {

        HashMap<String, Object> respuesta = new HashMap<>();

        Envio envio = ImpEnvio.obtenerPorGuia(guia);
        if (envio == null) {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Envío no encontrado");
            return respuesta;
        }

        respuesta.put("envio", envio);
        respuesta.put("paquetes", ImpPaquete.obtenerPaquetesPorEnvio(envio.getIdEnvio()));
        respuesta.put("historial", ImpEnvioHistorial.obtenerHistorial(envio.getIdEnvio()));
        respuesta.put("conductor", ImpEnvioConductor.obtenerConductorDeEnvio(envio.getIdEnvio()));

        return respuesta;
    }
}

