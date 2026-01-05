package packetworldescritorio.modelo.dao;

import packetworldescritorio.modelo.ConexionWS;
import packetworldescritorio.pojo.IniciarSesion;
import packetworldescritorio.pojo.RespuestaHTTP;
import packetworldescritorio.utilidades.Constantes;
import com.google.gson.Gson;
import java.net.HttpURLConnection;

public class IniciarSesionDAO {
    
    public static IniciarSesion iniciarSesion(String noPersonal, String contrasenia) {

        IniciarSesion respuestaLogin = new IniciarSesion();
        Gson gson = new Gson();
        String urlServicio = Constantes.URL_WS + "login/colaborador";
        
        String parametros = String.format("numeroPersonal=%s&contrasena=%s", noPersonal, contrasenia);

        RespuestaHTTP respuestaWS = ConexionWS.peticionPOST(urlServicio, parametros);
        
        if(respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            respuestaLogin = gson.fromJson(respuestaWS.getContenido(), IniciarSesion.class);
        } else {
            respuestaLogin.setError(true);
            respuestaLogin.setMensaje("Lo sentimos, hubo un error al iniciar sesión. Intenta de nuevo. :(");
        }

        return respuestaLogin;
    }
}
