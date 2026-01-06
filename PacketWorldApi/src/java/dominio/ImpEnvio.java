package dominio;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Envio;
import pojo.Mensaje;
import pojo.Paquete;
import pojo.RespuestaApiKm;

public class ImpEnvio {

    public static List<Envio> obtenerEnvios() {
        SqlSession conexionBD = MybatisUtil.obtenerConexion();
        if (conexionBD != null) {
            try {
                List<Envio> lista
                        = conexionBD.selectList("EnvioMapper.obtenerTodos");

                return lista;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                conexionBD.close();
            }
        }
        return null;
    }

    public static String generarGuia(List<Envio> envios) {
        int max = 0;
        for (Envio e : envios) {
            String guia = e.getNumeroGuia();
            if (guia != null && guia.startsWith("GUIA")) {
                try {
                    int numero = Integer.parseInt(guia.substring(4));
                    if (numero > max) {
                        max = numero;
                    }
                } catch (NumberFormatException ex) {
                }
            }
        }

        int siguiente = max + 1;
        return String.format("GUIA%03d", siguiente);
    }

    public static float obtenerDistancia(int idSucursal, String cpDestino) {
        float distancia = -1;
        Gson gson = new Gson();

        String cpSucursal = ImpSucursal.obtenerPorId(idSucursal).getCodigoPostal();

        try {
            String urlStr = "http://sublimas.com.mx:8080/calculadora/api/envios/distancia/" + cpSucursal + "," + cpDestino;
            URL url = new URL(urlStr);

            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Accept", "application/json");

            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                RespuestaApiKm respuesta = gson.fromJson(sb.toString(), RespuestaApiKm.class);

                if (!respuesta.isError()) {
                    distancia = respuesta.getDistanciaKM();
                } else {
                    //Mostrar una alerta
                }
            }
            conexion.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return distancia;
    }

    public static float calcularCostoKm(float distanciaKm) {
        if (distanciaKm >= 1 && distanciaKm <= 200) {
            return distanciaKm * 4;
        } else if (distanciaKm >= 201 && distanciaKm <= 500) {
            return distanciaKm * 3;
        } else if (distanciaKm >= 501 && distanciaKm <= 1000) {
            return distanciaKm * 2;
        } else if (distanciaKm >= 1001 && distanciaKm <= 2000) {
            return distanciaKm;
        } else {
            return (float) (distanciaKm * 0.5);
        }
    }

    public static float obtenerCostoPaquetes(int idEnvio) {
        List<Paquete> paquetes = new ArrayList<>();
        paquetes = ImpPaquete.obtenerPaquetesPorEnvio(idEnvio);
        if (paquetes.size() == 1) {
            return 0;
        } else if (paquetes.size() == 2) {
            return 50;
        } else if (paquetes.size() == 3) {
            return 80;
        } else if (paquetes.size() == 4) {
            return 110;
        } else {
            return 150;
        }
    }

    public static Mensaje registrarEnvio(Envio envio) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        List<Envio> envios = new ArrayList<>();
        envios = obtenerEnvios();

        envio.setNumeroGuia(generarGuia(envios));
        envio.setEstatus("En tránsito");
        envio.setDistanciaKm(obtenerDistancia(envio.getIdSucursalOrigen(), envio.getCpDestino()));
        envio.setCostoKm(calcularCostoKm(envio.getDistanciaKm()));
        envio.setCostoPaquetes(obtenerCostoPaquetes(envio.getIdEnvio()));
        envio.setCostoTotal(envio.getCostoKm() + envio.getCostoPaquetes());
        envio.setFechaCreacion(LocalDateTime.now().toString());

        if (conn != null) {
            try {
                conn.insert("EnvioMapper.registrar", envio);
                conn.commit();
                msj.setError(false);
                msj.setMensaje("Envío registrado exitosamente.");
            } catch (Exception ex) {
                msj.setError(true);
                msj.setMensaje(ex.getMessage());
            } finally {
                conn.close();
            }
        }
        return msj;
    }

    public static Envio obtenerPorGuia(String guia) {
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                return conn.selectOne("EnvioMapper.obtenerPorGuia", guia);
            } finally {
                conn.close();
            }
        }
        return null;
    }

    public static Mensaje editarEnvio(Envio envio) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        if (conn != null) {
            try {
                conn.update("EnvioMapper.editar", envio);
                conn.commit();
                msj.setError(false);
                msj.setMensaje("Envío actualizado correctamente.");
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                conn.close();
            }
        }
        return msj;
    }

    public static Envio obtenerEnvioGuiaDireccion(String guia) {
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                return conn.selectOne("EnvioMapper.obtenerEnvioGuiaDireccion", guia);
            } finally {
                conn.close();
            }
        }
        return null;
    }

    public static Mensaje eliminar(String numeroGuia) {
        Mensaje msj = new Mensaje();
        return msj;
    }
}
