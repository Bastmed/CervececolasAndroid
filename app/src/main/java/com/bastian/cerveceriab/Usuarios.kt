package com.bastian.cerveceriab

data class Usuario(val nombre: String, val clave: String, val esAdmin: Boolean)

object Usuarios {
    val listaUsuarios = listOf(
        Usuario("admin", "123", esAdmin = true),
        Usuario("usuario", "user123", esAdmin = false)
    )

    fun validar(nombre: String, clave: String): Usuario? {
        return listaUsuarios.find { it.nombre == nombre && it.clave == clave }
    }
}