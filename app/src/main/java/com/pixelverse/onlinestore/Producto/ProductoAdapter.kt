package com.pixelverse.onlinestore.Producto

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pixelverse.onlinestore.R

class ProductoAdapter( private val productos: MutableList<Producto>, private val tipoAccion: String, private val idUsuario: Int, private val onAgregarProducto: (Producto, Int) -> Unit, private val onEliminarProducto: (Producto) -> Unit) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    fun actualizarProductos(nuevosProductos: List<Producto>) {
        productos.clear()
        productos.addAll(nuevosProductos)
        notifyDataSetChanged()
    }

    inner class ProductoViewHolder private constructor(itemView: View, tipoAccion: String) : RecyclerView.ViewHolder(itemView) {

        val imagenProducto: ImageView = itemView.findViewById(R.id.imagenProducto)
        val tituloProducto: TextView = itemView.findViewById(R.id.tituloProducto)
        val precioProducto: TextView = itemView.findViewById(R.id.precioProducto)
        val eliminarButton: Button = itemView.findViewById(R.id.eliminarButton)

        constructor(itemView: View) : this(itemView, ACCION_AGREGAR) {  }

        constructor(itemView: View, cantidadProductoTextView: TextView, totalProductoTextView: TextView) : this(itemView, ACCION_ELIMINAR) {
            this.cantidadProductoTextView = cantidadProductoTextView
            this.totalProductoTextView = totalProductoTextView
        }

        lateinit var cantidadProductoTextView: TextView
        lateinit var totalProductoTextView: TextView
    }

    companion object {
        const val ACCION_AGREGAR = "agregar"
        const val ACCION_ELIMINAR = "eliminar"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        return when (tipoAccion) {
            ACCION_AGREGAR -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_producto, parent, false)
                ProductoViewHolder(itemView)
            }
            ACCION_ELIMINAR -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_producto_carrito, parent, false)
                val cantidadProductoTextView: TextView = itemView.findViewById(R.id.cantidadProductoTextView)
                val totalProductoTextView: TextView = itemView.findViewById(R.id.totalProductoTextView)
                ProductoViewHolder(itemView, cantidadProductoTextView, totalProductoTextView)
            }
            else -> throw IllegalArgumentException("Tipo de acción inválido")
        }
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        Glide.with(holder.itemView.context)
            .load(if (producto.imagenUrl != null) Uri.parse(producto.imagenUrl) else R.drawable.placeholder)
            .override(200, 200)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .error(R.drawable.error)
            .into(holder.imagenProducto)

        holder.tituloProducto.text = producto.nombre
        holder.precioProducto.text = "$ ${producto.precio}"
        when (tipoAccion) {
            ACCION_AGREGAR -> {
                holder.eliminarButton.text = "Agregar"
                holder.eliminarButton.setOnClickListener {
                    onAgregarProducto(producto, this.idUsuario)
                    Toast.makeText(
                        holder.itemView.context,
                        "Producto agregado al carrito",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            ACCION_ELIMINAR -> {
                holder.cantidadProductoTextView.text = "Cantidad: ${producto.cantidad}"
                val totalProducto = producto.precio!! * producto.cantidad
                holder.totalProductoTextView.text = "Total: $ ${String.format("%.2f", totalProducto)}"
                holder.eliminarButton.text = "Eliminar"
                holder.eliminarButton.setOnClickListener {
                    onEliminarProducto(producto)
                    Toast.makeText(
                        holder.itemView.context,
                        "Producto eliminado del carrito",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return productos.size
    }
}