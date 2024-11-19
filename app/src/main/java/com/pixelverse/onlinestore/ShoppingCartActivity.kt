package com.pixelverse.onlinestore

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.pixelverse.onlinestore.Producto.Producto
import com.pixelverse.onlinestore.Producto.ProductoAdapter
import com.pixelverse.onlinestore.db.DbHelper

@SuppressLint("WrongViewCast")
class ShoppingCartActivity : AppCompatActivity() {

    private lateinit var productosRecyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_shopping_cart)


        dbHelper = DbHelper(this)

        val backButton = findViewById<ImageButton>(R.id.ic_arrow_back)

        backButton.setOnClickListener {
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("NEXT_ACTIVITY", ProductListActivity.CLASS_NAME)
            startActivity(intent)
        }

        productosRecyclerView = findViewById(R.id.productosRecyclerView)

        val sharedPref = getSharedPreferences("mis_preferencias", Context.MODE_PRIVATE)
        val idUsuarioString = sharedPref.getString("id_usuario", "-1")
        val idUsuario = idUsuarioString?.toIntOrNull() ?: -1

        val productos = obtenerCarritoDeLaBaseDeDatos(idUsuarioString ?: "-1").toMutableList()

        adapter = ProductoAdapter(
            productos,
            ProductoAdapter.ACCION_ELIMINAR,
            idUsuario,
            { producto, idUsuario ->

            },
            { producto ->
                eliminarProductoDelCarrito(producto.id, idUsuario)
                recargarVistaCarrito()
            }
        )

        productosRecyclerView.adapter = adapter
        productosRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun recargarVistaCarrito() {
        val sharedPref = getSharedPreferences("mis_preferencias", Context.MODE_PRIVATE)
        val idUsuarioString = sharedPref.getString("id_usuario", "-1")
        val productosActualizados = obtenerCarritoDeLaBaseDeDatos(idUsuarioString ?: "-1")
        adapter.actualizarProductos(productosActualizados)
    }

    private fun obtenerCarritoDeLaBaseDeDatos(idUsuario: String): List<Producto> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT P.${DbHelper.COLUMN_ID_PRODUCTO}, P.${DbHelper.COLUMN_NOMBRE_PRODUCTO}, P.${DbHelper.COLUMN_PRECIO}, P.${DbHelper.COLUMN_IMAGEN_URL} " +
                    "FROM ${DbHelper.TABLE_PRODUCTOS} P " +
                    "INNER JOIN t_carrito C ON P.${DbHelper.COLUMN_ID_PRODUCTO} = C.id_producto " +
                    "WHERE C.id_usuario = ?",
            arrayOf(idUsuario)
        )
        val carrito = mutableListOf<Producto>()
        while (cursor.moveToNext()) {
            val idProducto = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ID_PRODUCTO))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_NOMBRE_PRODUCTO))
            val precio = cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_PRECIO))
            val imagenUrl = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_IMAGEN_URL))
            val producto = Producto(idProducto, nombre, precio, imagenUrl)
            carrito.add(producto)
        }
        cursor.close()
        return carrito
    }

    fun eliminarProductoDelCarrito(idProducto: Int, idUsuario: Int) {
        val db = dbHelper.writableDatabase
        db.delete(DbHelper.TABLE_CARRITO, "${DbHelper.COLUMN_ID_PRODUCTO} = ? AND ${DbHelper.COLUMN_ID_USUARIO} = ?", arrayOf(idProducto.toString(), idUsuario.toString()))
    }

    companion object {
        const val CLASS_NAME = "com.pixelverse.onlinestore.ShoppingCartActivity"
    }
}