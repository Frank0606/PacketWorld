package uv.tc.controlescolarmt.listener

import uv.tc.controlescolarmt.poko.Paquete

interface PaqueteListener {
    fun onVerDetalle(paquete: Paquete)
    fun onCambiarEstatus(paquete: Paquete)
}