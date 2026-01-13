package dominio;

import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.ConductorUnidad;
import pojo.Mensaje;

public class ImpConductorUnidad {

    public static Mensaje asignarUnidad(int idConductor, int idUnidad, String numeroPersonal) {
        Mensaje msj = new Mensaje();
        SqlSession con = MybatisUtil.obtenerConexion();

        if (con != null) {
            try {
                ConductorUnidad existente = con.selectOne(
                        "ConductorUnidadMapper.obtenerAsignacionPorConductor",
                        idConductor);

                if (existente != null) {
                    msj.setError(true);
                    msj.setMensaje("El conductor ya tiene una unidad asignada.");
                    return msj;
                }

                ConductorUnidad unidadOcupada = con.selectOne(
                        "ConductorUnidadMapper.obtenerAsignacionPorUnidad",
                        idUnidad);

                if (unidadOcupada != null) {
                    msj.setError(true);
                    msj.setMensaje("La unidad ya está asignada a otro conductor.");
                    return msj;
                }

                ConductorUnidad nueva = new ConductorUnidad();
                nueva.setIdConductor(idConductor);
                nueva.setIdUnidad(idUnidad);
                nueva.setNumeroPersonal(numeroPersonal);

                int insert = con.insert("ConductorUnidadMapper.registrarAsignacion", nueva);

                con.update("ConductorUnidadMapper.actualizarUnidadConductor", nueva);
                con.update("ConductorUnidadMapper.actualizarConductor", nueva);
                con.commit();

                msj.setError(insert <= 0);
                msj.setMensaje(insert > 0 ? "Unidad asignada correctamente." : "No se pudo asignar la unidad.");
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

    public static Mensaje eliminarAsignacion(int idConductor) {
        Mensaje msj = new Mensaje();
        SqlSession con = MybatisUtil.obtenerConexion();
        if (con != null) {
            try {
                con.update("ConductorUnidadMapper.limpiarNumeroPersonalUnidad", idConductor);
                int eliminado = con.delete("ConductorUnidadMapper.eliminarAsignacion", idConductor);
                con.commit();
                msj.setError(eliminado <= 0);
                msj.setMensaje(eliminado > 0 ? "Unidad liberada." : "No existe asignación para eliminar.");
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
}
