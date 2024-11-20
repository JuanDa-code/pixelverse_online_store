package com.pixelverse.onlinestore

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
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
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
            finish()
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

        val totalProductos = calcularTotalProductos(productos)

        val totalTextView = findViewById<TextView>(R.id.textView8)

        totalTextView.text = "$ ${String.format("%.2f", totalProductos)}"
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
            "SELECT P.${DbHelper.COLUMN_ID_PRODUCTO}, P.${DbHelper.COLUMN_NOMBRE_PRODUCTO}, P.${DbHelper.COLUMN_PRECIO}, P.${DbHelper.COLUMN_IMAGEN_URL}, SUM(C.${DbHelper.COLUMN_CANTIDAD}) AS cantidad " +
                    "FROM ${DbHelper.TABLE_PRODUCTOS} P " +
                    "INNER JOIN ${DbHelper.TABLE_CARRITO} C ON P.${DbHelper.COLUMN_ID_PRODUCTO} = C.${DbHelper.COLUMN_ID_PRODUCTO} " +
                    "WHERE C.${DbHelper.COLUMN_ID_USUARIO} = ? " +
                    "GROUP BY P.${DbHelper.COLUMN_ID_PRODUCTO}",
            arrayOf(idUsuario)
        )
        val carrito = mutableListOf<Producto>()
        while (cursor.moveToNext()) {
            val idProducto = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ID_PRODUCTO))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_NOMBRE_PRODUCTO))
            val precio = cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_PRECIO))
            val imagenUrl = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_IMAGEN_URL))
            val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"))
            val producto = Producto(idProducto, nombre, precio, imagenUrl, cantidad)
            carrito.add(producto)
        }
        cursor.close()
        return carrito
    }

    fun eliminarProductoDelCarrito(idProducto: Int, idUsuario: Int) {
        val db = dbHelper.writableDatabase

        val cursor = db.query(
            DbHelper.TABLE_CARRITO,
            arrayOf(DbHelper.COLUMN_ID_CARRITO),
            "${DbHelper.COLUMN_ID_PRODUCTO} = ? AND ${DbHelper.COLUMN_ID_USUARIO} = ?",
            arrayOf(idProducto.toString(), idUsuario.toString()),
            null,
            null,
            null,
            "1"
        )

        if (cursor.moveToFirst()) {
            val idCarrito = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ID_CARRITO))

            db.delete(
                DbHelper.TABLE_CARRITO,
                "${DbHelper.COLUMN_ID_CARRITO} = ?",
                arrayOf(idCarrito.toString())
            )
        }

        cursor.close()
    }

    private fun calcularTotalProductos(productos: List<Producto>): Double {
        var total = 0.0
        for (producto in productos) {
            total += producto.precio ?: 0.0 * producto.cantidad
        }
        return total
    }

    companion object {
        const val CLASS_NAME = "com.pixelverse.onlinestore.ShoppingCartActivity"
    }
}