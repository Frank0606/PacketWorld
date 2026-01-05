package pojo;

public class RespuestaApiKm {
    
    private float distanciaKM;
    private boolean error;
    private String mensaje;

    public RespuestaApiKm() {
    }

    public RespuestaApiKm(float distanciaKM, boolean error, String mensaje) {
        this.distanciaKM = distanciaKM;
        this.error = error;
        this.mensaje = mensaje;
    }

    public float getDistanciaKM() {
        return distanciaKM;
    }

    public void setDistanciaKM(float distanciaKM) {
        this.distanciaKM = distanciaKM;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    
}
