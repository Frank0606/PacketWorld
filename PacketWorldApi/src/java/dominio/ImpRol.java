package dominio;

import java.util.List;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Rol;

/**
 *
 * @author Manzano
 */
public class ImpRol {

    public static List<Rol> obtenerRoles() {
        SqlSession conexionBD = MybatisUtil.obtenerConexion();
        if (conexionBD != null) {
            try {
                List<Rol> roles = conexionBD.selectList("RolMapper.obtenerRol");
                conexionBD.commit();
                return roles;
            } catch (Exception e) {
                conexionBD.rollback();
            } finally {
                conexionBD.close();
            }
        }
        return null;
    }
}
