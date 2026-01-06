package ws;

import com.google.gson.Gson;
import dominio.ImpCliente;
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
import pojo.Cliente;
import pojo.Mensaje;

@Path("cliente")
public class WSCliente {

    private final Gson gson = new Gson();

    @GET
    @Path("obtener-todos")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public List<Cliente> obtenerTodos() {
        return ImpCliente.obtenerClientes();
    }

    @GET
    @Path("obtener-id/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Cliente obtenerPorId(@PathParam("id") int idCliente) {
        Cliente c = ImpCliente.obtenerPorId(idCliente);
        if (c == null) throw new BadRequestException("Cliente no encontrado.");
        return c;
    }

    @GET
    @Path("correo/{correo}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Cliente buscarPorCorreo(@PathParam("correo") String correo) {
        Cliente c = ImpCliente.buscarPorCorreo(correo);
        if (c == null) throw new BadRequestException("Correo no encontrado.");
        return c;
    }

    @GET
    @Path("nombre/{nombre}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public List<Cliente> buscarPorNombre(@PathParam("nombre") String nombre) {
        return ImpCliente.buscarPorNombre(nombre);
    }

    @GET
    @Path("telefono/{telefono}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Cliente buscarPorTelefono(@PathParam("telefono") String telefono) {
        Cliente c = ImpCliente.buscarPorTelefono(telefono);
        if (c == null) throw new BadRequestException("Cliente no encontrado.");
        return c;
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Mensaje registrar(String json) {
        Cliente cliente = gson.fromJson(json, Cliente.class);
        return ImpCliente.registrar(cliente);
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Mensaje editar(String json) {
        Cliente cliente = gson.fromJson(json, Cliente.class);
        return ImpCliente.editar(cliente);
    }

    @DELETE
    @Path("eliminar/{correo}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Mensaje eliminar(@PathParam("correo") String correo) {
        return ImpCliente.eliminar(correo);
    }

}
