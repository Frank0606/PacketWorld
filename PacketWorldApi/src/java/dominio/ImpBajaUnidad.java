package dominio;

import org.apache.ibatis.session.SqlSession;
import pojo.Mensaje;
import mybatis.MybatisUtil;
import pojo.BajaUnidad;

public class ImpBajaUnidad {

    public static Mensaje registrarBajaUnidad(BajaUnidad bajaUnidad) {
        Mensaje msj = new Mensaje();
        SqlSession con = MybatisUtil.obtenerConexion();

        if (con != null) {
            try {
                int insert = con.insert("BajaUnidadMapper.registrarBajaUnidad", bajaUnidad);
                con.update("BajaUnidadMapper.cambiarEstatusUnidad", bajaUnidad.getIdUnidad());
                con.commit();

                msj.setError(insert <= 0);
                msj.setMensaje(insert > 0 ? "Baja registrada correctamente." : "No se pudo registrar la baja.");
            } catch (Exception e) {
                con.rollback();
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
            } finally {
                con.close();
            }
        }
        return msj;
    }
}
