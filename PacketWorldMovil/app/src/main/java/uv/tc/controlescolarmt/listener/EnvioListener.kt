package uv.tc.controlescolarmt.listener

import uv.tc.controlescolarmt.poko.Envio

interface EnvioListener {
    fun onVerDetalle(envio: Envio)
}