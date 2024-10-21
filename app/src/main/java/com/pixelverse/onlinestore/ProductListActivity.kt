package com.pixelverse.onlinestore

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pixelverse.onlinestore.Producto.Producto
import com.pixelverse.onlinestore.Producto.ProductoAdapter

class ProductListActivity : AppCompatActivity() {

    private lateinit var productosRecyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_list)

        productosRecyclerView = findViewById(R.id.productosRecyclerView)
        val productos: List<Producto> = listOf(
            Producto(1, "Audifonos Inalámbricos Mac", 100000, R.drawable.audifonos),
            Producto(2, "Dron", 1500000, R.drawable.dron),
            Producto(3, "Celular", 5000000, R.drawable.celular),
            Producto(4, "Audifonos", 300000, R.drawable.audifonos2),
        )
        adapter = ProductoAdapter(productos, ProductoAdapter.ACCION_AGREGAR)
        productosRecyclerView.adapter = adapter
        productosRecyclerView.layoutManager = LinearLayoutManager(this)

        val cartButton = findViewById<ImageButton>(R.id.ic_cart)

        cartButton.setOnClickListener{
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("NEXT_ACTIVITY", ShoppingCartActivity.CLASS_NAME)
            startActivity(intent)
        }
    }

    companion object {
        const val CLASS_NAME = "com.pixelverse.onlinestore.ProductListActivity"
    }
}