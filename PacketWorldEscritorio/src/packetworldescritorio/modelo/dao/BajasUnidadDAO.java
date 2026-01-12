package packetworldescritorio.modelo.dao;

import packetworldescritorio.modelo.ConexionWS;
import packetworldescritorio.pojo.BajaUnidad;
import packetworldescritorio.pojo.RespuestaHTTP;
import packetworldescritorio.utilidades.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

public class BajasUnidadDAO {

    public static List<BajaUnidad> obtenerBajaUnidads() {
        List<BajaUnidad> bajaUnidad = null;
        String url = Constantes.URL_WS + "baja-unidad/todos";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);

        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                Type tipoListaBajaUnidad = new TypeToken<List<BajaUnidad>>() {
                }.getType();
                bajaUnidad = gson.fromJson(respuesta.getContenido(), tipoListaBajaUnidad);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bajaUnidad;
    }

    public static Mensaje agregarBajaUnidad(BajaUnidad bajaUnidad) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "baja-unidad/registrar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(bajaUnidad);
            RespuestaHTTP respuesta = ConexionWS.peticionPOSTJSON(url, parametros);
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
