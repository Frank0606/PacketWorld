package uv.tc.controlescolarmt.poko

import com.google.gson.annotations.SerializedName

data class Paquete(
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("peso")
    val peso: Double,
    @SerializedName("alto")
    val alto: Double,
    @SerializedName("ancho")
    val ancho: Double,
    @SerializedName("profundidad")
    val profundidad: Double,
    @SerializedName("idPaquete")
    val idPaquete: Int,
    @SerializedName("numeroGuia")
    val numeroGuia: String,
    @SerializedName("idEnvio")
    val idEnvio: Int
)
