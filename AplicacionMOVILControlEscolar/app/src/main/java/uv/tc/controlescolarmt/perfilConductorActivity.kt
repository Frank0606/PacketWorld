package uv.tc.controlescolarmt

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import uv.tc.controlescolarmt.databinding.ActivityPerfilConductorBinding
import uv.tc.controlescolarmt.poko.Colaborador
import uv.tc.controlescolarmt.poko.FotoColaboradorDTO
import uv.tc.controlescolarmt.util.Constantes

class PerfilConductorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilConductorBinding
    private lateinit var colaborador: Colaborador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilConductorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Ion.getDefault(this).conscryptMiddleware.enable(false)

        binding.btnActualizarPerfil.setOnClickListener {
            startActivity(Intent(this, EdicionConductorActivity::class.java))
        }

        binding.btnRegresar.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
    }

    override fun onResume() {
        super.onResume()
        cargarDatosConductor()
        cargarFotoConductor()
    }

    private fun cargarDatosConductor() {
        val json = getSharedPreferences("app_prefs", MODE_PRIVATE)
            .getString(Constantes.KEY_CONDUCTOR, null)

        if (json == null) {
            Toast.makeText(this, "Error al cargar colaborador.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        colaborador = Gson().fromJson(json, Colaborador::class.java)

        binding.tvNombreValor.text =
            "${colaborador.nombre} ${colaborador.apellidoPaterno} ${colaborador.apellidoMaterno}"
        binding.tvCurpValor.text = colaborador.curp
        binding.tvCorreoValor.text = colaborador.correo
        binding.tvPersonalValor.text = colaborador.numeroPersonal
        binding.tvRolValor.text = colaborador.nombreRol
        binding.tvLicencia.text = colaborador.numeroLicencia
    }

    private fun cargarFotoConductor() {
        Ion.with(this)
            .load("${Constantes.URL_API}colaborador/obtener-foto/${colaborador.idColaborador}")
            .asString()
            .setCallback { e, result ->

                if (e != null) {
                    e.printStackTrace()
                    return@setCallback
                }

                if (result.isNullOrBlank()) return@setCallback

                val dto = Gson().fromJson(result, FotoColaboradorDTO::class.java)

                if (!dto.fotoBase64.isNullOrEmpty()) {

                    val bytes = Base64.decode(dto.fotoBase64, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                    binding.ivFotoPerfil.setImageBitmap(bitmap)
                }
            }
    }
}
