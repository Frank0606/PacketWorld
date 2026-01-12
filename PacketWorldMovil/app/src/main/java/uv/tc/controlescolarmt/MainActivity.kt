package uv.tc.controlescolarmt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import uv.tc.controlescolarmt.adapters.EnvioAdapter
import uv.tc.controlescolarmt.adapters.EnvioListener
import uv.tc.controlescolarmt.databinding.ActivityMainBinding
import uv.tc.controlescolarmt.dto.RSEnvios
import uv.tc.controlescolarmt.poko.Colaborador
import uv.tc.controlescolarmt.poko.Envio
import uv.tc.controlescolarmt.util.Constantes

class MainActivity : AppCompatActivity(), EnvioListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var colaborador: Colaborador
    private val envios = mutableListOf<Envio>()
    private lateinit var adapter: EnvioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarDatosConductor()
        configurarRecycler()
        cargarEnviosAsignados()

        binding.btnPerfil.setOnClickListener {
            startActivity(Intent(this, perfilConductorActivity::class.java))
        }
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
            .load(
                "${Constantes.URL_API}envio/guia-direccion/${colaborador.idColaborador}"
            )
            .asString()
            .setCallback { e, result ->

                println("Respuesta del servidor: $result")

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
    }
}
