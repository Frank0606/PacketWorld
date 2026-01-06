package uv.tc.controlescolarmt.dto

import com.google.gson.annotations.SerializedName
import uv.tc.controlescolarmt.poko.Colaborador

data class RSAutenticacionConductor(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("mensaje")
    val mensaje: String,
    @SerializedName("colaborador")
    val colaborador: Colaborador?
)
