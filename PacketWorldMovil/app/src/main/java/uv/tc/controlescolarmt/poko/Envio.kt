package uv.tc.controlescolarmt.poko

import com.google.gson.annotations.SerializedName

data class Envio(
    @SerializedName("idEnvio")
    val idEnvio: Int,
    @SerializedName("idCliente")
    val idCliente: Int,
    @SerializedName("idColaborador")
    val idColaborador: Int,
    @SerializedName("nombreDestinatario")
    val nombreDestinatario: String,
    @SerializedName("apellidoPaternoDest")
    val apellidoPaternoDest: String,
    @SerializedName("apellidoMaternoDest")
    val apellidoMaternoDest: String,
    @SerializedName("idSucursalOrigen")
    val idSucursalOrigen: Int,
    @SerializedName("calleDestino")
    val calleDestino: String,
    @SerializedName("numeroDestino")
    val numeroDestino: String,
    @SerializedName("coloniaDestino")
    val coloniaDestino: String,
    @SerializedName("cpDestino")
    val cpDestino: String,
    @SerializedName("ciudadDestino")
    val ciudadDestino: String,
    @SerializedName("estadoDestino")
    val estadoDestino: String,
    @SerializedName("numeroGuia")
    val numeroGuia: String,
    @SerializedName("distanciaKm")
    val distanciaKm: Float,
    @SerializedName("costoKm")
    val costoKm: Float,
    @SerializedName("costoPaquetes")
    val costoPaquetes: Float,
    @SerializedName("costoTotal")
    val costoTotal: Float,
    @SerializedName("estatus")
    var estatus: String,
    @SerializedName("fechaCreacion")
    val fechaCreacion: String
)
