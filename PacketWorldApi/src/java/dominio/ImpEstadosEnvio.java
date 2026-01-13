package dominio;

import java.util.List;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.EstadosEnvio;
import pojo.Mensaje;

public class ImpEstadosEnvio {

    public static List<EstadosEnvio> obtenerTodos() {
        SqlSession conn = MybatisUtil.obtenerConexion();
        try {
            return conn.selectList("EstadosEnvioMapper.obtenerTodos");
        } finally {
            conn.close();
        }
    }

    public static EstadosEnvio obtenerPorId(int id) {
        SqlSession conn = MybatisUtil.obtenerConexion();
        try {
            return conn.selectOne("EstadosEnvioMapper.obtenerPorId", id);
        } finally {
            conn.close();
        }
    }

    public static Mensaje registrar(EstadosEnvio estado) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        try {
            conn.insert("EstadosEnvioMapper.registrar", estado);
            conn.commit();
            msj.setError(false);
            msj.setMensaje("Estado de envío registrado correctamente.");
        } catch (Exception e) {
            msj.setError(true);
            msj.setMensaje(e.getMessage());
        } finally {
            conn.close();
        }
        return msj;
    }

    public static Mensaje editar(EstadosEnvio estado) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        try {
            conn.update("EstadosEnvioMapper.editar", estado);
            conn.commit();
            msj.setError(false);
            msj.setMensaje("Estado de envío actualizado correctamente.");
        } catch (Exception e) {
            msj.setError(true);
            msj.setMensaje(e.getMessage());
        } finally {
            conn.close();
        }
        return msj;
    }

    public static Mensaje eliminar(int id) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        try {
            conn.delete("EstadosEnvioMapper.eliminar", id);
            conn.commit();
            msj.setError(false);
            msj.setMensaje("Estado de envío eliminado correctamente.");
        } catch (Exception e) {
            msj.setError(true);
            msj.setMensaje("No se puede eliminar el estado (puede estar en uso).");
        } finally {
            conn.close();
        }
        return msj;
    }
}
