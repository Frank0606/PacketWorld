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

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            .setHeader("Content-Type", "application/x-www-form-urlencoded")
            .setBodyParameter("numeroPersonal", numeroPersonal)
            .setBodyParameter("contrasena", contrasena)
            .asString()
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

                    if (respuesta.colaborador != null && respuesta.colaborador.idColaborador == 3) {

                        guardarConductor(respuesta.colaborador)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(
                            this,
                            "El colaborador no es un conductor.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

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
