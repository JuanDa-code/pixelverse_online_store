package com.pixelverse.onlinestore.Carrito

import com.pixelverse.onlinestore.Producto.Producto

class Carrito {

    private val productos: MutableList<Producto> = mutableListOf()

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
    }

    fun eliminarProducto(producto: Producto) {
        productos.remove(producto)
    }

    fun obtenerProductos(): List<Producto> {
        return productos
    }

    fun obtenerTotal(): Double {
        return productos.sumOf { it.precio!!.toDouble() }
    }
}