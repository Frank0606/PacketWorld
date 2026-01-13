package packetworldescritorio.pojo;

public class EnvioHistorial {

    private int idHistorial;
    private int idEnvio;
    private int idEstadosEnvio;
    private String comentario;
    private String fechaCambio;
    private int idColaborador;

    public EnvioHistorial() {
    }

    public EnvioHistorial(int idHistorial, int idEnvio, int idEstadosEnvio,
                          String comentario, String fechaCambio, int idColaborador) {
        this.idHistorial = idHistorial;
        this.idEnvio = idEnvio;
        this.idEstadosEnvio = idEstadosEnvio;
        this.comentario = comentario;
        this.fechaCambio = fechaCambio;
        this.idColaborador = idColaborador;
    }

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(int idEnvio) {
        this.idEnvio = idEnvio;
    }

    public int getIdEstadosEnvio() {
        return idEstadosEnvio;
    }

    public void setIdEstadosEnvio(int idEstadosEnvio) {
        this.idEstadosEnvio = idEstadosEnvio;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(String fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public int getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(int idColaborador) {
        this.idColaborador = idColaborador;
    }
}
