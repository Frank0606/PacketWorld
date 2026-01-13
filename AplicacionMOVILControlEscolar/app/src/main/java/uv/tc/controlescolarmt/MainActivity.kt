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
import uv.tc.controlescolarmt.util.Constantes
import kotlin.jvm.java

class MainActivity : AppCompatActivity(), EnvioListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var colaborador: Colaborador
    private val envios = mutableListOf<Envio>()
    private lateinit var adapter: EnvioAdapter
    private lateinit var runnable: Runnable
    private val handler = android.os.Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        runnable = object : Runnable {
            override fun run() {
                refrescar()
                handler.postDelayed(this, 5000)
            }
        }

        cargarDatosConductor()
        configurarRecycler()
        cargarEnviosAsignados()

        // PERFIL (ImageButton)
        binding.btnPerfil.setOnClickListener {
            startActivity(Intent(this, perfilConductorActivity::class.java))
        }

        // CERRAR SESIÓN (ImageButton)
        binding.btnLogout.setOnClickListener {
            mostrarConfirmacionCerrarSesion()
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    private fun refrescar() {
        cargarEnviosAsignados()
    }

    private fun mostrarConfirmacionCerrarSesion() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro de cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                cerrarSesion()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun cerrarSesion() {
        val prefs = getSharedPreferences("sesion", MODE_PRIVATE)
        prefs.edit().clear().apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun cargarDatosConductor() {
        val pref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val json = pref.getString(Constantes.KEY_CONDUCTOR, null)

        if (json == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        colaborador = Gson().fromJson(json, Colaborador::class.java)
        binding.tvBienvenida.text = "Bienvenido, ${colaborador.nombre}"
    }

    private fun configurarRecycler() {
        adapter = EnvioAdapter(envios, this)
        binding.rvEnvios.layoutManager = LinearLayoutManager(this)
        binding.rvEnvios.adapter = adapter
    }

    private fun cargarEnviosAsignados() {
        Ion.with(this)
            .load("${Constantes.URL_API}envio/envio-conductor/${colaborador.idColaborador}")
            .setHeader("Accept", "application/json; charset=UTF-8")
            .asString(Charsets.UTF_8)
            .setCallback { e, result ->

                if (e != null) {
                    Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show()
                    return@setCallback
                }

                try {
                    val tipoListaEnvio =
                        object : com.google.gson.reflect.TypeToken<List<Envio>>() {}.type

                    val enviosAPI: List<Envio> =
                        Gson().fromJson(result, tipoListaEnvio)

                    binding.tvListaTitulo.text = "Total de envíos: ${enviosAPI.size}"
                    adapter.actualizarLista(enviosAPI)

                } catch (ex: Exception) {
                    Toast.makeText(this, "Error al procesar envíos", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onCambiarEstatus(envio: Envio) {
        onVerDetalle(envio)
    }

    override fun onVerDetalle(envio: Envio) {
        val intent = Intent(this, DetalleEnvioActivity::class.java)
        intent.putExtra(Constantes.KEY_ENVIO, Gson().toJson(envio))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        cargarEnviosAsignados()
        handler.post(runnable)
    }
}
