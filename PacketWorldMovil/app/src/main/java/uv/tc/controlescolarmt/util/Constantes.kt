package uv.tc.controlescolarmt.util

object Constantes {

    // URL base de la API. Reemplazar con la URL real del servidor de servicios REST.
    const val URL_API = "http://192.168.1.68:8084/packetworld/api/"

    // Endpoints para la autenticación y gestión del conductor
    const val ENDPOINT_LOGIN = "login/colaborador"
    const val ENDPOINT_EDITAR_CONDUCTOR = "colaborador/editar"


    // Endpoints para la gestión de envíos
    const val ENDPOINT_ENVIOS_ASIGNADOS = "envio/guia-direccion/{guia}"
    const val ENDPOINT_ACTUALIZAR_ESTATUS = "envio-historial/registrar"

    // Claves para Intent y SharedPreferences
    const val KEY_CONDUCTOR = "colaborador_actual"
    const val KEY_ENVIO = "envio_seleccionado"
}