package ws;

import com.google.gson.Gson;
import dominio.ImpColaborador;
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
import pojo.Colaborador;
import pojo.FotoColaboradorDTO;
import pojo.Mensaje;

@Path("colaborador")
public class WSColaborador {

    private final Gson gson = new Gson();

    @GET
    @Path("obtener-todos")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public List<Colaborador> obtenerTodos() {
        return ImpColaborador.obtenerColaboradores();
    }

    @GET
    @Path("buscar/numero-personal/{numeroPersonal}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Colaborador obtenerPorNumeroPersonal(@PathParam("numeroPersonal") String numeroPersonal) {

        if (numeroPersonal == null || numeroPersonal.trim().isEmpty()) {
            throw new BadRequestException("Número de personal inválido.");
        }

        Colaborador col = ImpColaborador.obtenerColaboradorPorNumeroPersonal(numeroPersonal);

        if (col == null) {
            throw new BadRequestException("Colaborador no encontrado.");
        }

        return col;
    }

    @GET
    @Path("buscar/id/{idColaborador}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Colaborador obtenerPorId(@PathParam("idColaborador") int idColaborador) {

        if (idColaborador == 0 || idColaborador == -1) {
            throw new BadRequestException("Número de personal inválido.");
        }

        Colaborador col = ImpColaborador.obtenerColaboradorPorId(idColaborador);

        if (col == null) {
            throw new BadRequestException("Colaborador no encontrado.");
        }

        return col;
    }

    @GET
    @Path("buscar/nombre/{nombre}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public List<Colaborador> buscarPorNombre(@PathParam("nombre") String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BadRequestException("Nombre inválido.");
        }

        return ImpColaborador.buscarPorNombre(nombre);
    }

    @GET
    @Path("buscar/rol/{idRol}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public List<Colaborador> buscarPorRol(@PathParam("idRol") int idRol) {

        if (idRol <= 0) {
            throw new BadRequestException("ID de rol inválido.");
        }

        return ImpColaborador.buscarPorRol(idRol);
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Mensaje registrar(String json) {
        try {
            Colaborador col = gson.fromJson(json, Colaborador.class);
            return ImpColaborador.registrarColaborador(col);
        } catch (Exception e) {
            throw new BadRequestException("Datos inválidos para registrar colaborador.");
        }
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Mensaje editar(String json) {
        try {
            Colaborador col = gson.fromJson(json, Colaborador.class);
            return ImpColaborador.editarColaborador(col);
        } catch (Exception e) {
            throw new BadRequestException("Datos inválidos para editar colaborador.");
        }
    }

    @DELETE
    @Path("eliminar/{numeroPersonal}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Mensaje eliminar(@PathParam("numeroPersonal") String numeroPersonal) {

        if (numeroPersonal == null || numeroPersonal.trim().isEmpty()) {
            throw new BadRequestException("Número de personal inválido.");
        }

        return ImpColaborador.eliminarColaborador(numeroPersonal);
    }

    @GET
    @Path("obtener-foto/{idColaborador}")
    @Produces(MediaType.APPLICATION_JSON)
    public FotoColaboradorDTO obtenerFoto(@PathParam("idColaborador") int idColaborador) {

        if (idColaborador <= 0) {
            throw new BadRequestException("ID inválido.");
        }

        FotoColaboradorDTO dto
                = ImpColaborador.obtenerFotoBase64PorId(idColaborador);

        return dto;
    }

    @PUT
    @Path("actualizar-foto/{idColaborador}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Mensaje actualizarFoto(
            @PathParam("idColaborador") int idColaborador,
            byte[] fotografia
    ) {

        if (idColaborador <= 0) {
            throw new BadRequestException("ID inválido.");
        }

        if (fotografia == null || fotografia.length == 0) {
            throw new BadRequestException("Fotografía inválida.");
        }

        return ImpColaborador.actualizarFoto(idColaborador, fotografia);
    }

}
