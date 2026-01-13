package packetworldescritorio.modelo.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import packetworldescritorio.modelo.ConexionWS;
import packetworldescritorio.pojo.EstadosEnvio;
import packetworldescritorio.pojo.RespuestaHTTP;
import packetworldescritorio.utilidades.Constantes;

public class EstadosEnvioDAO {

    /**
     * Obtener todos los estados de envío
     */
    public static List<EstadosEnvio> obtenerTodos() {
        List<EstadosEnvio> estados = new ArrayList<>();
        String url = Constantes.URL_WS + "estados-envio/obtener-todos";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);

        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                Type tipoLista = new TypeToken<List<EstadosEnvio>>() {}.getType();
                estados = gson.fromJson(respuesta.getContenido(), tipoLista);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return estados;
    }

    /**
     * Obtener estado de envío por ID
     */
    public static EstadosEnvio obtenerPorId(Integer idEstadosEnvio) {
        EstadosEnvio estado = null;
        String url = Constantes.URL_WS + "estados-envio/obtener/" + idEstadosEnvio;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);

        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                estado = gson.fromJson(respuesta.getContenido(), EstadosEnvio.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return estado;
    }
}
