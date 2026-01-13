package packetworldescritorio.modelo.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import packetworldescritorio.modelo.ConexionWS;
import packetworldescritorio.pojo.Estado;
import packetworldescritorio.pojo.RespuestaHTTP;
import packetworldescritorio.utilidades.Constantes;

public class EstadosDAO {
    
    public static List<Estado> obtenerTodos() {
        List<Estado> estados = new ArrayList();
        String url = Constantes.URL_WS + "estado/obtener-todos";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);

        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                Type tipoListaEstado = new TypeToken<List<Estado>>() {
                }.getType();
                estados = gson.fromJson(respuesta.getContenido(), tipoListaEstado);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return estados;
    }
    
    public static Estado obtenerEstado(Integer idEstado) {
        Estado estado = new Estado();
        String url = Constantes.URL_WS + "estado/obtener-id/" + idEstado;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);

        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                estado = gson.fromJson(respuesta.getContenido(), Estado.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return estado;
    }
}
