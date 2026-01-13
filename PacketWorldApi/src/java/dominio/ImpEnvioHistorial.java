package dominio;

import java.util.List;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Envio;
import pojo.EnvioHistorial;
import pojo.Mensaje;

public class ImpEnvioHistorial {

    public static List<EnvioHistorial> obtenerHistorial(int idEnvio) {
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                return conn.selectList("EnvioHistorialMapper.obtenerPorEnvio", idEnvio);
            } finally {
                conn.close();
            }
        }
        return null;
    }

    public static Mensaje registrarEstatus(EnvioHistorial h) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        if (conn != null) {
            try {
                conn.insert("EnvioHistorialMapper.registrar", h);
                Envio envio = new Envio();
                envio.setIdEstadosEnvio(h.getIdEstadosEnvio());
                envio.setIdEnvio(h.getIdEnvio());
                conn.update("EnvioMapper.actualizarEstadoEnvio", envio);
                conn.commit();
                msj.setError(false);
                msj.setMensaje("Estatus registrado correctamente.");
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                conn.close();
            }
        }
        return msj;
    }
}
