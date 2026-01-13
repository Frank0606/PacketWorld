package uv.tc.controlescolarmt

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import uv.tc.controlescolarmt.adapters.EnvioAdapter
import uv.tc.controlescolarmt.databinding.ActivityMainBinding
import uv.tc.controlescolarmt.listener.EnvioListener
import uv.tc.controlescolarmt.poko.Colaborador
import uv.tc.controlescolarmt.poko.Envio
import uv.tc.controlescolarmt.poko.Estado
import uv.tc.controlescolarmt.poko.Estatus
import uv.tc.controlescolarmt.util.Constantes

class MainActivity : AppCompatActivity(), EnvioListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var colaborador: Colaborador
    private lateinit var adapter: EnvioAdapter
    private val envios = mutableListOf<Envio>()
    private val mapaEstados = mutableMapOf<Int, String>()
    private val mapaEstatus = mutableMapOf<Int, String>()
    private val handler = android.os.Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarDatosConductor()
        configurarRecycler()

        cargarCatalogos {
            cargarEnviosAsignados()
        }

        runnable = object : Runnable {
            override fun run() {
                cargarEnviosAsignados()
                handler.postDelayed(this, 5000)
            }
        }

        binding.btnPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilConductorActivity::class.java))
            finish()
        }

        binding.btnLogout.setOnClickListener {
            mostrarConfirmacionCerrarSesion()
        }
    }

    override fun onResume() {
        super.onResume()
        handler.post(runnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    private fun configurarRecycler() {
        adapter = EnvioAdapter(envios, mapaEstados, mapaEstatus, this)
        binding.rvEnvios.layoutManager = LinearLayoutManager(this)
        binding.rvEnvios.adapter = adapter
    }

    private fun cargarDatosConductor() {
        val json = getSharedPreferences("app_prefs", MODE_PRIVATE)
            .getString(Constantes.KEY_CONDUCTOR, null)

        if (json.isNullOrBlank()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        colaborador = Gson().fromJson(json, Colaborador::class.java)
        binding.tvBienvenida.text = "Bienvenido, ${colaborador.nombre}"
    }

    private fun cargarCatalogos(onComplete: () -> Unit) {

        Ion.with(this)
            .load("${Constantes.URL_API}estado/obtener-todos")
            .asString()
            .setCallback { e1, r1 ->

                if (e1 != null || r1.isNullOrBlank()) {
                    Toast.makeText(this, "Error al cargar estados", Toast.LENGTH_LONG).show()
                    return@setCallback
                }

                val estados = Gson().fromJson(r1, Array<Estado>::class.java)
                mapaEstados.clear()
                estados.forEach { mapaEstados[it.idEstado] = it.estadoMexico }

                Ion.with(this)
                    .load("${Constantes.URL_API}estados-envio/obtener-todos")
                    .asString()
                    .setCallback { e2, r2 ->
                        if (e2 != null || r2.isNullOrBlank()) {
                            Toast.makeText(this, "Error al cargar estatus", Toast.LENGTH_LONG).show()
                            return@setCallback
                        }

                        val estatus = Gson().fromJson(r2, Array<Estatus>::class.java)
                        mapaEstatus.clear()
                        estatus.forEach { mapaEstatus[it.idEstadosEnvio] = it.estadoEnvio }

                        onComplete()
                    }
            }
    }

    private fun cargarEnviosAsignados() {
        Ion.with(this)
            .load("${Constantes.URL_API}envio/envio-conductor/${colaborador.idColaborador}")
            .asString(Charsets.UTF_8)
            .setCallback { e, result ->

                if (e != null || result.isNullOrBlank()) {
                    Toast.makeText(this, "Error al cargar envíos", Toast.LENGTH_LONG).show()
                    return@setCallback
                }

                try {
                    val tipo =
                        object : com.google.gson.reflect.TypeToken<List<Envio>>() {}.type

                    val lista = Gson().fromJson<List<Envio>>(result, tipo)

                    binding.tvListaTitulo.text =
                        "Total de envíos: ${lista.size}"

                    adapter.actualizarLista(lista)

                } catch (_: Exception) {
                    Toast.makeText(this, "Error al procesar envíos", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun mostrarConfirmacionCerrarSesion() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro de cerrar sesión?")
            .setPositiveButton("Sí") { _, _ -> cerrarSesion() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun cerrarSesion() {
        getSharedPreferences("sesion", MODE_PRIVATE)
            .edit()
            .clear()
            .apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onCambiarEstatus(envio: Envio) {
        onVerDetalle(envio)
    }

    override fun onVerDetalle(envio: Envio) {
        val intent = Intent(this, DetalleEnvioActivity::class.java)
        intent.putExtra(Constantes.KEY_ENVIO, Gson().toJson(envio))
        startActivity(intent)
    }
}
