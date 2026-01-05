package packetworldescritorio.modelo.dao;

import packetworldescritorio.modelo.ConexionWS;
import packetworldescritorio.pojo.RespuestaHTTP;
import packetworldescritorio.pojo.Rol;
import packetworldescritorio.utilidades.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

public class RolDAO {
    public static List<Rol> obtenerRol(){
        List<Rol> roles = null;
        String url = Constantes.URL_WS + "roles/obtener-todos";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            try{
                Type tipoListaRol = new TypeToken<List<Rol>>() {}.getType();
                roles = gson.fromJson(respuesta.getContenido(), tipoListaRol);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return roles;
    }
}
