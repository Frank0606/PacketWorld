package uv.tc.controlescolarmt.poko

import com.google.gson.annotations.SerializedName

data class Envio(

    @SerializedName("idEnvio")
    var idEnvio: Int = 0,

    @SerializedName("idCliente")
    var idCliente: Int = 0,

    @SerializedName("idColaborador")
    var idColaborador: Int = 0,

    @SerializedName("nombreDestinatario")
    var nombreDestinatario: String? = null,

    @SerializedName("idSucursalOrigen")
    var idSucursalOrigen: Int = 0,

    @SerializedName("calleDestino")
    var calleDestino: String? = null,

    @SerializedName("numeroDestino")
    var numeroDestino: String? = null,

    @SerializedName("coloniaDestino")
    var coloniaDestino: String? = null,

    @SerializedName("cpDestino")
    var cpDestino: String? = null,

    @SerializedName("ciudadDestino")
    var ciudadDestino: String? = null,

    @SerializedName("idEstadoDestino")
    var idEstadoDestino: Int = 0,

    @SerializedName("idEstadosEnvio")
    var idEstadosEnvio: Int = 0,

    @SerializedName("numeroGuia")
    var numeroGuia: String? = null,

    @SerializedName("distanciaKm")
    var distanciaKm: Float = 0f,

    @SerializedName("costoKm")
    var costoKm: Float = 0f,

    @SerializedName("costoPaquetes")
    var costoPaquetes: Float = 0f,

    @SerializedName("costoTotal")
    var costoTotal: Float = 0f,

    @SerializedName("fechaCreacion")
    var fechaCreacion: String = ""
)