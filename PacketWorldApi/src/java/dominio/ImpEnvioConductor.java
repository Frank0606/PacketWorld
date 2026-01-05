package dominio;

import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.EnvioConductor;
import pojo.Mensaje;

public class ImpEnvioConductor {

    public static Mensaje asignarEnvio(int idEnvio, int idConductor) {
        Mensaje msj = new Mensaje();
        SqlSession con = MybatisUtil.obtenerConexion();

        if (con != null) {
            try {
                // validar si ya está asignado
                EnvioConductor yaAsignado = con.selectOne("EnvioConductorMapper.obtenerAsignacionPorEnvio", idEnvio);

                if (yaAsignado != null) {
                    msj.setError(true);
                    msj.setMensaje("El envío ya está asignado a un conductor.");
                    return msj;
                }

                EnvioConductor nuevo = new EnvioConductor();
                nuevo.setIdEnvio(idEnvio);
                nuevo.setIdConductor(idConductor);

                int insert = con.insert("EnvioConductorMapper.registrarAsignacion", nuevo);
                con.commit();

                msj.setError(insert <= 0);
                msj.setMensaje(insert > 0 ? "Envío asignado correctamente." : "No se pudo asignar el envío.");
            } catch (Exception e) {
                con.rollback();
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                con.close();
            }
        }

        return msj;
    }

    public static Mensaje removerAsignacion(int idEnvio) {
        Mensaje msj = new Mensaje();
        SqlSession con = MybatisUtil.obtenerConexion();

        if (con != null) {
            try {
                int deleted = con.delete("EnvioConductorMapper.eliminarAsignacion", idEnvio);
                con.commit();

                msj.setError(deleted <= 0);
                msj.setMensaje(deleted > 0 ? "Asignación eliminada." : "El envío no tiene asignación.");
            } catch (Exception e) {
                con.rollback();
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                con.close();
            }
        }

        return msj;
    }

    public static EnvioConductor obtenerConductorDeEnvio(int idEnvio) {
        SqlSession con = MybatisUtil.obtenerConexion();
        EnvioConductor asignacion = null;

        if (con != null) {
            try {
                asignacion = con.selectOne("EnvioConductorMapper.obtenerConductorDeEnvio", idEnvio);
                con.commit();
            } catch (Exception e) {
                con.rollback();
            } finally {
                con.close();
            }
        }

        return asignacion;
    }

    public static java.util.List<EnvioConductor> obtenerEnviosPorConductor(int idConductor) {
        SqlSession con = MybatisUtil.obtenerConexion();
        java.util.List<EnvioConductor> envios = null;

        if (con != null) {
            try {
                envios = con.selectList("EnvioConductorMapper.obtenerEnviosPorConductor", idConductor);
                con.commit();
            } catch (Exception e) {
                con.rollback();
            } finally {
                con.close();
            }
        }

        return envios;
    }

}
