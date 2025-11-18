package dominio;

import java.util.List;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Mensaje;
import pojo.Unidad;

public class ImpUnidad {

    public static List<Unidad> obtenerUnidades() {
        SqlSession conexionBD = MybatisUtil.obtenerConexion();
        if (conexionBD != null) {
            try {
                List<Unidad> unidades = conexionBD.selectList("unidad.unidades");
                conexionBD.commit();
                return unidades;
            } catch (Exception e) {
                e.printStackTrace();
                conexionBD.rollback();
            } finally {
                conexionBD.close(); // Cierre de conexión
            }
        }
        return null;
    }

    public static Unidad obtenerUnidadVin(String vin) {
        SqlSession conexionBD = MybatisUtil.obtenerConexion();
        if (conexionBD != null) {
            try {
                Unidad unidades = conexionBD.selectOne("unidad.unidadVin", vin);
                conexionBD.commit();
                return unidades;
            } catch (Exception e) {
                e.printStackTrace();
                conexionBD.rollback();
            } finally {
                conexionBD.close(); // Cierre de conexión
            }
        }
        return null;
    }

    public static Mensaje agregarUnidad(Unidad unidad) {
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MybatisUtil.obtenerConexion();
        if (conexionBD != null) {
            try {
                int resultado = conexionBD.insert("unidad.registrarUnidad", unidad);
                conexionBD.commit();
                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Unidad registrada con exito");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo realizar el registro, intentelo mas tarde");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                conexionBD.close(); // Cierre de conexión
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Por el momento el servicio no esta disponible.");
        }
        return msj;
    }

    public static Mensaje actualizarUnidad(Unidad unidad) {
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MybatisUtil.obtenerConexion();
        if (conexionBD != null) {
            try {
                int resultado = conexionBD.update("unidad.actualizarUnidad", unidad);
                conexionBD.commit();
                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Unidad actualizada con exito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo actualizar la unidad, inténtelo más tarde.");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                conexionBD.close(); // Cierre de conexión
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Por el momento el servicio no esta disponible.");
        }
        return msj;
    }

    public static Mensaje eliminarUnidad(String vin) {
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MybatisUtil.obtenerConexion();
        if (conexionBD != null) {
            try {
                int resultado = conexionBD.delete("unidad.eliminarUnidad", vin);
                conexionBD.commit();
                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Unidad se elimino con exito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("Error al eliminar la unidad, inténtelo más tarde.");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                conexionBD.close(); // Cierre de conexión
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Por el momento el servicio no esta disponible.");
        }
        return msj;
    }
}