package uv.tc.controlescolarmt.dto

import com.google.gson.annotations.SerializedName
import uv.tc.controlescolarmt.poko.Envio

data class RSEnvios(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("mensaje")
    val mensaje: String,
    @SerializedName("envios")
    val envios: List<Envio>?
)
