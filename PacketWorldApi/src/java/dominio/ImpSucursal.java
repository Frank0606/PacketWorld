package dominio;

import java.util.List;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Mensaje;
import pojo.Sucursal;

public class ImpSucursal {

    public static List<Sucursal> obtenerSucursales() {
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                return conn.selectList("SucursalMapper.obtenerTodos");
            } finally {
                conn.close();
            }
        }
        return null;
    }

    public static Sucursal obtenerPorId(int idSucursal) {
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                return conn.selectOne("SucursalMapper.obtenerPorId", idSucursal);
            } finally {
                conn.close();
            }
        }
        return null;
    }

    public static Sucursal obtenerPorCodigo(String codigo) {
        SqlSession conn = MybatisUtil.obtenerConexion();
        if (conn != null) {
            try {
                return conn.selectOne("SucursalMapper.obtenerPorCodigo", codigo);
            } finally {
                conn.close();
            }
        }
        return null;
    }

    public static Mensaje registrar(Sucursal s) {
        Mensaje m = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        if (conn != null) {
            try {
                int resultado = conn.insert("SucursalMapper.registrar", s);
                conn.commit();
                m.setError(resultado == 0);
                m.setMensaje(resultado > 0 ? "Sucursal registrada" : "No se pudo registrar");
            } catch (Exception e) {
                m.setError(true);
                m.setMensaje(e.getMessage());
            } finally {
                conn.close();
            }
        }
        return m;
    }

    public static Mensaje editar(Sucursal s) {
        Mensaje m = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        if (conn != null) {
            try {
                int resultado = conn.update("SucursalMapper.editar", s);
                conn.commit();
                m.setError(resultado == 0);
                m.setMensaje(resultado > 0 ? "Sucursal actualizada" : "No se pudo actualizar");
            } catch (Exception e) {
                m.setError(true);
                m.setMensaje(e.getMessage());
            } finally {
                conn.close();
            }
        }
        return m;
    }

    public static Mensaje darBaja(int idSucursal) {
        Mensaje m = new Mensaje();
        SqlSession conn = MybatisUtil.obtenerConexion();

        if (conn != null) {
            try {
                int resultado = conn.update("SucursalMapper.darBaja", idSucursal);
                conn.commit();
                m.setError(resultado == 0);
                m.setMensaje(resultado > 0 ? "Sucursal dada de baja" : "No se pudo dar de baja");
            } catch (Exception e) {
                m.setError(true);
                m.setMensaje(e.getMessage());
            } finally {
                conn.close();
            }
        }
        return m;
    }
}
