package uv.tc.controlescolarmt

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import uv.tc.controlescolarmt.adapters.EstadoAdapter
import uv.tc.controlescolarmt.databinding.ActivityDetalleEnvioBinding
import uv.tc.controlescolarmt.databinding.DialogActualizarEstatusBinding
import uv.tc.controlescolarmt.dto.Respuesta
import uv.tc.controlescolarmt.poko.Colaborador
import uv.tc.controlescolarmt.poko.Envio
import uv.tc.controlescolarmt.poko.EstatusEnvio
import uv.tc.controlescolarmt.util.Constantes

class DetalleEnvioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleEnvioBinding
    private lateinit var envio: Envio

    // Estatus EXACTOS como los maneja la API
    private val estatusDisponibles = listOf(
        "EN_TRANSITO",
        "DETENIDO",
        "ENTREGADO",
        "CANCELADO"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleEnvioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val envioJson = intent.getStringExtra(Constantes.KEY_ENVIO)
        if (envioJson == null) {
            Toast.makeText(this, "Error al cargar el envío.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        envio = Gson().fromJson(envioJson, Envio::class.java)
        mostrarDetallesEnvio()

        binding.btnCambiarEstatus.setOnClickListener {
            mostrarDialogoActualizacion()
        }
    }

    private fun mostrarDetallesEnvio() {
        binding.tvGuia.text = "Guía: ${envio.numeroGuia}"
        binding.tvEstatusActual.text = "Estatus Actual: ${envio.estatus}"
        binding.tvOrigen.text = "Sucursal Origen: ${envio.idSucursalOrigen}"
        binding.tvDestinatario.text =
            "Destinatario: ${envio.nombreDestinatario} ${envio.apellidoPaternoDest} ${envio.apellidoMaternoDest}"
        binding.tvDireccion.text =
            "Dirección: ${envio.calleDestino}, ${envio.numeroDestino}, ${envio.coloniaDestino}, ${envio.cpDestino}, ${envio.ciudadDestino}, ${envio.estadoDestino}"
        binding.tvContacto.text = "Cliente ID: ${envio.idCliente}"

    }

    private fun mostrarDialogoActualizacion() {

        val dialogBinding = DialogActualizarEstatusBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.tvEstadoActualValor.text = envio.estatus

        dialogBinding.spEstadoNuevo.adapter =
            EstadoAdapter(this, estatusDisponibles)

        dialogBinding.btnActualizarDialogo.setOnClickListener {

            val nuevoEstatus = dialogBinding.spEstadoNuevo.selectedItem.toString()
            val comentario = dialogBinding.etComentario.text.toString().trim()

            if ((nuevoEstatus == "DETENIDO" || nuevoEstatus == "CANCELADO") && comentario.isEmpty()) {
                Toast.makeText(
                    this,
                    "El comentario es obligatorio para este estatus.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            actualizarEstatus(nuevoEstatus, comentario)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun actualizarEstatus(nuevoEstatus: String, comentario: String) {

        val pref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val conductorJson = pref.getString(Constantes.KEY_CONDUCTOR, null)

        if (conductorJson == null) {
            Toast.makeText(this, "Sesión no válida.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val colaborador = Gson().fromJson(conductorJson, Colaborador::class.java)

        val estatusEnvio : EstatusEnvio = EstatusEnvio(idEnvio = 0, estatus = "", comentario = "", fechaCambio = "", idColaborador = 0, idHistorial = 0);
        estatusEnvio.idEnvio = envio.idEnvio;
        estatusEnvio.estatus = nuevoEstatus;
        estatusEnvio.comentario = comentario;
        estatusEnvio.fechaCambio = "";
        estatusEnvio.idColaborador = colaborador.idColaborador;

        var jsonEnvioHistorial = Gson().toJson(estatusEnvio)

        Ion.with(this)
            .load("POST", "${Constantes.URL_API}${Constantes.ENDPOINT_ACTUALIZAR_ESTATUS}")
            .setHeader("Content-Type", "application/json")
            .setStringBody(jsonEnvioHistorial)
            .asString()
            .setCallback { e, result ->

                if (e != null) {
                    Toast.makeText(
                        this,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setCallback
                }

                try {
                    val respuesta = Gson().fromJson(result, Respuesta::class.java)

                    // ⚠️ No usamos respuesta.error como lógica
                    Toast.makeText(
                        this,
                        respuesta.mensaje,
                        Toast.LENGTH_LONG
                    ).show()

                    envio.estatus = nuevoEstatus
                    binding.tvEstatusActual.text = "Estatus Actual: $nuevoEstatus"
                    setResult(RESULT_OK)

                } catch (ex: Exception) {
                    Toast.makeText(
                        this,
                        "Error al procesar la respuesta del servidor.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
