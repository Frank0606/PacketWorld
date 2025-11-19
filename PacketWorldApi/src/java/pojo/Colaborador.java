package pojo;

public class Colaborador {
    
    private Integer idColaborador;
    private String nombreColaborador;
    private String apellidoMaterno;
    private String apellidoPaterno;
    private String curp;
    private String correoElectronico;
    private String noPersonal;
    private String contrasenia;
    private String licencia;
    private byte[] fotografia;
    private Integer idRol;
    private String tipoRol;
    private Integer idUnidad;
    private String vin;

    public Colaborador() {
    }

    public Colaborador(Integer idColaborador, String nombreColaborador, String apellidoMaterno, 
            String apellidoPaterno, String curp, String correoElectronico, String noPersonal, 
            String contrasenia, byte[] fotografia, String licencia, Integer idRol, String tipoRol, Integer idUnidad, String vin) {
        this.idColaborador = idColaborador;
        this.nombreColaborador = nombreColaborador;
        this.apellidoMaterno = apellidoMaterno;
        this.apellidoPaterno = apellidoPaterno;
        this.curp = curp;
        this.correoElectronico = correoElectronico;
        this.noPersonal = noPersonal;
        this.contrasenia = contrasenia;
        this.fotografia = fotografia;
        this.licencia = licencia;
        this.idRol = idRol;
        this.tipoRol = tipoRol;
        this.idUnidad = idUnidad;
        this.vin = vin;
    }

    public Integer getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(Integer idColaborador) {
        this.idColaborador = idColaborador;
    }

    public String getNombreColaborador() {
        return nombreColaborador;
    }

    public void setNombreColaborador(String nombreColaborador) {
        this.nombreColaborador = nombreColaborador;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNoPersonal() {
        return noPersonal;
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonal = noPersonal;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public byte[] getFotografia() {
        return fotografia;
    }

    public void setFotografia(byte[] fotografia) {
        this.fotografia = fotografia;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(String tipoRol) {
        this.tipoRol = tipoRol;
    }

    public Integer getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(Integer idUnidad) {
        this.idUnidad = idUnidad;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public String toString() {
        return "Colaborador{" + "idColaborador=" + idColaborador + ", nombreColaborador=" + nombreColaborador + ", apellidoMaterno=" + apellidoMaterno + ", apellidoPaterno=" + apellidoPaterno + ", curp=" + curp + ", correoElectronico=" + correoElectronico + ", noPersonal=" + noPersonal + ", contrasenia=" + contrasenia + ", fotografia=" + fotografia + ", idRol=" + idRol + ", tipoRol=" + tipoRol + ", idUnidad=" + idUnidad + ", vin=" + vin + '}';
    }
}
