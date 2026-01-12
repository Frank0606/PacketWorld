package packetworldescritorio.modelo.dao;

import packetworldescritorio.modelo.ConexionWS;
import packetworldescritorio.pojo.RespuestaHTTP;
import packetworldescritorio.pojo.Sucursal;
import packetworldescritorio.pojo.Mensaje;
import packetworldescritorio.utilidades.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.HttpURLConnection;
import java.lang.reflect.Type;
import java.util.List;

public class SucursalDAO {
    
    public static List<Sucursal> obtenerSucursales() {
        List<Sucursal> sucursales = null;
        String url = Constantes.URL_WS + "sucursal/obtener-todos";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            try {
                Type tipoListaSucursal = new TypeToken<List<Sucursal>>(){}.getType();
                sucursales = gson.fromJson(respuesta.getContenido(), tipoListaSucursal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sucursales;
    }
    
    public static Sucursal obtenerPorId(int idSucursal) {
        Sucursal sucursal = null;
        String url = Constantes.URL_WS + "sucursal/obtener/" + idSucursal;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            try {
                sucursal = gson.fromJson(respuesta.getContenido(), Sucursal.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sucursal;
    }
    
    public static Sucursal obtenerPorCodigo(String codigo) {
        Sucursal sucursal = null;
        String url = Constantes.URL_WS + "sucursal/codigo/" + codigo;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            try {
                sucursal = gson.fromJson(respuesta.getContenido(), Sucursal.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sucursal;
    }
    
    public static Mensaje registrarSucursal(Sucursal sucursal) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "sucursal/registrar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(sucursal);
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
    
    public static Mensaje editarSucursal(Sucursal sucursal) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "sucursal/editar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(sucursal);
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
    
    public static Mensaje darBajaSucursal(int idSucursal) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "sucursal/baja/" + idSucursal;
        Gson gson = new Gson();
        try {
            RespuestaHTTP respuesta = ConexionWS.peticionPUT(url);
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
    
    public static Mensaje eliminarSucursal(int idSucursal) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "sucursal/eliminar/" + idSucursal;
        Gson gson = new Gson();
        try {
            RespuestaHTTP respuesta = ConexionWS.peticionDELETE(url);
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