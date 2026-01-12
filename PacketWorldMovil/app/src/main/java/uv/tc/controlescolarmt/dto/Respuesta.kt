package uv.tc.controlescolarmt.dto

import com.google.gson.annotations.SerializedName

data class Respuesta(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("mensaje")
    val mensaje: String
)
