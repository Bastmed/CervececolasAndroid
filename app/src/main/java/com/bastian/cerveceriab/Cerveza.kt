package com.bastian.cerveceriab

data class Cerveza(val nombre: String, val estilo: String, val grado: Double) {
    override fun toString(): String {
        return "$nombre - $estilo (${grado}% alc.)"
    }
}

object Cervezas {
    val estilos = listOf("Golden Ale", "IPA", "Stout", "Lager", "Pilsner", "Porter", "Trigo")
    val lista = mutableListOf(
        Cerveza("Golden Artesanal", "Golden Ale", 4.8),
        Cerveza("Lúpulo Rebelde", "IPA", 6.2),
        Cerveza("Noche de Malta", "Stout", 7.0)
    )
}