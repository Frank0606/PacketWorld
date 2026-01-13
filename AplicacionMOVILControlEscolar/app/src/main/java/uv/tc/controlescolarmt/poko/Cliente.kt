package uv.tc.controlescolarmt.poko

data class Cliente(
    val idCliente: Int,
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val telefono: String,
    val correo: String
)
