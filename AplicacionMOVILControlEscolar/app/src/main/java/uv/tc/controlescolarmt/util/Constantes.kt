package uv.tc.controlescolarmt.util

object Constantes {

    // URL base de la API. Reemplazar con la URL real del servidor de servicios REST.
    const val URL_API = "http://192.168.100.17:8084/packetworld/api/"

    // Endpoints para la autenticación y gestión del conductor
    const val ENDPOINT_LOGIN = "login/colaborador"
    const val ENDPOINT_EDITAR_CONDUCTOR = "colaborador/editar"

    // Claves para Intent y SharedPreferences
    const val KEY_CONDUCTOR = "colaborador_actual"
    const val KEY_ENVIO = "envio_seleccionado"
}