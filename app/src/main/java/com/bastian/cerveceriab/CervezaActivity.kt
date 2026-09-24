package com.bastian.cerveceriab

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

class CervezaActivity : AppCompatActivity() {

    private var posicionSeleccionada = -1
    private lateinit var adapter: ArrayAdapter<Cerveza>
    private lateinit var adapterEstilos: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cerveza)

        val lvCervezas = findViewById<ListView>(R.id.lvCervezas)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val spEstiloCerveza = findViewById<Spinner>(R.id.spEstiloCerveza)
        val etGrado = findViewById<EditText>(R.id.etGrado)
        val btnAgregarCerveza = findViewById<Button>(R.id.btnAgregarCerveza)
        val btnEditarCerveza = findViewById<Button>(R.id.btnEditarCerveza)
        val btnEliminarCerveza = findViewById<Button>(R.id.btnEliminarCerveza)
        val btnSalirCerveza = findViewById<Button>(R.id.btnSalirCerveza)

        val esAdmin = intent.getBooleanExtra("esAdmin", false)
        if (!esAdmin) {
            etNombre.visibility = View.GONE
            spEstiloCerveza.visibility = View.GONE
            etGrado.visibility = View.GONE
            btnAgregarCerveza.visibility = View.GONE
            btnEditarCerveza.visibility = View.GONE
            btnEliminarCerveza.visibility = View.GONE
        }

        adapterEstilos = ArrayAdapter(this, android.R.layout.simple_spinner_item, Cervezas.estilos)
        adapterEstilos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEstiloCerveza.adapter = adapterEstilos

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Cervezas.lista)
        lvCervezas.adapter = adapter

        lvCervezas.setOnItemClickListener { _, _, position, _ ->
            posicionSeleccionada = position
            val cerveza = Cervezas.lista[position]
            etNombre.setText(cerveza.nombre)
            spEstiloCerveza.setSelection(adapterEstilos.getPosition(cerveza.estilo))
            etGrado.setText(cerveza.grado.toString())
        }

        btnAgregarCerveza.setOnClickListener {
            val nombre = etNombre.text.toString()
            val estilo = spEstiloCerveza.selectedItem as? String
            val grado = etGrado.text.toString().toDoubleOrNull()

            if (nombre.isNotBlank() && estilo != null && grado != null) {
                Cervezas.lista.add(Cerveza(nombre, estilo, grado))
                adapter.notifyDataSetChanged()

                Firebase.analytics.logEvent("agregar_item") {
                    param("entidad", "cerveza")
                    param("nombre_item", nombre)
                }

                etNombre.setText("")
                etGrado.setText("")
                posicionSeleccionada = -1
            } else {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        btnEditarCerveza.setOnClickListener {
            if (posicionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una cerveza de la lista", Toast.LENGTH_SHORT).show()
            } else {
                val nombre = etNombre.text.toString()
                val estilo = spEstiloCerveza.selectedItem as? String
                val grado = etGrado.text.toString().toDoubleOrNull()

                if (nombre.isNotBlank() && estilo != null && grado != null) {
                    Cervezas.lista[posicionSeleccionada] = Cerveza(nombre, estilo, grado)
                    adapter.notifyDataSetChanged()
                    etNombre.setText("")
                    etGrado.setText("")
                    posicionSeleccionada = -1
                } else {
                    Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnEliminarCerveza.setOnClickListener {
            if (posicionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una cerveza de la lista", Toast.LENGTH_SHORT).show()
            } else {
                Cervezas.lista.removeAt(posicionSeleccionada)
                adapter.notifyDataSetChanged()
                etNombre.setText("")
                etGrado.setText("")
                posicionSeleccionada = -1
            }
        }

        btnSalirCerveza.setOnClickListener { finish() }
    }
}