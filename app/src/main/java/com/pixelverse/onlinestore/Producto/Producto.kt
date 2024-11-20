package com.pixelverse.onlinestore.Producto

import androidx.room.PrimaryKey
import com.pixelverse.onlinestore.R

data class Producto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String? = null,
    val precio: Double? = null,
    val imagenUrl: String? = null,
    val cantidad: Int = 1
)