package uv.tc.controlescolarmt

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.InputType
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
    private lateinit var colaboradorOriginal: Colaborador
    private var fotoUri: Uri? = null
    private var passwordVisible = false

    private val REGEX_SOLO_LETRAS =
        Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")

    private val REGEX_LICENCIA =
        Regex("^[A-Z0-9]{9}$")

    private val REGEX_PASSWORD =
        Regex("^[A-Za-z0-9@#.,]{8,}$")

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let {
                    fotoUri = it
                    binding.ivFotoConductor.setImageURI(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEdicionConductorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Ion.getDefault(this).conscryptMiddleware.enable(false)

        cargarDatosConductor()
        cargarFotoConductor()
        configurarCampoLicencia()

        binding.ivTogglePassword.setOnClickListener {
            passwordVisible = !passwordVisible
            binding.etPassword.inputType =
                if (passwordVisible)
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            binding.etPassword.setSelection(binding.etPassword.text.length)
        }

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

    private fun configurarCampoLicencia() {
        binding.etLicencia.filters = arrayOf(InputFilter.LengthFilter(9))

        binding.etLicencia.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val texto = it.toString().uppercase()
                    if (it.toString() != texto) {
                        binding.etLicencia.setText(texto)
                        binding.etLicencia.setSelection(texto.length)
                        return
                    }

                    if (texto.length in listOf(4, 6)) {
                        binding.etLicencia.error =
                            "La licencia debe tener 9 caracteres (${texto.length}/9)"
                    } else if (texto.length == 9) {
                        binding.etLicencia.error = null
                    }
                }
            }
        })
    }

    private fun cargarDatosConductor() {
        val json = getSharedPreferences("app_prefs", MODE_PRIVATE)
            .getString(Constantes.KEY_CONDUCTOR, null) ?: run {
            Toast.makeText(this, "Error al cargar datos.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        colaboradorOriginal = Gson().fromJson(json, Colaborador::class.java)

        binding.tvNumeroPersonal.text = "Número Personal: ${colaboradorOriginal.numeroPersonal}"
        binding.tvRol.text = "Rol: ${colaboradorOriginal.nombreRol}"
        binding.tvSucursal.text = "Sucursal: ${colaboradorOriginal.nombreSucursal}"
        binding.tvCorreo.text = "Correo: ${colaboradorOriginal.correo}"
        binding.tvCurp.text = "CURP: ${colaboradorOriginal.curp}"

        binding.etNombre.setText(colaboradorOriginal.nombre)
        binding.etApellidoPaterno.setText(colaboradorOriginal.apellidoPaterno)
        binding.etApellidoMaterno.setText(colaboradorOriginal.apellidoMaterno)
        binding.etLicencia.setText(colaboradorOriginal.numeroLicencia)
        binding.etPassword.setText(colaboradorOriginal.contrasena)
    }

    private fun cargarFotoConductor() {
        Ion.with(this)
            .load("${Constantes.URL_API}/colaborador/obtener-foto/${colaboradorOriginal.idColaborador}")
            .asString()
            .setCallback { _, result ->
                try {
                    val col = Gson().fromJson(result, Colaborador::class.java)
                    col.fotoBase64?.let {
                        val bytes = Base64.decode(it, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        binding.ivFotoConductor.setImageBitmap(bitmap)
                    }
                } catch (_: Exception) {}
            }
    }

    private fun editarDatosConductor() {
        if (!validarFormulario()) return

        colaboradorOriginal.apply {
            nombre = binding.etNombre.text.toString().trim()
            apellidoPaterno = binding.etApellidoPaterno.text.toString().trim()
            apellidoMaterno = binding.etApellidoMaterno.text.toString().trim()
            numeroLicencia = binding.etLicencia.text.toString().trim().uppercase()
            contrasena = binding.etPassword.text.toString().trim()
        }

        actualizarDatosConductor()
    }

    private fun validarFormulario(): Boolean {

        val nombre = binding.etNombre.text.toString().trim()
        val paterno = binding.etApellidoPaterno.text.toString().trim()
        val materno = binding.etApellidoMaterno.text.toString().trim()
        val licencia = binding.etLicencia.text.toString().trim().uppercase()
        val password = binding.etPassword.text.toString().trim()

        if (nombre.isEmpty() || paterno.isEmpty() || materno.isEmpty()
            || licencia.isEmpty() || password.isEmpty()
        ) {
            Toast.makeText(
                this,
                "No se permite dejar campos vacíos.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (!REGEX_SOLO_LETRAS.matches(nombre)) {
            Toast.makeText(
                this,
                "El nombre solo debe contener letras.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (!REGEX_SOLO_LETRAS.matches(paterno)) {
            Toast.makeText(
                this,
                "El apellido paterno solo debe contener letras.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (!REGEX_SOLO_LETRAS.matches(materno)) {
            Toast.makeText(
                this,
                "El apellido materno solo debe contener letras.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (!REGEX_LICENCIA.matches(licencia)) {
            Toast.makeText(
                this,
                "La licencia debe tener 9 caracteres y solo puede contener letras y números.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (!REGEX_PASSWORD.matches(password)) {
            Toast.makeText(
                this,
                "La contraseña solo puede contener letras, números y los caracteres @ # . ,",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }


    private fun actualizarDatosConductor() {
        val json = Gson().toJson(colaboradorOriginal)

        Ion.with(this)
            .load("PUT", "${Constantes.URL_API}${Constantes.ENDPOINT_EDITAR_CONDUCTOR}")
            .setHeader("Content-Type", "application/json; charset=UTF-8")
            .setStringBody(json)
            .asString()
            .setCallback { e, result ->
                if (e != null) {
                    Toast.makeText(this, "Error al actualizar.", Toast.LENGTH_LONG).show()
                    return@setCallback
                }

                val respuesta = Gson().fromJson(result, Respuesta::class.java)
                if (respuesta.error) {
                    Toast.makeText(this, respuesta.mensaje, Toast.LENGTH_LONG).show()
                    return@setCallback
                }

                guardarConductorActualizado(colaboradorOriginal)

                fotoUri?.let { actualizarFotoConductor(it) }
                    ?: run {
                        Toast.makeText(this, "Perfil actualizado correctamente.", Toast.LENGTH_LONG).show()
                        finish()
                    }
            }
    }

    private fun actualizarFotoConductor(uri: Uri) {
        val bytes = uriToByteArray(uri) ?: return

        Ion.with(this)
            .load(
                "PUT",
                "${Constantes.URL_API}/colaborador/actualizar-foto/${colaboradorOriginal.idColaborador}"
            )
            .setHeader("Content-Type", "application/octet-stream")
            .setByteArrayBody(bytes)
            .asString()
            .setCallback { e, result ->

                if (e != null) {
                    e.printStackTrace()
                    Toast.makeText(
                        this,
                        "No se pudo actualizar la foto. Verifica tu conexión.",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setCallback
                }

                if (result.isNullOrEmpty()) {
                    Toast.makeText(
                        this,
                        "Respuesta inválida del servidor.",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setCallback
                }

                try {
                    val respuesta = Gson().fromJson(result, Respuesta::class.java)

                    if (respuesta.error) {
                        Toast.makeText(
                            this,
                            respuesta.mensaje ?: "Error al actualizar la foto.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@setCallback
                    }

                    Toast.makeText(
                        this,
                        "Perfil actualizado correctamente.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()

                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Toast.makeText(
                        this,
                        "Error inesperado al procesar la respuesta.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun uriToByteArray(uri: Uri): ByteArray? {
        return try {
            contentResolver.openInputStream(uri)?.use {
                val bitmap = BitmapFactory.decodeStream(it)
                val resized = Bitmap.createScaledBitmap(bitmap, 300, 300, true)
                val baos = ByteArrayOutputStream()
                resized.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                baos.toByteArray()
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun guardarConductorActualizado(colaborador: Colaborador) {
        getSharedPreferences("app_prefs", MODE_PRIVATE)
            .edit()
            .putString(Constantes.KEY_CONDUCTOR, Gson().toJson(colaborador))
            .apply()
    }
}
