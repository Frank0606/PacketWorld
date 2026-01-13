package uv.tc.controlescolarmt.poko
import com.google.gson.annotations.SerializedName
data class Sucursal(

    @SerializedName("idSucursal")
    val idSucursal: Int,
    @SerializedName("codigo")
    val codigo: String,
    @SerializedName("nombreCorto")
    val nombreCorto: String,
    @SerializedName("estatus")
    val estatus: String,
    @SerializedName("calle")
    val calle: String,
    @SerializedName("numero")
    val numero: String,
    @SerializedName("colonia")
    val colonia: String,
    @SerializedName("codigoPostal")
    val codigoPostal: String,
    @SerializedName("ciudad")
    val ciudad: String,
    @SerializedName("idEstado")
    val idEstado: Int

)
