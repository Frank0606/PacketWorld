package uv.tc.controlescolarmt

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import uv.tc.controlescolarmt.adapters.PaqueteAdapter
import uv.tc.controlescolarmt.databinding.ActivityDetalleEnvioBinding
import uv.tc.controlescolarmt.listener.PaqueteListener
import uv.tc.controlescolarmt.poko.*
import uv.tc.controlescolarmt.util.Constantes
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DetalleEnvioActivity : AppCompatActivity(), PaqueteListener {

    private lateinit var binding: ActivityDetalleEnvioBinding
    private lateinit var envio: Envio
    private lateinit var adapter: PaqueteAdapter
    private val paquetes = mutableListOf<Paquete>()
    private val gson = Gson()
    private lateinit var runnable: Runnable
    private val handler = android.os.Handler(Looper.getMainLooper())

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleEnvioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        runnable = object : Runnable {
            override fun run() {
                cargarEnvio()
                handler.postDelayed(this, 5000)
            }
        }

        adapter = PaqueteAdapter(paquetes, this, gson)
        binding.cardPaquetes.layoutManager = LinearLayoutManager(this)
        binding.cardPaquetes.adapter = adapter

        cargarEnvio()

        binding.imgRegresar.setOnClickListener {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }

        binding.btnCambiarEstatus.setOnClickListener { mostrarDialogoCambioEstatus() }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.post(runnable)
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
            .load("${Constantes.URL_API}envio/obtener/${envio.numeroGuia}")
            .asString(Charsets.UTF_8)
            .setCallback { e, result ->
                if (e != null || result.isNullOrBlank()) {
                    Toast.makeText(this, "Error al cargar envío", Toast.LENGTH_LONG).show()
                    return@setCallback
                }

                envio = gson.fromJson(result, Envio::class.java)

                binding.tvGuia.text = "Guía: ${envio.numeroGuia}"
                binding.tvDestino.text =
                    "Destino: ${envio.calleDestino}, ${envio.numeroDestino}, ${envio.coloniaDestino}"

                cargarEstadoDestino(envio.idEstadoDestino)
                cargarEstatus(envio.idEstadosEnvio)
                cargarSucursal(envio.idSucursalOrigen)
                cargarCliente(envio.idCliente)
                cargarPaquetes(envio.idEnvio)
            }
    }

    private fun cargarEstatus(idEstatus: Int) {
        Ion.with(this)
            .load("${Constantes.URL_API}estados-envio/obtener/$idEstatus")
            .asString(Charsets.UTF_8)
            .setCallback { e, result ->
                binding.tvEstatusActual.text =
                    if (e != null || result.isNullOrBlank()) {
                        "Estatus: No disponible"
                    } else {
                        try {
                            val estatus = gson.fromJson(result, Estatus::class.java)
                            "Estatus: ${estatus.estadoEnvio}"
                        } catch (_: Exception) {
                            "Estatus: No disponible"
                        }
                    }
            }
    }

    private fun cargarEstadoDestino(idEstado: Int) {
        Ion.with(this)
            .load("${Constantes.URL_API}estado/obtener-id/$idEstado")
            .asString(Charsets.UTF_8)
            .setCallback { e, result ->
                val textoEstado =
                    if (e != null || result.isNullOrBlank()) {
                        "Estado: No disponible"
                    } else {
                        try {
                            val estado = gson.fromJson(result, Estado::class.java)
                            "Estado: ${estado.estadoMexico}"
                        } catch (_: Exception) {
                            "Estado: No disponible"
                        }
                    }

                binding.tvDestino.append("\n$textoEstado")
            }
    }

    private fun cargarSucursal(idSucursal: Int) {
        Ion.with(this)
            .load("${Constantes.URL_API}sucursal/obtener/$idSucursal")
            .asString(Charsets.UTF_8)
            .setCallback { _, result ->
                binding.tvOrigen.text =
                    try {
                        val sucursal = gson.fromJson(result, Sucursal::class.java)
                        "Sucursal Origen: ${sucursal.nombreCorto}"
                    } catch (_: Exception) {
                        "Sucursal Origen: No disponible"
                    }
            }
    }

    private fun cargarCliente(idCliente: Int) {
        Ion.with(this)
            .load("${Constantes.URL_API}cliente/obtener-id/$idCliente")
            .asString(Charsets.UTF_8)
            .setCallback { _, result ->
                try {
                    val cliente = gson.fromJson(result, Cliente::class.java)
                    binding.tvClienteNombre.text =
                        "Cliente: ${cliente.nombre} ${cliente.apellidoPaterno} ${cliente.apellidoMaterno}"
                    binding.tvClienteTelefono.text = "Teléfono: ${cliente.telefono}"
                    binding.tvClienteCorreo.text = "Correo: ${cliente.correo}"
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
            .load("${Constantes.URL_API}paquete/obtener-por-envio/$idEnvio")
            .asString()
            .setCallback { _, result ->
                try {
                    val lista = gson.fromJson(result, Array<Paquete>::class.java)
                    paquetes.clear()
                    paquetes.addAll(lista)
                    adapter.notifyDataSetChanged()
                } catch (_: Exception) {
                    Toast.makeText(this, "Error al cargar paquetes", Toast.LENGTH_LONG).show()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrarDialogoCambioEstatus() {

        val view = layoutInflater.inflate(R.layout.dialog_actualizar_estatus, null)

        val spinner = view.findViewById<Spinner>(R.id.sp_estado_nuevo)
        val etComentario = view.findViewById<EditText>(R.id.et_comentario)
        val tvErrorEstado = view.findViewById<TextView>(R.id.tv_error_estado)
        val tvErrorComentario = view.findViewById<TextView>(R.id.tv_error_comentario)

        tvErrorEstado.text = ""
        tvErrorComentario.text = ""

        // 1️⃣ Cargar estados desde API
        Ion.with(this)
            .load("${Constantes.URL_API}estados-envio/obtener-todos")
            .asString(Charsets.UTF_8)
            .setCallback { e, result ->

                if (e != null || result.isNullOrBlank()) {
                    Toast.makeText(this, "Error al cargar estados", Toast.LENGTH_LONG).show()
                    return@setCallback
                }

                val todos = gson.fromJson(result, Array<Estatus>::class.java)

                // 2️⃣ Filtrar EXACTAMENTE como en escritorio
                val estadosFiltrados = todos.filter {
                    val nombre = it.estadoEnvio.lowercase()
                    nombre != "procesado" &&
                            nombre != "recibido en sucursal"
                }

                val adapterSpinner = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    estadosFiltrados.map { it.estadoEnvio }
                )

                spinner.adapter = adapterSpinner

                // 3️⃣ Mostrar diálogo solo cuando el spinner ya está listo
                AlertDialog.Builder(this)
                    .setView(view)
                    .setPositiveButton("Actualizar", null)
                    .setNegativeButton("Cancelar", null)
                    .create()
                    .also { dialog ->

                        dialog.setOnShowListener {

                            val btnActualizar =
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE)

                            btnActualizar.setOnClickListener {

                                tvErrorEstado.text = ""
                                tvErrorComentario.text = ""

                                val pos = spinner.selectedItemPosition
                                if (pos == AdapterView.INVALID_POSITION) {
                                    tvErrorEstado.text = "Selecciona un estado."
                                    return@setOnClickListener
                                }

                                val estadoSeleccionado = estadosFiltrados[pos]
                                val nombreEstado =
                                    estadoSeleccionado.estadoEnvio.lowercase()

                                val comentario = etComentario.text.toString().trim()

                                // 4️⃣ Validación EXACTA de escritorio
                                if ((nombreEstado == "detenido" || nombreEstado == "cancelado")
                                    && comentario.isEmpty()
                                ) {
                                    tvErrorComentario.text =
                                        "El comentario es obligatorio para estados detenidos o cancelados."
                                    return@setOnClickListener
                                }

                                val fechaCambio = LocalDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

                                // 5️⃣ Construcción del objeto
                                val estatusEnvio = EstatusEnvio(
                                    idEnvio = envio.idEnvio,
                                    idEstadosEnvio = estadoSeleccionado.idEstadosEnvio,
                                    comentario = comentario,
                                    fechaCambio = fechaCambio,
                                    idColaborador = envio.idColaborador,
                                    idHistorial = null
                                )

                                // 6️⃣ Envío
                                Ion.with(this@DetalleEnvioActivity)
                                    .load(
                                        "POST",
                                        "${Constantes.URL_API}envio-historial/registrar"
                                    )
                                    .setHeader(
                                        "Content-Type",
                                        "application/json; charset=UTF-8"
                                    )
                                    .setStringBody(gson.toJson(estatusEnvio))
                                    .asString()
                                    .setCallback { ex, _ ->

                                        if (ex != null) {
                                            Toast.makeText(
                                                this@DetalleEnvioActivity,
                                                "No se pudo actualizar el estatus",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            return@setCallback
                                        }

                                        Toast.makeText(
                                            this@DetalleEnvioActivity,
                                            "Estatus actualizado correctamente",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        dialog.dismiss()
                                        cargarEnvio()
                                    }
                            }

                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                                .setOnClickListener { dialog.dismiss() }
                        }

                        dialog.show()
                    }
            }
    }

    override fun onVerDetalle(paquete: Paquete) {
        Toast.makeText(this, paquete.descripcion, Toast.LENGTH_SHORT).show()
    }

    override fun onCambiarEstatus(paquete: Paquete) {}
}