package packetworldescritorio.pojo;

public class EstadosEnvio {

    private int idEstadosEnvio;
    private String estadoEnvio;

    public EstadosEnvio() {
    }

    public EstadosEnvio(int idEstadosEnvio, String estadoEnvio) {
        this.idEstadosEnvio = idEstadosEnvio;
        this.estadoEnvio = estadoEnvio;
    }

    public int getIdEstadosEnvio() {
        return idEstadosEnvio;
    }

    public void setIdEstadosEnvio(int idEstadosEnvio) {
        this.idEstadosEnvio = idEstadosEnvio;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    @Override
    public String toString() {
        return estadoEnvio;
    }
}
