package dominio;

import java.util.List;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Colaborador;
import pojo.Mensaje;

public class ImpColaborador {

    // =========================================
    // OBTENER TODOS LOS COLABORADORES
    // =========================================
    public static List<Colaborador> obtenerColaboradores() {
        SqlSession conexionBD = MybatisUtil.obtenerConexion();
        if (conexionBD != null) {
            try {
                List<Colaborador> lista
                        = conexionBD.selectList("ColaboradorMapper.obtenerTodosColaborador");

                return lista;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                conexionBD.close();
            }
        }
        return null;
    }

    // =========================================
    // OBTENER POR NUMERO DE PERSONAL
    // =========================================
    public static Colaborador obtenerColaboradorPorNumeroPersonal(String numeroPersonal) {
        SqlSession conexionBD = MybatisUtil.obtenerConexion();
        if (conexionBD != null) {
            try {
                Colaborador col = conexionBD.selectOne(
                        "ColaboradorMapper.obtenerPorNumeroPersonal",
                        numeroPersonal
                );
                return col;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                conexionBD.close();
            }
        }
        return null;
    }
    
    public static Colaborador obtenerColaboradorPorId(int idColaborador) {
        SqlSession conexionBD = MybatisUtil.obtenerConexion();
        if (conexionBD != null) {
            try {
                Colaborador col = conexionBD.selectOne(
                        "ColaboradorMapper.obtenerPorId",
                        idColaborador
                );
                return col;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                conexionBD.close();
            }
        }
        return null;
    }

    // =========================================
    // REGISTRAR COLABORADOR
    // =========================================
    public static Mensaje registrarColaborador(Colaborador colaborador) {
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MybatisUtil.obtenerConexion();

        if (conexionBD != null) {
            try {
                int resultado = conexionBD.insert("ColaboradorMapper.registrar", colaborador);
                conexionBD.commit();

                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Colaborador registrado con éxito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo registrar al colaborador, inténtelo más tarde.");
                }

            } catch (Exception e) {
                conexionBD.rollback();
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }

        } else {
            msj.setError(true);
            msj.setMensaje("No hay conexión a la base de datos.");
        }
        return msj;
    }

    // =========================================
    // EDITAR COLABORADOR
    // =========================================
    public static Mensaje editarColaborador(Colaborador colaborador) {
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MybatisUtil.obtenerConexion();

        if (conexionBD != null) {
            try {
                int resultado = conexionBD.update("ColaboradorMapper.editar", colaborador);
                conexionBD.commit();

                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Colaborador actualizado correctamente.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se encontró el colaborador a actualizar.");
                }

            } catch (Exception e) {
                conexionBD.rollback();
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }

        } else {
            msj.setError(true);
            msj.setMensaje("No hay conexión a la base de datos.");
        }
        return msj;
    }

    // =========================================
    // ELIMINAR COLABORADOR
    // =========================================
    public static Mensaje eliminarColaborador(String numeroPersonal) {
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MybatisUtil.obtenerConexion();

        if (conexionBD != null) {
            try {
                int resultado = conexionBD.delete("ColaboradorMapper.eliminar", numeroPersonal);
                conexionBD.commit();

                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Colaborador eliminado con éxito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo eliminar el colaborador.");
                }

            } catch (Exception e) {
                conexionBD.rollback();
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }

        } else {
            msj.setError(true);
            msj.setMensaje("No hay conexión a la base de datos.");
        }
        return msj;
    }

    // =========================================
    // BUSCAR COLABORADORES POR NOMBRE
    // =========================================
    public static List<Colaborador> buscarPorNombre(String nombre) {
        SqlSession conexionBD = MybatisUtil.obtenerConexion();
        if (conexionBD != null) {
            try {
                List<Colaborador> lista
                        = conexionBD.selectList("ColaboradorMapper.buscarPorNombre", nombre);

                return lista;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                conexionBD.close();
            }
        }
        return null;
    }

    // =========================================
    // BUSCAR COLABORADORES POR ROL
    // =========================================
    public static List<Colaborador> buscarPorRol(int idRol) {
        SqlSession conexionBD = MybatisUtil.obtenerConexion();
        if (conexionBD != null) {
            try {
                List<Colaborador> lista
                        = conexionBD.selectList("ColaboradorMapper.buscarPorRol", idRol);

                return lista;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                conexionBD.close();
            }
        }
        return null;
    }

}
