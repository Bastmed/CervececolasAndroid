package com.bastian.cerveceriab

import android.app.DatePickerDialog
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
import java.util.Calendar

class LoteActivity : AppCompatActivity() {

    private var posicionSeleccionada = -1
    private lateinit var adapter: ArrayAdapter<Lote>
    private lateinit var adapterCervezas: ArrayAdapter<String>
    private lateinit var adapterEstados: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lote)

        val lvLotes = findViewById<ListView>(R.id.lvLotes)
        val spCervezaLote = findViewById<Spinner>(R.id.spCervezaLote)
        val etCantidadLitros = findViewById<EditText>(R.id.etCantidadLitros)
        val etFechaInicio = findViewById<EditText>(R.id.etFechaInicio)
        val spEstadoLote = findViewById<Spinner>(R.id.spEstadoLote)
        val btnAgregarLote = findViewById<Button>(R.id.btnAgregarLote)
        val btnEditarLote = findViewById<Button>(R.id.btnEditarLote)
        val btnEliminarLote = findViewById<Button>(R.id.btnEliminarLote)
        val btnSalirLote = findViewById<Button>(R.id.btnSalirLote)

        val esAdmin = intent.getBooleanExtra("esAdmin", false)
        if (!esAdmin) {
            spCervezaLote.visibility = View.GONE
            etCantidadLitros.visibility = View.GONE
            etFechaInicio.visibility = View.GONE
            spEstadoLote.visibility = View.GONE
            btnAgregarLote.visibility = View.GONE
            btnEditarLote.visibility = View.GONE
            btnEliminarLote.visibility = View.GONE
        }

        val nombresCervezas = Cervezas.lista.map { it.nombre }
        adapterCervezas = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresCervezas)
        adapterCervezas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCervezaLote.adapter = adapterCervezas

        adapterEstados = ArrayAdapter(this, android.R.layout.simple_spinner_item, Lotes.estados)
        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEstadoLote.adapter = adapterEstados

        etFechaInicio.setOnClickListener {
            val calendario = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    val fecha = "%02d-%02d-%04d".format(day, month + 1, year)
                    etFechaInicio.setText(fecha)
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Lotes.lista)
        lvLotes.adapter = adapter

        lvLotes.setOnItemClickListener { _, _, position, _ ->
            posicionSeleccionada = position
            val lote = Lotes.lista[position]
            spCervezaLote.setSelection(adapterCervezas.getPosition(lote.cervezaAsociada))
            etCantidadLitros.setText(lote.cantidadLitros.toString())
            etFechaInicio.setText(lote.fechaInicio)
            spEstadoLote.setSelection(adapterEstados.getPosition(lote.estado))
        }

        btnAgregarLote.setOnClickListener {
            val cerveza = spCervezaLote.selectedItem as? String
            val cantidad = etCantidadLitros.text.toString().toDoubleOrNull()
            val fecha = etFechaInicio.text.toString()
            val estado = spEstadoLote.selectedItem as? String

            if (cerveza != null && cantidad != null && fecha.isNotBlank() && estado != null) {
                Lotes.lista.add(Lote(cerveza, cantidad, fecha, estado))
                adapter.notifyDataSetChanged()

                Firebase.analytics.logEvent("agregar_item") {
                    param("entidad", "lote")
                    param("nombre_item", cerveza)
                }

                etCantidadLitros.setText("")
                etFechaInicio.setText("")
                posicionSeleccionada = -1
            } else {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        btnEditarLote.setOnClickListener {
            if (posicionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona un lote de la lista", Toast.LENGTH_SHORT).show()
            } else {
                val cerveza = spCervezaLote.selectedItem as? String
                val cantidad = etCantidadLitros.text.toString().toDoubleOrNull()
                val fecha = etFechaInicio.text.toString()
                val estado = spEstadoLote.selectedItem as? String

                if (cerveza != null && cantidad != null && fecha.isNotBlank() && estado != null) {
                    Lotes.lista[posicionSeleccionada] = Lote(cerveza, cantidad, fecha, estado)
                    adapter.notifyDataSetChanged()
                    etCantidadLitros.setText("")
                    etFechaInicio.setText("")
                    posicionSeleccionada = -1
                } else {
                    Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnEliminarLote.setOnClickListener {
            if (posicionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona un lote de la lista", Toast.LENGTH_SHORT).show()
            } else {
                Lotes.lista.removeAt(posicionSeleccionada)
                adapter.notifyDataSetChanged()
                etCantidadLitros.setText("")
                etFechaInicio.setText("")
                posicionSeleccionada = -1
            }
        }

        btnSalirLote.setOnClickListener { finish() }
    }
}