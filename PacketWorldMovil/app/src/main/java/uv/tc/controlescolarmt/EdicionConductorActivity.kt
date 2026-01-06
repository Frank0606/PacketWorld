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
import uv.tc.controlescolarmt.databinding.ActivityEdicionConductorBinding
import uv.tc.controlescolarmt.dto.Respuesta
import uv.tc.controlescolarmt.poko.Colaborador
import uv.tc.controlescolarmt.util.Constantes
import java.io.ByteArrayOutputStream

class EdicionConductorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEdicionConductorBinding
    private lateinit var colaborador: Colaborador
    private var fotoBase64: String? = null

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageUri = data?.data
            if (imageUri != null) {
                val inputStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.ivFotoConductor.setImageBitmap(bitmap)
                fotoBase64 = encodeImage(bitmap)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEdicionConductorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarDatosConductor()

        binding.ibActualizarFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            selectImageLauncher.launch(intent)
        }

        binding.btnActualizar.setOnClickListener {
            editarDatosConductor()
        }
    }

    private fun cargarDatosConductor() {
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val conductorJson = sharedPref.getString(Constantes.KEY_CONDUCTOR, null)

        if (conductorJson != null) {
            colaborador = Gson().fromJson(conductorJson, Colaborador::class.java)
            
            // Mostrar datos
            binding.tvNumeroPersonal.text = "Número Personal: ${colaborador.numeroPersonal}"
            binding.tvRol.text = "Rol: ${colaborador.nombreRol}"
            binding.tvSucursal.text = "Sucursal: ${colaborador.nombreSucursal}"
            
            binding.etNombre.setText(colaborador.nombre)
            binding.etApellidoPaterno.setText(colaborador.apellidoPaterno)
            binding.etApellidoMaterno.setText(colaborador.apellidoMaterno)
            binding.etCurp.setText(colaborador.curp)
            binding.etCorreo.setText(colaborador.correo)
            binding.etLicencia.setText(colaborador.numeroLicencia)
            binding.etPassword.setText(colaborador.contrasena)

            // Cargar foto si existe
            if (!colaborador.foto.isNullOrEmpty()) {
                val imageBytes = Base64.decode(colaborador.foto, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                binding.ivFotoConductor.setImageBitmap(decodedImage)
                fotoBase64 = colaborador.foto
            }
        } else {
            Toast.makeText(this, "Error al cargar datos del colaborador.", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun editarDatosConductor() {
        colaborador.nombre = binding.etNombre.text.toString().trim()
        colaborador.apellidoPaterno = binding.etApellidoPaterno.text.toString().trim()
        colaborador.apellidoMaterno = binding.etApellidoMaterno.text.toString().trim()
        colaborador.curp = binding.etCurp.text.toString().trim()
        colaborador.correo = binding.etCorreo.text.toString().trim()
        colaborador.numeroLicencia = binding.etLicencia.text.toString().trim()
        colaborador.contrasena = binding.etPassword.text.toString().trim()
        colaborador.foto = fotoBase64

        if (colaborador.nombre.isEmpty() || colaborador.apellidoPaterno.isEmpty() ||
            colaborador.curp.isEmpty() || colaborador.correo.isEmpty() || colaborador.contrasena.isEmpty()) {
            Toast.makeText(this, "Los campos marcados son obligatorios.", Toast.LENGTH_LONG).show()
            return
        }

        val conductorJson = Gson().toJson(colaborador)

        Ion.with(this)
            .load("PUT", "${Constantes.URL_API}${Constantes.ENDPOINT_EDITAR_CONDUCTOR}")
            .setHeader("Content-Type", "application/json")
            .setStringBody(conductorJson)
            .asString()
            .setCallback { e, result ->
                if (e != null) {
                    Toast.makeText(this, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                    return@setCallback
                }

                try {
                    val respuesta = Gson().fromJson(result, Respuesta::class.java)
                    println("Respuesta del servidor: $result")
                    if (!respuesta.error) {
                        guardarConductorActualizado(colaborador)
                        Toast.makeText(this, "Perfil actualizado correctamente.", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this, respuesta.mensaje, Toast.LENGTH_LONG).show()
                    }
                } catch (ex: Exception) {
                    Toast.makeText(this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun guardarConductorActualizado(colaboradorActualizado: Colaborador) {
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(Constantes.KEY_CONDUCTOR, Gson().toJson(colaboradorActualizado))
            apply()
        }
    }

    private fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}
