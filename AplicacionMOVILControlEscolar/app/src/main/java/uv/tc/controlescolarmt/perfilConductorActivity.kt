package uv.tc.controlescolarmt

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import uv.tc.controlescolarmt.databinding.ActivityPerfilConductorBinding
import uv.tc.controlescolarmt.dto.Respuesta
import uv.tc.controlescolarmt.poko.Colaborador
import uv.tc.controlescolarmt.util.Constantes
import java.io.ByteArrayOutputStream

class perfilConductorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilConductorBinding
    private lateinit var colaborador: Colaborador
    private var fotoBase64: String? = null

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri = data?.data
                if (imageUri != null) {
                    val inputStream = contentResolver.openInputStream(imageUri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.ivFotoPerfil.setImageBitmap(bitmap)
                    fotoBase64 = encodeImage(bitmap)
                    actualizarFotoServicio()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilConductorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarDatosConductor()

        binding.btnActualizarPerfil.setOnClickListener {
            val intent = Intent(this, EdicionConductorActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegresar.setOnClickListener {
            finish()
        }
    }

    private fun cargarDatosConductor() {
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val conductorJson = sharedPref.getString(Constantes.KEY_CONDUCTOR, null)

        if (conductorJson != null) {
            colaborador = Gson().fromJson(conductorJson, Colaborador::class.java)

            binding.tvNombreValor.text =
                "${colaborador.nombre} ${colaborador.apellidoPaterno} ${colaborador.apellidoMaterno}"
            binding.tvCurpValor.text = colaborador.curp
            binding.tvCorreoValor.text = colaborador.correo
            binding.tvPersonalValor.text = colaborador.numeroPersonal
            binding.tvRolValor.text = colaborador.nombreRol
            binding.tvLicencia.text = colaborador.numeroLicencia

            if (!colaborador.fotografia.isNullOrEmpty()) {
                val imageBytes = Base64.decode(colaborador.fotografia, Base64.DEFAULT)
                val decodedImage =
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                binding.ivFotoPerfil.setImageBitmap(decodedImage)
            }
        } else {
            Toast.makeText(
                this,
                "Error al cargar datos del colaborador.",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    }

    private fun actualizarFotoServicio() {
        colaborador.fotografia = fotoBase64
        val conductorJson = Gson().toJson(colaborador)

        Ion.with(this)
            .load("PUT", "${Constantes.URL_API}${Constantes.ENDPOINT_EDITAR_CONDUCTOR}")
            .setHeader("Content-Type", "application/json; charset=UTF-8")
            .setHeader("Accept", "application/json; charset=UTF-8")
            .setStringBody(conductorJson)
            .asString(Charsets.UTF_8)
            .setCallback { e, result ->
                if (e != null) {
                    Toast.makeText(
                        this,
                        "Error al actualizar foto: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setCallback
                }

                try {
                    val respuesta = Gson().fromJson(result, Respuesta::class.java)
                    if (!respuesta.error) {
                        guardarConductorLocal(colaborador)
                        Toast.makeText(
                            this,
                            "Foto actualizada correctamente.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            respuesta.mensaje,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (ex: Exception) {
                    Toast.makeText(
                        this,
                        "Error al procesar respuesta del servidor.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun guardarConductorLocal(colaboradorActualizado: Colaborador) {
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(
                Constantes.KEY_CONDUCTOR,
                Gson().toJson(colaboradorActualizado)
            )
            apply()
        }
    }

    private fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    override fun onResume() {
        super.onResume()
        cargarDatosConductor()
    }
}
