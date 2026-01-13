package uv.tc.controlescolarmt.poko
import com.google.gson.annotations.SerializedName

data class EstatusEnvio(

    @SerializedName("idEnvio") var idEnvio: Int,
    @SerializedName("idEstadosEnvio") var idEstadosEnvio: Int,
    @SerializedName("comentario") var comentario: String?,
    @SerializedName("fechaCambio") var fechaCambio: String?,
    @SerializedName("idColaborador") var idColaborador: Int,
    @SerializedName("idHistorial") var idHistorial: Int?
)




