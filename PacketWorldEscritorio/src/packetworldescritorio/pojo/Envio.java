package packetworldescritorio.pojo;

public class Envio {

    private int idEnvio;
    private int idCliente;
    private int idColaborador;
    private String nombreDestinatario;
    private String apellidoPaternoDest;
    private String apellidoMaternoDest;
    private int idSucursalOrigen;
    private String calleDestino;
    private String numeroDestino;
    private String coloniaDestino;
    private String cpDestino;
    private String ciudadDestino;
    private String estadoDestino;
    private String numeroGuia;
    private float distanciaKm;
    private float costoKm;
    private float costoPaquetes;
    private float costoTotal;
    private String estatus;
    private String fechaCreacion;

    public Envio() {
    }

    public Envio(int idEnvio, int idCliente, int idColaborador, String nombreDestinatario, String apellidoPaternoDest, String apellidoMaternoDest, int idSucursalOrigen, String calleDestino, String numeroDestino, String coloniaDestino, String cpDestino, String ciudadDestino, String estadoDestino, String numeroGuia, float distanciaKm, float costoKm, float costoPaquetes, float costoTotal, String estatus, String fechaCreacion) {
        this.idEnvio = idEnvio;
        this.idCliente = idCliente;
        this.idColaborador = idColaborador;
        this.nombreDestinatario = nombreDestinatario;
        this.apellidoPaternoDest = apellidoPaternoDest;
        this.apellidoMaternoDest = apellidoMaternoDest;
        this.idSucursalOrigen = idSucursalOrigen;
        this.calleDestino = calleDestino;
        this.numeroDestino = numeroDestino;
        this.coloniaDestino = coloniaDestino;
        this.cpDestino = cpDestino;
        this.ciudadDestino = ciudadDestino;
        this.estadoDestino = estadoDestino;
        this.numeroGuia = numeroGuia;
        this.distanciaKm = distanciaKm;
        this.costoKm = costoKm;
        this.costoPaquetes = costoPaquetes;
        this.costoTotal = costoTotal;
        this.estatus = estatus;
        this.fechaCreacion = fechaCreacion;
    }
    
    public int getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(int idEnvio) {
        this.idEnvio = idEnvio;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(int idColaborador) {
        this.idColaborador = idColaborador;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    public String getApellidoPaternoDest() {
        return apellidoPaternoDest;
    }

    public void setApellidoPaternoDest(String apellidoPaternoDest) {
        this.apellidoPaternoDest = apellidoPaternoDest;
    }

    public String getApellidoMaternoDest() {
        return apellidoMaternoDest;
    }

    public void setApellidoMaternoDest(String apellidoMaternoDest) {
        this.apellidoMaternoDest = apellidoMaternoDest;
    }

    public int getIdSucursalOrigen() {
        return idSucursalOrigen;
    }

    public void setIdSucursalOrigen(int idSucursalOrigen) {
        this.idSucursalOrigen = idSucursalOrigen;
    }

    public String getCalleDestino() {
        return calleDestino;
    }

    public void setCalleDestino(String calleDestino) {
        this.calleDestino = calleDestino;
    }

    public String getNumeroDestino() {
        return numeroDestino;
    }

    public void setNumeroDestino(String numeroDestino) {
        this.numeroDestino = numeroDestino;
    }

    public String getColoniaDestino() {
        return coloniaDestino;
    }

    public void setColoniaDestino(String coloniaDestino) {
        this.coloniaDestino = coloniaDestino;
    }

    public String getCpDestino() {
        return cpDestino;
    }

    public void setCpDestino(String cpDestino) {
        this.cpDestino = cpDestino;
    }

    public String getCiudadDestino() {
        return ciudadDestino;
    }

    public void setCiudadDestino(String ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public String getEstadoDestino() {
        return estadoDestino;
    }

    public void setEstadoDestino(String estadoDestino) {
        this.estadoDestino = estadoDestino;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public float getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(float distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public float getCostoKm() {
        return costoKm;
    }

    public void setCostoKm(float costoKm) {
        this.costoKm = costoKm;
    }

    public float getCostoPaquetes() {
        return costoPaquetes;
    }

    public void setCostoPaquetes(float costoPaquetes) {
        this.costoPaquetes = costoPaquetes;
    }

    public float getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(float costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return numeroGuia;
    }
    
    
}
