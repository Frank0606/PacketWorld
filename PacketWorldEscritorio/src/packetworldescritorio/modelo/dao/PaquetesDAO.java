package packetworldescritorio.modelo.dao;

import packetworldescritorio.modelo.ConexionWS;
import packetworldescritorio.pojo.Paquete;
import packetworldescritorio.pojo.RespuestaHTTP;
import packetworldescritorio.utilidades.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

public class PaquetesDAO {

    public static List<Paquete> obtenerPaquetes() {
        List<Paquete> paquetes = null;
        String url = Constantes.URL_WS + "paquete/todos";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);

        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                Type tipoListaPaquete = new TypeToken<List<Paquete>>(){}.getType();
                paquetes = gson.fromJson(respuesta.getContenido(), tipoListaPaquete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return paquetes;
    }

    public static Mensaje agregarPaquete(Paquete paquete) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "paquete/agregar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(paquete);
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

    public static Mensaje actualizarPaquete(Paquete paquete) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "paquete/actualizar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(paquete);
            RespuestaHTTP respuesta = ConexionWS.peticionPUTJSON(url, parametros);
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

    public static Mensaje eliminarPaquete(int idPaquete) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "paquete/eliminar/" + idPaquete;
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(idPaquete);
            RespuestaHTTP respuesta = ConexionWS.peticionDELETEJSON(url, parametros);
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
