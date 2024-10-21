package com.pixelverse.onlinestore

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pixelverse.onlinestore.Producto.Producto
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
            finish()
        }

        productosRecyclerView = findViewById(R.id.productosRecyclerView)
        val productos: List<Producto> = listOf(
            Producto(1, "Audifonos Inalámbricos Mac", 100000, R.drawable.audifonos),
            Producto(2, "Producto 2", 150000, R.drawable.dron),
        )
        adapter = ProductoAdapter(productos, ProductoAdapter.ACCION_ELIMINAR)
        productosRecyclerView.adapter = adapter
        productosRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    companion object {
        const val CLASS_NAME = "com.pixelverse.onlinestore.ShoppingCartActivity"
    }
}