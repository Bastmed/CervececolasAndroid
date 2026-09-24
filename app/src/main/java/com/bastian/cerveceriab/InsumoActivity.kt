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

class InsumoActivity : AppCompatActivity() {

    private var posicionSeleccionada = -1
    private lateinit var adapter: ArrayAdapter<Insumo>
    private lateinit var adapterUnidades: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insumo)

        val lvInsumos = findViewById<ListView>(R.id.lvInsumos)
        val etNombreInsumo = findViewById<EditText>(R.id.etNombreInsumo)
        val etCantidad = findViewById<EditText>(R.id.etCantidad)
        val spUnidadInsumo = findViewById<Spinner>(R.id.spUnidadInsumo)
        val etProveedor = findViewById<EditText>(R.id.etProveedor)
        val etFechaCompra = findViewById<EditText>(R.id.etFechaCompra)
        val btnAgregarInsumo = findViewById<Button>(R.id.btnAgregarInsumo)
        val btnEditarInsumo = findViewById<Button>(R.id.btnEditarInsumo)
        val btnEliminarInsumo = findViewById<Button>(R.id.btnEliminarInsumo)
        val btnSalirInsumo = findViewById<Button>(R.id.btnSalirInsumo)

        val esAdmin = intent.getBooleanExtra("esAdmin", false)
        if (!esAdmin) {
            etNombreInsumo.visibility = View.GONE
            etCantidad.visibility = View.GONE
            spUnidadInsumo.visibility = View.GONE
            etProveedor.visibility = View.GONE
            etFechaCompra.visibility = View.GONE
            btnAgregarInsumo.visibility = View.GONE
            btnEditarInsumo.visibility = View.GONE
            btnEliminarInsumo.visibility = View.GONE
        }

        adapterUnidades = ArrayAdapter(this, android.R.layout.simple_spinner_item, Insumos.unidades)
        adapterUnidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spUnidadInsumo.adapter = adapterUnidades

        etFechaCompra.setOnClickListener {
            val calendario = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    val fecha = "%02d-%02d-%04d".format(day, month + 1, year)
                    etFechaCompra.setText(fecha)
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Insumos.lista)
        lvInsumos.adapter = adapter

        lvInsumos.setOnItemClickListener { _, _, position, _ ->
            posicionSeleccionada = position
            val insumo = Insumos.lista[position]
            etNombreInsumo.setText(insumo.nombre)
            etCantidad.setText(insumo.cantidad.toString())
            spUnidadInsumo.setSelection(adapterUnidades.getPosition(insumo.unidad))
            etProveedor.setText(insumo.proveedor)
            etFechaCompra.setText(insumo.fechaCompra)
        }

        btnAgregarInsumo.setOnClickListener {
            val nombre = etNombreInsumo.text.toString()
            val cantidad = etCantidad.text.toString().toDoubleOrNull()
            val unidad = spUnidadInsumo.selectedItem as? String
            val proveedor = etProveedor.text.toString()
            val fecha = etFechaCompra.text.toString()

            if (nombre.isNotBlank() && cantidad != null && unidad != null && proveedor.isNotBlank() && fecha.isNotBlank()) {
                Insumos.lista.add(Insumo(nombre, cantidad, unidad, proveedor, fecha))
                adapter.notifyDataSetChanged()

                Firebase.analytics.logEvent("agregar_item") {
                    param("entidad", "insumo")
                    param("nombre_item", nombre)
                }

                etNombreInsumo.setText("")
                etCantidad.setText("")
                etProveedor.setText("")
                etFechaCompra.setText("")
                posicionSeleccionada = -1
            } else {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        btnEditarInsumo.setOnClickListener {
            if (posicionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona un insumo de la lista", Toast.LENGTH_SHORT).show()
            } else {
                val nombre = etNombreInsumo.text.toString()
                val cantidad = etCantidad.text.toString().toDoubleOrNull()
                val unidad = spUnidadInsumo.selectedItem as? String
                val proveedor = etProveedor.text.toString()
                val fecha = etFechaCompra.text.toString()

                if (nombre.isNotBlank() && cantidad != null && unidad != null && proveedor.isNotBlank() && fecha.isNotBlank()) {
                    Insumos.lista[posicionSeleccionada] = Insumo(nombre, cantidad, unidad, proveedor, fecha)
                    adapter.notifyDataSetChanged()
                    etNombreInsumo.setText("")
                    etCantidad.setText("")
                    etProveedor.setText("")
                    etFechaCompra.setText("")
                    posicionSeleccionada = -1
                } else {
                    Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnEliminarInsumo.setOnClickListener {
            if (posicionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona un insumo de la lista", Toast.LENGTH_SHORT).show()
            } else {
                Insumos.lista.removeAt(posicionSeleccionada)
                adapter.notifyDataSetChanged()
                etNombreInsumo.setText("")
                etCantidad.setText("")
                etProveedor.setText("")
                etFechaCompra.setText("")
                posicionSeleccionada = -1
            }
        }

        btnSalirInsumo.setOnClickListener { finish() }
    }
}