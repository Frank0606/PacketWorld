package dominio;

import java.util.List;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Mensaje;
import pojo.Unidad;

public class ImpUnidad {

    public static List<Unidad> obtenerTodos() {
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                List<Unidad> lista = conn.selectList("UnidadMapper.obtenerTodos");
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

    public static Unidad obtenerPorId(int id) {
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                Unidad unidad = conn.selectOne("UnidadMapper.obtenerPorId", id);
                conn.commit();
                return unidad;
            } catch (Exception e) {
                conn.rollback();
            } finally {
                conn.close();
            }
        }
        return null;
    }

    public static Mensaje registrar(Unidad unidad) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();
        
        if (conn != null) {
            try {
                int result = conn.insert("UnidadMapper.registrar", unidad);
                conn.commit();

                if (result > 0) {
                    msj.setError(false);
                    msj.setMensaje("Unidad registrada con éxito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo registrar la unidad.");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            }
        }
        return msj;
    }

    public static Mensaje editar(Unidad unidad) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        if (conn != null) {
            try {
                int result = conn.update("UnidadMapper.editar", unidad);
                conn.commit();

                if (result > 0) {
                    msj.setError(false);
                    msj.setMensaje("Unidad actualizada con éxito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo actualizar la unidad.");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            }
        }
        return msj;
    }

    public static Mensaje eliminar(String vin) {
        Mensaje msj = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        if (conn != null) {
            try {
                int result = conn.delete("UnidadMapper.eliminar", vin);
                conn.commit();

                if (result > 0) {
                    msj.setError(false);
                    msj.setMensaje("Unidad eliminada con éxito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo eliminar la unidad.");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            }
        }
        return msj;
    }
}
