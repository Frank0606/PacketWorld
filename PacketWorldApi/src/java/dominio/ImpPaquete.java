package dominio;

import java.util.List;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Mensaje;
import pojo.Paquete;

public class ImpPaquete {

    public static List<Paquete> obtenerPaquetes() {
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                List<Paquete> lista = conn.selectList("PaqueteMapper.obtenerTodos");
                return lista;
            } finally {
                conn.close();
            }
        }
        return null;
    }

    public static List<Paquete> obtenerPaquetesPorEnvio(int idEnvio) {
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                List<Paquete> lista = conn.selectList("PaqueteMapper.obtenerPorEnvio", idEnvio);
                return lista;
            } finally {
                conn.close();
            }
        }
        return null;
    }

    public static Paquete obtenerPaquetePorId(int idPaquete) {
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                return conn.selectOne("PaqueteMapper.obtenerPorId", idPaquete);
            } finally {
                conn.close();
            }
        }
        return null;
    }

    public static Mensaje registrarPaquete(Paquete paquete) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                int resultado = conn.insert("PaqueteMapper.registrar", paquete);
                conn.commit();
                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Paquete registrado correctamente.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo registrar el paquete.");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                conn.close();
            }
        }
        return msj;
    }

    public static Mensaje editarPaquete(Paquete paquete) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                int resultado = conn.update("PaqueteMapper.editar", paquete);
                conn.commit();
                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Paquete actualizado correctamente.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo actualizar el paquete.");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                conn.close();
            }
        }
        return msj;
    }

    public static Mensaje eliminarPaquete(int idPaquete) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                int resultado = conn.delete("PaqueteMapper.eliminar", idPaquete);
                conn.commit();
                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Paquete eliminado correctamente.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo eliminar el paquete.");
                }
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
