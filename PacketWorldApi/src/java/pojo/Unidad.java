package pojo;

public class Unidad {

    private int idUnidad;
    private int anio;
    private String vin;
    private String noIdentificacion;
    private String numeroPersonal;
    private int idTipoUnidad;
    private String tipoUnidad;
    private String numeroInterno;
    private String marca;
    private String modelo;
    private String estatus;

    public Unidad() {
    }

    public Unidad(int idUnidad, int anio, String vin, String noIdentificacion, String numeroPersonal, int idTipoUnidad, String tipoUnidad, String numeroInterno, String marca, String modelo, String estatus) {
        this.idUnidad = idUnidad;
        this.anio = anio;
        this.vin = vin;
        this.noIdentificacion = noIdentificacion;
        this.numeroPersonal = numeroPersonal;
        this.idTipoUnidad = idTipoUnidad;
        this.tipoUnidad = tipoUnidad;
        this.numeroInterno = numeroInterno;
        this.marca = marca;
        this.modelo = modelo;
        this.estatus = estatus;
    }

    public int getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(int idUnidad) {
        this.idUnidad = idUnidad;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getNoIdentificacion() {
        return noIdentificacion;
    }

    public void setNoIdentificacion(String noIdentificacion) {
        this.noIdentificacion = noIdentificacion;
    }

    public String getNumeroPersonal() {
        return numeroPersonal;
    }

    public void setNumeroPersonal(String numeroPersonal) {
        this.numeroPersonal = numeroPersonal;
    }

    public int getIdTipoUnidad() {
        return idTipoUnidad;
    }

    public void setIdTipoUnidad(int idTipoUnidad) {
        this.idTipoUnidad = idTipoUnidad;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public String getNumeroInterno() {
        return numeroInterno;
    }

    public void setNumeroInterno(String numeroInterno) {
        this.numeroInterno = numeroInterno;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
