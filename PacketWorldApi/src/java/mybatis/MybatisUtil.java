package mybatis;

import java.io.IOException;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


public class MybatisUtil {
    
    private static final String RESOURCE = "mybatis/mybatis-config.xml";
    private static final String ENVIROMENT = "desarrollo";
    
    public static SqlSession obtenerConexion(){
        SqlSession conexion = null;
        try {
            Reader reader = Resources.getResourceAsReader(RESOURCE);
            SqlSessionFactory abrirSesion = new SqlSessionFactoryBuilder().build(reader);
            conexion = abrirSesion.openSession();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return conexion;
    }
    
}