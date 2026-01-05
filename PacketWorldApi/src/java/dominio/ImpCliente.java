package dominio;

import java.util.List;
import mybatis.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Cliente;
import pojo.Mensaje;

public class ImpCliente {

    // ==========================================================
    // OBTENER TODOS
    // ==========================================================
    public static List<Cliente> obtenerClientes() {
        SqlSession conexion = MybatisUtil.obtenerConexion();
        if (conexion != null) {
            try {
                List<Cliente> lista = conexion.selectList("ClienteMapper.obtenerTodos");
                conexion.commit();
                return lista;
            } catch (Exception e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.close();
            }
        }
        return null;
    }

    // ==========================================================
    // OBTENER POR ID
    // ==========================================================
    public static Cliente obtenerPorId(int idCliente) {
        SqlSession conexion = MybatisUtil.obtenerConexion();
        if (conexion != null) {
            try {
                Cliente cliente = conexion.selectOne("ClienteMapper.obtenerPorId", idCliente);
                conexion.commit();
                return cliente;
            } catch (Exception e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.close();
            }
        }
        return null;
    }

    // ==========================================================
    // BUSCAR POR NOMBRE
    // ==========================================================
    public static List<Cliente> buscarPorNombre(String nombre) {
        SqlSession conexion = MybatisUtil.obtenerConexion();
        if (conexion != null) {
            try {
                List<Cliente> clientes =
                        conexion.selectList("ClienteMapper.buscarPorNombre", nombre);

                conexion.commit();
                return clientes;
            } catch (Exception e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.close();
            }
        }
        return null;
    }

    // ==========================================================
    // BUSCAR POR TELÉFONO
    // ==========================================================
    public static Cliente buscarPorTelefono(String telefono) {
        SqlSession conexion = MybatisUtil.obtenerConexion();
        if (conexion != null) {
            try {
                Cliente cliente =
                        conexion.selectOne("ClienteMapper.buscarPorTelefono", telefono);

                conexion.commit();
                return cliente;
            } catch (Exception e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.close();
            }
        }
        return null;
    }

    // ==========================================================
    // BUSCAR POR CORREO
    // ==========================================================
    public static Cliente buscarPorCorreo(String correo) {
        SqlSession conexion = MybatisUtil.obtenerConexion();
        if (conexion != null) {
            try {
                Cliente cliente =
                        conexion.selectOne("ClienteMapper.buscarPorCorreo", correo);

                conexion.commit();
                return cliente;
            } catch (Exception e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.close();
            }
        }
        return null;
    }

    // ==========================================================
    // REGISTRAR
    // ==========================================================
    public static Mensaje registrar(Cliente cliente) {
        Mensaje msj = new Mensaje();
        SqlSession conexion = MybatisUtil.obtenerConexion();

        if (conexion != null) {
            try {
                int resultado =
                        conexion.insert("ClienteMapper.registrar", cliente);

                conexion.commit();

                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Cliente registrado con éxito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo registrar el cliente.");
                }
            } catch (Exception e) {
                conexion.rollback();
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                conexion.close();
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Servicio no disponible.");
        }
        return msj;
    }

    // ==========================================================
    // EDITAR
    // ==========================================================
    public static Mensaje editar(Cliente cliente) {
        Mensaje msj = new Mensaje();
        SqlSession conexion = MybatisUtil.obtenerConexion();

        if (conexion != null) {
            try {
                int resultado =
                        conexion.update("ClienteMapper.editar", cliente);

                conexion.commit();

                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Cliente actualizado con éxito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo actualizar el cliente.");
                }
            } catch (Exception e) {
                conexion.rollback();
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                conexion.close();
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Servicio no disponible.");
        }
        return msj;
    }

    // ==========================================================
    // ELIMINAR
    // ==========================================================
    public static Mensaje eliminar(String correo) {
        Mensaje msj = new Mensaje();
        SqlSession conexion = MybatisUtil.obtenerConexion();

        if (conexion != null) {
            try {
                int resultado =
                        conexion.delete("ClienteMapper.eliminar", correo);

                conexion.commit();

                if (resultado > 0) {
                    msj.setError(false);
                    msj.setMensaje("Cliente eliminado con éxito.");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se pudo eliminar el cliente.");
                }

            } catch (Exception e) {
                conexion.rollback();
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            } finally {
                conexion.close();
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Servicio no disponible.");
        }

        return msj;
    }

}
