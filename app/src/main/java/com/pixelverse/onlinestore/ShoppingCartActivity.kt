package com.pixelverse.onlinestore

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pixelverse.onlinestore.Carrito.CarritoGlobal
import com.pixelverse.onlinestore.Producto.ProductoAdapter

@SuppressLint("WrongViewCast")
class ShoppingCartActivity : AppCompatActivity() {

    private lateinit var productosRecyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shopping_cart)

        val backButton = findViewById<ImageButton>(R.id.ic_arrow_back)

        backButton.setOnClickListener {
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("NEXT_ACTIVITY", ProductListActivity.CLASS_NAME)
            startActivity(intent)
        }

        productosRecyclerView = findViewById(R.id.productosRecyclerView)
        val productos = CarritoGlobal.carrito.obtenerProductos()

        adapter = ProductoAdapter(productos.toMutableList(), ProductoAdapter.ACCION_ELIMINAR) { productoEliminado ->
            CarritoGlobal.carrito.eliminarProducto(productoEliminado)
            recargarVistaCarrito()
        }

        productosRecyclerView.adapter = adapter
        productosRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun recargarVistaCarrito() {
        val productosActualizados = CarritoGlobal.carrito.obtenerProductos()
        adapter.actualizarProductos(productosActualizados)
    }

    companion object {
        const val CLASS_NAME = "com.pixelverse.onlinestore.ShoppingCartActivity"
    }
}