package com.bastian.cerveceriab

data class Lote(
    val cervezaAsociada: String,
    val cantidadLitros: Double,
    val fechaInicio: String,
    val estado: String
) {
    override fun toString(): String {
        return "$cervezaAsociada - $cantidadLitros L - Inicio: $fechaInicio - Estado: $estado"
    }
}

object Lotes {
    val estados = listOf("Fermentando", "Embotellado", "Listo")

    val lista = mutableListOf(
        Lote("Golden Artesanal", 50.0, "10-03-2026", "Fermentando"),
        Lote("Lúpulo Rebelde", 30.0, "01-04-2026", "Embotellado")
    )
}