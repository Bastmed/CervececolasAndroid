package com.bastian.cerveceriab

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
class MenuActivity : AppCompatActivity() {

    private var esAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        val btnCervezas = findViewById<Button>(R.id.btnCervezas)
        val btnInsumos = findViewById<Button>(R.id.btnInsumos)
        val btnLotes = findViewById<Button>(R.id.btnLotes)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        esAdmin = intent.getBooleanExtra("esAdmin", false)
        val nombreUsuario = intent.getStringExtra("nombreUsuario")
        val rol = if (esAdmin) "admin" else "usuario"

        tvBienvenida.text = "Bienvenido, $nombreUsuario ($rol)"

        btnCervezas.setOnClickListener {
            val intent = Intent(this, CervezaActivity::class.java)
            intent.putExtra("esAdmin", esAdmin)
            startActivity(intent)
        }

        btnInsumos.setOnClickListener {
            val intent = Intent(this, InsumoActivity::class.java)
            intent.putExtra("esAdmin", esAdmin)
            startActivity(intent)
        }

        btnLotes.setOnClickListener {
            val intent = Intent(this, LoteActivity::class.java)
            intent.putExtra("esAdmin", esAdmin)
            startActivity(intent)
        }

        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}