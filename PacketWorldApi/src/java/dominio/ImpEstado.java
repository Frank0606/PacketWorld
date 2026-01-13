package dominio;

import java.util.List;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Estado;
import pojo.Mensaje;

public class ImpEstado {

    public static List<Estado> obtenerEstados() {
        SqlSession conexion = MybatisUtil.obtenerConexion();
        if (conexion != null) {
            try {
                List<Estado> lista = conexion.selectList("EstadoMapper.obtenerTodos");
                conexion.commit();
                return lista;
            } catch (Exception e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.close();
            }
        }
        return null;
    }

    public static Estado obtenerPorId(int idEstado) {
        SqlSession conexion = MybatisUtil.obtenerConexion();
        if (conexion != null) {
            try {
                Estado estado = conexion.selectOne("EstadoMapper.obtenerPorId", idEstado);
                conexion.commit();
                return estado;
            } catch (Exception e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.close();
            }
        }
        return null;
    }

    public static List<Estado> buscarPorNombre(String nombre) {
        SqlSession conexion = MybatisUtil.obtenerConexion();
        if (conexion != null) {
            try {
                List<Estado> estados = conexion.selectList("EstadoMapper.buscarPorNombre", nombre);
                conexion.commit();
                return estados;
            } catch (Exception e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.close();
            }
        }
        return null;
    }

    public static Mensaje registrar(Estado estado) {
        Mensaje msj = new Mensaje();
        SqlSession conexion = MybatisUtil.obtenerConexion();

        if (conexion != null) {
            try {
                int resultado = conexion.insert("EstadoMapper.registrar", estado);
                conexion.commit();

                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Estado registrado con éxito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo registrar el estado.");
                }
            } catch (Exception e) {
                conexion.rollback();
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                conexion.close();
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Servicio no disponible.");
        }
        return msj;
    }

    public static Mensaje editar(Estado estado) {
        Mensaje msj = new Mensaje();
        SqlSession conexion = MybatisUtil.obtenerConexion();

        if (conexion != null) {
            try {
                int resultado = conexion.update("EstadoMapper.editar", estado);
                conexion.commit();

                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Estado actualizado con éxito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo actualizar el estado.");
                }
            } catch (Exception e) {
                conexion.rollback();
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                conexion.close();
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Servicio no disponible.");
        }
        return msj;
    }

    public static Mensaje eliminar(int idEstado) {
        Mensaje msj = new Mensaje();
        SqlSession conexion = MybatisUtil.obtenerConexion();

        if (conexion != null) {
            try {
                int resultado = conexion.delete("EstadoMapper.eliminar", idEstado);
                conexion.commit();

                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Estado eliminado con éxito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo eliminar el estado.");
                }
            } catch (Exception e) {
                conexion.rollback();
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                conexion.close();
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Servicio no disponible.");
        }
        return msj;
    }
}