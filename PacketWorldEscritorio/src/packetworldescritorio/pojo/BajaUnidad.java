package packetworldescritorio.pojo;

public class BajaUnidad {
    
    private Integer idBajaUnidad;
    private String vin;
    private String motivo;

    public BajaUnidad() {
    }

    public BajaUnidad(Integer idBajaUnidad, String vin, String motivo) {
        this.idBajaUnidad = idBajaUnidad;
        this.vin = vin;
        this.motivo = motivo;
    }

    public Integer getIdBajaUnidad() {
        return idBajaUnidad;
    }

    public void setIdBajaUnidad(Integer idBajaUnidad) {
        this.idBajaUnidad = idBajaUnidad;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public String toString() {
        return "BajaUnidad{" + "idBajaUnidad=" + idBajaUnidad + ", vin=" + vin + ", motivo=" + motivo + '}';
    }
    
}
