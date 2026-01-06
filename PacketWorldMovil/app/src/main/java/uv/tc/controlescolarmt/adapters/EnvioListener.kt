package uv.tc.controlescolarmt.adapters

import uv.tc.controlescolarmt.poko.Envio

interface EnvioListener {
    fun onVerDetalle(envio: Envio)
    fun onCambiarEstatus(envio: Envio)
}
