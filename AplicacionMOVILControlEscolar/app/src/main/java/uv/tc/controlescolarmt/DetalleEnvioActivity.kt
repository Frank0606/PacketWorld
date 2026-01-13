package uv.tc.controlescolarmt

import android.os.Bundle
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import uv.tc.controlescolarmt.adapters.PaqueteAdapter
import uv.tc.controlescolarmt.databinding.ActivityDetalleEnvioBinding
import uv.tc.controlescolarmt.listener.PaqueteListener
import uv.tc.controlescolarmt.poko.Cliente
import uv.tc.controlescolarmt.poko.Envio
import uv.tc.controlescolarmt.poko.EstatusEnvio
import uv.tc.controlescolarmt.poko.Paquete
import uv.tc.controlescolarmt.poko.Sucursal
import uv.tc.controlescolarmt.util.Constantes
import java.time.LocalTime

class DetalleEnvioActivity : AppCompatActivity(), PaqueteListener {

    private lateinit var binding: ActivityDetalleEnvioBinding
    private lateinit var envio: Envio
    private lateinit var adapter: PaqueteAdapter
    private val paquetes = mutableListOf<Paquete>()
    private val gson = Gson()
    private lateinit var runnable: Runnable
    private val handler = android.os.Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleEnvioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        runnable = object : Runnable {
            override fun run() {
                refrescar()
                handler.postDelayed(this, 5000)
            }
        }

        adapter = PaqueteAdapter(paquetes, this, gson)
        binding.cardPaquetes.layoutManager = LinearLayoutManager(this)
        binding.cardPaquetes.adapter = adapter

        cargarEnvio()

