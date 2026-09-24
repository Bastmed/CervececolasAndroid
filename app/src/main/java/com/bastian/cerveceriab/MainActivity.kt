package com.bastian.cerveceriab

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val ivOjoAbierto = findViewById<ImageView>(R.id.ivOjoAbierto)
        val ivOjoCerrado = findViewById<ImageView>(R.id.ivOjoCerrado)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)

        ivOjoCerrado.setOnClickListener {
            etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            etPassword.setSelection(etPassword.text.length)
            ivOjoCerrado.visibility = View.GONE
            ivOjoAbierto.visibility = View.VISIBLE
        }

        ivOjoAbierto.setOnClickListener {
            etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            etPassword.setSelection(etPassword.text.length)
            ivOjoAbierto.visibility = View.GONE
            ivOjoCerrado.visibility = View.VISIBLE
        }

        btnIngresar.setOnClickListener {
            val nombre = etUsuario.text.toString()
            val clave = etPassword.text.toString()

            val usuario = Usuarios.validar(nombre, clave)
            if (usuario != null) {
                Firebase.analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                    param(FirebaseAnalytics.Param.METHOD, usuario.nombre)
                }

                val intent = Intent(this, MenuActivity::class.java)
                intent.putExtra("esAdmin", usuario.esAdmin)
                intent.putExtra("nombreUsuario", usuario.nombre)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
