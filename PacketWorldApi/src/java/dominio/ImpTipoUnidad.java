package dominio;

import java.util.List;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.TipoUnidad;

public class ImpTipoUnidad {

    public static List<TipoUnidad> obtenerUnidad() {

        SqlSession conexionBD = MybatisUtil.obtenerConexion();

        if (conexionBD != null) {
            try {
                List<TipoUnidad> unidades = conexionBD.selectList("obtenerTipoUnidad.tipoUnidad");

                conexionBD.commit();

                return unidades;

            } catch (Exception e) {
                e.printStackTrace();
                conexionBD.rollback();

            } finally {
                conexionBD.close();
            }

        }

        return null;

    }
}