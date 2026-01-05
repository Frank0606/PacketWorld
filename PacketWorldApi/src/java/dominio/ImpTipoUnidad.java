package dominio;

import java.util.List;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Mensaje;
import pojo.TipoUnidad;

public class ImpTipoUnidad {

    public static List<TipoUnidad> obtenerTodos() {
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                List<TipoUnidad> lista = conn.selectList("TipoUnidadMapper.obtenerTodos");
                conn.commit();
                return lista;
            } catch (Exception e) {
                conn.rollback();
            } finally {
                conn.close();
            }
        }
        return null;
    }

    public static TipoUnidad obtenerPorId(int id) {
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                TipoUnidad tipo = conn.selectOne("TipoUnidadMapper.obtenerPorId", id);
                conn.commit();
                return tipo;
            } catch (Exception e) {
                conn.rollback();
            } finally {
                conn.close();
            }
        }
        return null;
    }

    public static Mensaje registrar(TipoUnidad tipo) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        if (conn != null) {
            try {
                int result = conn.insert("TipoUnidadMapper.registrar", tipo);
                conn.commit();
                if (result > 0) {
                    msj.setError(false);
                    msj.setMensaje("Tipo de unidad registrado con éxito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo registrar el tipo de unidad.");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            }
        }
        return msj;
    }

    public static Mensaje editar(TipoUnidad tipo) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        if (conn != null) {
            try {
                int result = conn.update("TipoUnidadMapper.editar", tipo);
                conn.commit();
                if (result > 0) {
                    msj.setError(false);
                    msj.setMensaje("Tipo de unidad actualizado con éxito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo actualizar.");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            }
        }
        return msj;
    }

    public static Mensaje eliminar(int id) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        if (conn != null) {
            try {
                int result = conn.delete("TipoUnidadMapper.eliminar", id);
                conn.commit();
                if (result > 0) {
                    msj.setError(false);
                    msj.setMensaje("Tipo de unidad eliminado.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo eliminar.");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            }
        }
        return msj;
    }
}
