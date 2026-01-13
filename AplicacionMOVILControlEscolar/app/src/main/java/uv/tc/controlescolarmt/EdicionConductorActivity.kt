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

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
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
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            selectImageLauncher.launch(intent)
        }

        binding.btnActualizar.setOnClickListener {
            editarDatosConductor()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun cargarDatosConductor() {
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val conductorJson = sharedPref.getString(Constantes.KEY_CONDUCTOR, null)

        if (conductorJson == null) {
            Toast.makeText(this, "Error al cargar datos del colaborador.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        colaborador = Gson().fromJson(conductorJson, Colaborador::class.java)

        binding.tvNumeroPersonal.text = "Número Personal: ${colaborador.numeroPersonal}"
        binding.tvRol.text = "Rol: ${colaborador.nombreRol}"
        binding.tvSucursal.text = "Sucursal: ${colaborador.nombreSucursal}"
        binding.tvCorreo.text = "Correo: ${colaborador.correo}"
        binding.tvCurp.text = "CURP: ${colaborador.curp}"

        binding.etNombre.setText(colaborador.nombre)
        binding.etApellidoPaterno.setText(colaborador.apellidoPaterno)
        binding.etApellidoMaterno.setText(colaborador.apellidoMaterno)
        binding.etLicencia.setText(colaborador.numeroLicencia)
        binding.etPassword.setText(colaborador.contrasena)

        if (!colaborador.fotografia.isNullOrEmpty()) {
            val imageBytes = Base64.decode(colaborador.fotografia, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            binding.ivFotoConductor.setImageBitmap(decodedImage)
            fotoBase64 = colaborador.fotografia
        }
    }

    private fun editarDatosConductor() {

        val nombre = binding.etNombre.text.toString().trim()
        val apellidoPaterno = binding.etApellidoPaterno.text.toString().trim()
        val apellidoMaterno = binding.etApellidoMaterno.text.toString().trim()
        val licencia = binding.etLicencia.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // ===== VALIDACIONES =====

        if (nombre.length < 2) {
            Toast.makeText(this, "El nombre debe tener al menos 2 caracteres.", Toast.LENGTH_LONG).show()
            return
        }

        val regexNombre = Regex("^[A-ZÁÉÍÓÚÑa-záéíóúñ\\s]{2,}$")

        if (!regexNombre.matches(apellidoPaterno)) {
            Toast.makeText(this, "Apellido paterno inválido.", Toast.LENGTH_LONG).show()
            return
        }

        if (apellidoMaterno.isNotEmpty() && !regexNombre.matches(apellidoMaterno)) {
            Toast.makeText(this, "Apellido materno inválido.", Toast.LENGTH_LONG).show()
            return
        }

        val regexCurp =
            Regex("^[A-Z][AEIOU][A-Z]{2}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])[HM][A-Z]{2}[B-DF-HJ-NP-TV-Z]{3}[A-Z0-9]\\d$")

        if (licencia.length != 9) {
            Toast.makeText(this, "El número de licencia debe tener 9 caracteres.", Toast.LENGTH_LONG).show()
            return
        }

        if (password.length < 8) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres.", Toast.LENGTH_LONG).show()
            return
        }

        // ===== ASIGNACIÓN =====

        colaborador.nombre = nombre
        colaborador.apellidoPaterno = apellidoPaterno
        colaborador.apellidoMaterno = apellidoMaterno
        colaborador.numeroLicencia = licencia
        colaborador.contrasena = password
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
                    Toast.makeText(this, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                    return@setCallback
                }

                try {
                    val respuesta = Gson().fromJson(result, Respuesta::class.java)

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
