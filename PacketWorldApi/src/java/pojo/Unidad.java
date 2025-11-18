package pojo;

public class Unidad {

    private Integer idUnidad;
    private String marca;
    private String modelo;
    private int anio;
    private String vin;
    private String noIdentificacion;
    private Integer idTipoUnidad;
    private String tipoUnidad;
    private String noPersonal;

    public Unidad() {
    }

    public Unidad(Integer idUnidad, String marca, String modelo, int anio, String vin,
            String noIdentificacion, Integer idTipoUnidad, String tipoUnidad, String noPersonal) {
        this.idUnidad = idUnidad;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.vin = vin;
        this.noIdentificacion = noIdentificacion;
        this.idTipoUnidad = idTipoUnidad;
        this.tipoUnidad = tipoUnidad;
        this.noPersonal = noPersonal;
    }

    public String getNoPersonal() {
        return noPersonal;
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonal = noPersonal;
    }

    public Integer getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(Integer idUnidad) {
        this.idUnidad = idUnidad;
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

    public Integer getIdTipoUnidad() {
        return idTipoUnidad;
    }

    public void setIdTipoUnidad(Integer idTipoUnidad) {
        this.idTipoUnidad = idTipoUnidad;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    @Override
    public String toString() {
        return "Unidad{" + "idUnidad=" + idUnidad + ", marca=" + marca + ", modelo=" + modelo + ", anio=" + anio + ", vin=" + vin + ", noIdentificacion=" + noIdentificacion + ", idTipoUnidad=" + idTipoUnidad + ", tipoUnidad=" + tipoUnidad + ", noPersonal=" + noPersonal + '}';
    }

}
