package uv.tc.controlescolarmt.poko

import com.google.gson.annotations.SerializedName

data class Colaborador(

    @SerializedName("idColaborador")
    val idColaborador: Int,
    @SerializedName("numeroPersonal")
    val numeroPersonal: String,
    @SerializedName("nombre")
    var nombre: String,
    @SerializedName("apellidoPaterno")
    var apellidoPaterno: String,
    @SerializedName("apellidoMaterno")
    var apellidoMaterno: String,
    @SerializedName("curp")
    var curp: String,
    @SerializedName("correo")
    var correo: String,
    @SerializedName("contrasena")
    var contrasena: String,
    @SerializedName("numeroLicencia")
    var numeroLicencia: String,
    @SerializedName("nombreRol")
    val nombreRol: String,
    @SerializedName("nombreSucursal")
    val nombreSucursal: String,
    @SerializedName("fotografia")
    var fotografia: String? = null,
    @SerializedName("idSucursal")
    var idSucursal: Int,
    @SerializedName("idUnidad")
    var idUnidad: Integer
)
