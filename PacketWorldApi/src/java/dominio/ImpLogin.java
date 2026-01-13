package dominio;

import java.util.LinkedHashMap;
import java.util.Map;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Colaborador;
import pojo.LoginColaborador;

public class ImpLogin {

    public static LoginColaborador validarSesionColaborador(String numeroPersonal, String contrasena) {

        LoginColaborador respuesta = new LoginColaborador();
        SqlSession conexionBD = MybatisUtil.obtenerConexion();

        if (conexionBD != null) {
            try {
                Map<String, Object> parametros = new LinkedHashMap<>();
                parametros.put("numeroPersonal", numeroPersonal);
                parametros.put("contrasena", contrasena);

                Colaborador colaborador = conexionBD.selectOne("IniciarSesionMapper.loginColaborador", parametros);

                if (colaborador != null) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Credenciales correctas del colaborador: " + colaborador.getNombre());
                    respuesta.setColaborador(colaborador);
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("Número de personal y/o contraseña incorrectos");
                }

            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje("Error: " + e.getMessage());
            } finally {
                conexionBD.close();
            }

        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión a la base de datos.");
        }

        return respuesta;
    }

}
