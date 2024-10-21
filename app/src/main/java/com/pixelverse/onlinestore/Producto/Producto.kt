package com.pixelverse.onlinestore.Producto

import com.pixelverse.onlinestore.R

data class Producto(
    val id: Int = 1,
    val nombre: String = "Audifonos Inalámbricos Mac",
    val precio: Int = 100000,
    val imagenUrl: Int = R.drawable.audifonos
)