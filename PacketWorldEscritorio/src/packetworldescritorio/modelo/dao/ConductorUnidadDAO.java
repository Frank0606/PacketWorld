package packetworldescritorio.modelo.dao;

import com.google.gson.Gson;
import java.net.HttpURLConnection;
import packetworldescritorio.modelo.ConexionWS;
import packetworldescritorio.pojo.Colaborador;
import packetworldescritorio.pojo.RespuestaHTTP;
import packetworldescritorio.utilidades.Constantes;

public class ConductorUnidadDAO {

    public static Mensaje asignarUnidad(Colaborador colaborador) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "conductor-unidad/asignar/"
                + colaborador.getIdColaborador() + "/" + colaborador.getIdUnidad() + "/" + colaborador.getNumeroPersonal();
        Gson gson = new Gson();
        try {
            RespuestaHTTP respuesta = ConexionWS.peticionPOSTJSON(url, "");
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

    public static Mensaje liberarUnidad(int idConductor) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "conductor-unidad/liberar/" + idConductor;
        Gson gson = new Gson();

        try {
            RespuestaHTTP respuesta = ConexionWS.peticionDELETE(url);
            if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
                msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
            } else {
                msj.setError(true);
                msj.setMensaje("Error al liberar unidad: " + respuesta.getContenido());
            }
        } catch (Exception e) {
            msj.setError(true);
            msj.setMensaje("Excepción: " + e.getMessage());
        }

        return msj;
    }

}