        binding.imgRegresar.setOnClickListener {
            finish()
        }
        binding.btnCambiarEstatus.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_actualizar_estatus, null)

            val tvEstadoActual = dialogView.findViewById<TextView>(R.id.tv_estado_actual_valor)
            val spinnerEstado = dialogView.findViewById<Spinner>(R.id.sp_estado_nuevo)
            val etComentario = dialogView.findViewById<EditText>(R.id.et_comentario)
            val btnCancelar = dialogView.findViewById<Button>(R.id.btn_cancelar1)
            val btnActualizar = dialogView.findViewById<Button>(R.id.btn_actualizar_dialogo)

            val opciones = listOf("Pendiente", "En tránsito", "Entregado")
            tvEstadoActual.text = envio.estatus
            val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerEstado.adapter = adapterSpinner

            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            btnCancelar.setOnClickListener {
                dialog.dismiss()
            }

            btnActualizar.setOnClickListener {
                val estadoNuevo = spinnerEstado.selectedItem.toString()
                val comentario = etComentario.text.toString()

                val estatusEnvio = EstatusEnvio(
                    idEnvio = envio.idEnvio,
                    estatus = estadoNuevo,
                    comentario = comentario,
                    fechaCambio = LocalTime.now().toString(),
                    idColaborador = envio.idColaborador,
                    idHistorial = null
                )

                val gson = Gson()
                val estatusJson = gson.toJson(estatusEnvio)

                Ion.with(this)
                    .load("POST", "${Constantes.URL_API}envio-historial/registrar")
                    .setHeader("Content-Type", "application/json; charset=UTF-8")
                    .setHeader("Accept", "application/json; charset=UTF-8")
                    .setStringBody(estatusJson)
                    .asString(Charsets.UTF_8)
                    .setCallback { e, result ->
                        if (e != null || result.isNullOrBlank()) {
                            Toast.makeText(this, "Error al actualizar estatus", Toast.LENGTH_SHORT).show()
                            return@setCallback
                        }
                        try {
                            Toast.makeText(this, "Estatus actualizado correctamente", Toast.LENGTH_SHORT).show()
                        } catch (_: Exception) {
                            Toast.makeText(this, "Error al procesar respuesta", Toast.LENGTH_SHORT).show()
                        }

                    }
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume(){
        super.onResume()
        handler.post(runnable)
    }

    private fun refrescar() {
        cargarEnvio()
    }

    private fun cargarEnvio() {
        val envioJson = intent.getStringExtra(Constantes.KEY_ENVIO)

        if (envioJson.isNullOrBlank()) {
            Toast.makeText(this, "Error al cargar envío", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        envio = gson.fromJson(envioJson, Envio::class.java)

        Ion.with(this)
            .load("GET", "${Constantes.URL_API}envio/obtener/${envio.numeroGuia}")
            .asString(Charsets.UTF_8)
            .setCallback { e, result ->
                if (e != null || result.isNullOrBlank()) {
                    Toast.makeText(this, "Error al cargar envío", Toast.LENGTH_LONG).show()
                    return@setCallback
                }
                try {
                    envio = gson.fromJson(result, Envio::class.java)
                    binding.tvGuia.text = "Guía: ${envio.numeroGuia}"
                    binding.tvEstatusActual.text = "Estatus: ${envio.estatus}"
                    binding.tvDestino.text =
                        "Destino: ${envio.calleDestino}, ${envio.numeroDestino}, ${envio.coloniaDestino}"

                    cargarSucursal(envio.idSucursalOrigen)
                    cargarCliente(envio.idCliente)
                    cargarPaquetes(envio.idEnvio)

                } catch (_: Exception) {
                    Toast.makeText(this, "Error al procesar envío", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun cargarSucursal(idSucursal: Int) {
        Ion.with(this)
            .load("GET", "${Constantes.URL_API}sucursal/obtener/$idSucursal")
            .asString(Charsets.UTF_8)
            .setCallback { e, result ->
                if (e != null || result.isNullOrBlank()) {
                    binding.tvOrigen.text = "Sucursal Origen: No disponible"
                    return@setCallback
                }

                try {
                    val sucursal = gson.fromJson(result, Sucursal::class.java)
                    binding.tvOrigen.text =
                        "Sucursal Origen: ${sucursal.nombreCorto}"
                } catch (_: Exception) {
                    binding.tvOrigen.text = "Sucursal Origen: No disponible"
                }
            }
    }

    private fun cargarCliente(idCliente: Int) {
        Ion.with(this)
            .load("GET", "${Constantes.URL_API}cliente/obtener-id/$idCliente")
            .asString(Charsets.UTF_8)
            .setCallback { e, result ->
                if (e != null || result.isNullOrBlank()) {
                    mostrarClienteNoDisponible()
                    return@setCallback
                }

                try {
                    val cliente = gson.fromJson(result, Cliente::class.java)
                    binding.tvClienteNombre.text =
                        "Cliente: ${cliente.nombre} ${cliente.apellidoPaterno} ${cliente.apellidoMaterno}"
                    binding.tvClienteTelefono.text =
                        "Teléfono: ${cliente.telefono}"
                    binding.tvClienteCorreo.text =
                        "Correo: ${cliente.correo}"
                } catch (_: Exception) {
                    mostrarClienteNoDisponible()
                }
            }
    }

    private fun mostrarClienteNoDisponible() {
        binding.tvClienteNombre.text = "Cliente: No disponible"
        binding.tvClienteTelefono.text = "Teléfono: No disponible"
        binding.tvClienteCorreo.text = "Correo: No disponible"
    }

    private fun cargarPaquetes(idEnvio: Int) {
        Ion.with(this)
            .load("GET", "${Constantes.URL_API}paquete/obtener-por-envio/$idEnvio")
            .asString()
            .setCallback { e, result ->
                if (e != null || result.isNullOrBlank()) {
                    Toast.makeText(this, "Error al cargar paquetes", Toast.LENGTH_LONG).show()
                    return@setCallback
                }

                try {
                    val lista = gson.fromJson(result, Array<Paquete>::class.java)
                    paquetes.clear()
                    paquetes.addAll(lista)
                    adapter.notifyDataSetChanged()
                } catch (_: Exception) {
                    Toast.makeText(this, "Error al procesar paquetes", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onVerDetalle(paquete: Paquete) {
        Toast.makeText(this, paquete.descripcion, Toast.LENGTH_SHORT).show()
    }

    override fun onCambiarEstatus(paquete: Paquete) {

    }
}
