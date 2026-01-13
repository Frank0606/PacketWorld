package uv.tc.controlescolarmt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import uv.tc.controlescolarmt.databinding.ActivityLoginBinding
import uv.tc.controlescolarmt.dto.RSAutenticacionConductor
import uv.tc.controlescolarmt.util.Constantes
import android.text.Editable
import android.text.TextWatcher


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.etNumeroPersonal.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val textoMayusculas = it.toString().uppercase()
                    if (it.toString() != textoMayusculas) {
                        binding.etNumeroPersonal.setText(textoMayusculas)
                        binding.etNumeroPersonal.setSelection(textoMayusculas.length)
                    }
                }
            }
        })


        binding.btnLogin.setOnClickListener {
            iniciarSesion()
        }
    }

    private fun iniciarSesion() {

        val numeroPersonal = binding.etNumeroPersonal.text.toString().trim()
        val contrasena = binding.etPassword.text.toString().trim()

        if (numeroPersonal.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(
                this,
                "Ingrese número de personal y contraseña",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        Ion.with(this)
            .load("POST", "${Constantes.URL_API}${Constantes.ENDPOINT_LOGIN}")
            .setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
            .setHeader("Accept", "application/json; charset=UTF-8")
            .setBodyParameter("numeroPersonal", numeroPersonal)
            .setBodyParameter("contrasena", contrasena)
            .asString(Charsets.UTF_8)
            .setCallback { e, result ->

                if (e != null) {
                    Toast.makeText(
                        this,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setCallback
                }

                try {
                    val respuesta = Gson().fromJson(
                        result,
                        RSAutenticacionConductor::class.java
                    )

                    if (respuesta.colaborador == null) {
                        Toast.makeText(
                            this,
                            "El usuario y/o la contraseña no son correctos. Favor de verificar.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@setCallback
                    }

                    if (respuesta.colaborador.idColaborador != 3) {
                        Toast.makeText(
                            this,
                            "El usuario no tiene rol de conductor.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@setCallback
                    }

                    guardarConductor(respuesta.colaborador)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } catch (ex: Exception) {
                    Toast.makeText(
                        this,
                        "Error al procesar la respuesta del servidor.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun guardarConductor(colaborador: uv.tc.controlescolarmt.poko.Colaborador) {
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(Constantes.KEY_CONDUCTOR, Gson().toJson(colaborador))
            apply()
        }
    }
}
