package pojo;

public class Rol {
    private Integer idRol;
    private String nombreR;

    public Rol() {
    }

    public Rol(Integer idRol, String NombreR) {
        this.idRol = idRol;
        this.nombreR = NombreR;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombreR() {
        return nombreR;
    }

    public void setNombreR(String NombreR) {
        this.nombreR = NombreR;
    }
}
