package com.pixelverse.onlinestore

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pixelverse.onlinestore.Producto.Producto
import com.pixelverse.onlinestore.Producto.ProductoAdapter
import com.pixelverse.onlinestore.db.DbHelper

class ProductListActivity : AppCompatActivity() {

    private lateinit var productosRecyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_product_list)

        dbHelper = DbHelper(this)

        productosRecyclerView = findViewById(R.id.productosRecyclerView)
        val productos: List<Producto> = obtenerProductosDeLaBaseDeDatos()
        adapter = ProductoAdapter(productos, ProductoAdapter.ACCION_AGREGAR)
        productosRecyclerView.adapter = adapter
        productosRecyclerView.layoutManager = LinearLayoutManager(this)

        val cartButton = findViewById<ImageButton>(R.id.ic_cart)
        val agregarProductoButton = findViewById<Button>(R.id.agregarProductoButton)

        cartButton.setOnClickListener{
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("NEXT_ACTIVITY", ShoppingCartActivity.CLASS_NAME)
            startActivity(intent)
        }

        agregarProductoButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_agregar_producto, null)
            val nombreEditText = view.findViewById<EditText>(R.id.nombreProductoEditText)
            val precioEditText = view.findViewById<EditText>(R.id.precioProductoEditText)
            val imagenUrlEditText = view.findViewById<EditText>(R.id.imagenUrlProductoEditText)

            builder.setView(view)
            builder.setPositiveButton("Agregar") { dialog, _ ->
                val nombre = nombreEditText.text.toString()
                val precio = precioEditText.text.toString().toDoubleOrNull() ?: 0.0
                val imagenUrl = imagenUrlEditText.text.toString()

                if (nombre.isBlank() || precio <= 0.0 || imagenUrl.isBlank()) {
                    Toast.makeText(this, "Por favor ingresa datos válidos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                agregarProducto(nombre, precio, imagenUrl)

                val productos = obtenerProductosDeLaBaseDeDatos()
                adapter = ProductoAdapter(productos, ProductoAdapter.ACCION_AGREGAR)
                productosRecyclerView.adapter = adapter
            }
            builder.setNegativeButton("Cancelar", null)
            builder.show()
        }
    }

    private fun obtenerProductosDeLaBaseDeDatos(): List<Producto> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DbHelper.TABLE_PRODUCTOS}", null)

        val productos = mutableListOf<Producto>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ID_PRODUCTO))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_NOMBRE_PRODUCTO))
            val precio = cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_PRECIO))
            val imagenUrl = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_IMAGEN_URL))

            productos.add(Producto(id, nombre, precio, imagenUrl))
        }
        cursor.close()

        return productos
    }

    private fun agregarProducto(nombre: String, precio: Double, imagenUrl: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DbHelper.COLUMN_NOMBRE_PRODUCTO, nombre)
            put(DbHelper.COLUMN_PRECIO, precio)
            put(DbHelper.COLUMN_IMAGEN_URL, imagenUrl)
        }
        val newRowId = db.insert(DbHelper.TABLE_PRODUCTOS, null, values)
        if (newRowId == -1L) {
            Toast.makeText(this, "Error al agregar producto", Toast.LENGTH_SHORT).show()
            Log.e("ProductListActivity", "Error al agregar producto")
        } else {
            Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
        }
        return newRowId
    }

    companion object {
        const val CLASS_NAME = "com.pixelverse.onlinestore.ProductListActivity"
    }
}