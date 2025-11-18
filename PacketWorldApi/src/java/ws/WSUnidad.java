package ws;

import com.google.gson.Gson;
import dominio.ImpUnidad;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Mensaje;
import pojo.Unidad;

@Path("unidad")
public class WSUnidad {

    @Path("todos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Unidad> obtenerUnidades() {
        return ImpUnidad.obtenerUnidades();
    }

    @Path("obtenerUnidadVin/{vin}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Unidad obtenerUnidadVin(@PathParam("vin") String vin) {
        if (vin != null && !vin.isEmpty()) {
            Unidad unidades = ImpUnidad.obtenerUnidadVin(vin);
            if (unidades != null) {
                return unidades;
            }
            throw new BadRequestException("No se encontro el colaborador:");
        }
        throw new BadRequestException("Se encontro el colaborador seleccionado:");
    }

    @Path("agregar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Mensaje agregarUnidad(String jsonColaborador) {
        try {
            Gson gson = new Gson();
            Unidad unidades = gson.fromJson(jsonColaborador, Unidad.class);

            return ImpUnidad.agregarUnidad(unidades);
        } catch (Exception e) {
            throw new BadRequestException();
        }
    }

    @Path("actualizar")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Mensaje actualizarColaborador(String jsonColaborador) {
        try {
            Gson gson = new Gson();
            Unidad unidades = gson.fromJson(jsonColaborador, Unidad.class);
            return ImpUnidad.actualizarUnidad(unidades);
        } catch (Exception e) {
            throw new BadRequestException();
        }
    }

    @Path("eliminar/{vin}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminarColaborador(@PathParam("vin") String vin) {
        if (!vin.isEmpty()) {
            return ImpUnidad.eliminarUnidad(vin);
        } else {
            throw new BadRequestException("ID Inválido.");
        }
    }
}
