package uv.tc.controlescolarmt.poko

data class Cliente(
    val idCliente: Int,
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val telefono: String,
    val correo: String,
    val calle: String,
    val numero: String,
    val colonia: String,
    val codigoPostal: String,
    val ciudad: String,
    val idEstado: Int
)
