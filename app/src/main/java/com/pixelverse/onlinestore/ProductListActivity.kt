package com.pixelverse.onlinestore

import android.content.ContentValues
import android.content.Context
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
import com.pixelverse.onlinestore.Carrito.Carrito
import com.pixelverse.onlinestore.Carrito.CarritoGlobal
import com.pixelverse.onlinestore.Producto.Producto
import com.pixelverse.onlinestore.Producto.ProductoAdapter
import com.pixelverse.onlinestore.db.DbHelper

class ProductListActivity : AppCompatActivity() {

    private lateinit var productosRecyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private lateinit var dbHelper: DbHelper
    private val carrito = Carrito()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_product_list)

        dbHelper = DbHelper(this)

        productosRecyclerView = findViewById(R.id.productosRecyclerView)
        val productos: MutableList<Producto> = obtenerProductosDeLaBaseDeDatos().toMutableList()
        adapter = ProductoAdapter(productos, ProductoAdapter.ACCION_AGREGAR, {})
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

                val productos = obtenerProductosDeLaBaseDeDatos().toMutableList()
                adapter = ProductoAdapter(productos, ProductoAdapter.ACCION_AGREGAR, {})
                productosRecyclerView.adapter = adapter
            }
            builder.setNegativeButton("Cancelar", null)
            builder.show()
        }

        val sharedPref = getSharedPreferences("mis_preferencias", Context.MODE_PRIVATE)
        val idUsuarioString = sharedPref.getString("id_usuario", "-1")
        val idUsuario = idUsuarioString?.toIntOrNull() ?: -1

        guardarCarritoEnLaBaseDeDatos(carrito, idUsuario)
    }

    private fun guardarCarritoEnLaBaseDeDatos(carrito: Carrito, idUsuario: Int) {
        val db = dbHelper.writableDatabase
        for (producto in carrito.obtenerProductos()) {
            val values = ContentValues().apply {
                put("id_usuario", idUsuario)
                put("id_producto", producto.id)
                put("cantidad", 1)
            }
            db.insert("carrito", null, values)
        }
    }

    private fun obtenerCarritoDeLaBaseDeDatos(idUsuario: String): Carrito {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM carrito WHERE id_usuario = ?", arrayOf(idUsuario))
        val carrito = Carrito()
        while (cursor.moveToNext()) {
            val idProducto = cursor.getInt(cursor.getColumnIndexOrThrow("id_producto"))
            // ... (obtener la información del producto desde la base de datos o una API)
            val producto = obtenerProductoPorId(idProducto)
            if (producto != null) {
                CarritoGlobal.carrito.agregarProducto(producto)
            }
        }
        cursor.close()
        return carrito
    }

    private fun obtenerProductoPorId(idProducto: Int): Producto? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DbHelper.TABLE_PRODUCTOS} WHERE ${DbHelper.COLUMN_ID_PRODUCTO} = ?", arrayOf(idProducto.toString()))
        var producto: Producto? = null
        if (cursor.moveToFirst()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_NOMBRE_PRODUCTO))
            val precio = cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_PRECIO))
            val imagenUrl = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_IMAGEN_URL))
            producto = Producto(idProducto, nombre, precio)
        }
        cursor.close()
        return producto
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