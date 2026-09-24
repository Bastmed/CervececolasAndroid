package com.bastian.cerveceriab

data class Insumo(
    val nombre: String,
    val cantidad: Double,
    val unidad: String,
    val proveedor: String,
    val fechaCompra: String
) {
    override fun toString(): String {
        return "$nombre: $cantidad $unidad - Proveedor: $proveedor ($fechaCompra)"
    }
}

object Insumos {
    val unidades = listOf("kg", "g", "litros", "ml", "unidades")
    val lista = mutableListOf(
        Insumo("Malta Pilsner", 25.0, "kg", "Agromalt", "05-03-2026"),
        Insumo("Lúpulo Cascade", 2.0, "kg", "HopWorld", "12-04-2026"),
        Insumo("Levadura Ale US-05", 500.0, "g", "Fermentis Chile", "20-04-2026")
    )
}