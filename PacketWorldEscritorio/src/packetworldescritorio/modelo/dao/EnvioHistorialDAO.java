package packetworldescritorio.modelo.dao;

import com.google.gson.Gson;
import java.net.HttpURLConnection;
import packetworldescritorio.modelo.ConexionWS;
import packetworldescritorio.pojo.RespuestaHTTP;
import packetworldescritorio.utilidades.Constantes;

public class EnvioHistorialDAO {

    public static Mensaje obtenerEnvioHistorial(int idEnvio) {
        Mensaje msj = null;
        String url = Constantes.URL_WS + "envio-historial/obtener/" + idEnvio;
        Gson gson = new Gson();
        try {
            RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
            if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
                msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
            } else {
                msj.setError(true);
                msj.setMensaje(respuesta.getContenido());
            }
        } catch (Exception e) {
            msj.setError(true);
            msj.setMensaje(e.getMessage());
        }
        return msj;
    }
}
