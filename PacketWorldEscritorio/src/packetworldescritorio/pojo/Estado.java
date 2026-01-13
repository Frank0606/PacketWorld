package packetworldescritorio.pojo;

public class Estado {
    private Integer idEstado;
    private String estadoMexico;

    public Estado() {}

    public Estado(Integer idEstado, String estadoMexico) {
        this.idEstado = idEstado;
        this.estadoMexico = estadoMexico;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public String getEstadoMexico() {
        return estadoMexico;
    }

    public void setEstadoMexico(String estadoMexico) {
        this.estadoMexico = estadoMexico;
    }

    @Override
    public String toString() {
        return estadoMexico;
    }
}