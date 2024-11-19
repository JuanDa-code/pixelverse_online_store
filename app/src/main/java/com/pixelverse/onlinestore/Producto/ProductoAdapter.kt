package com.pixelverse.onlinestore.Producto

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

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val imagenProducto: ImageView = itemView.findViewById(R.id.imagenProducto)
        val checkboxProducto: CheckBox = itemView.findViewById(R.id.checkboxProducto)
        val tituloProducto: TextView = itemView.findViewById(R.id.tituloProducto)
        val precioProducto: TextView = itemView.findViewById(R.id.precioProducto)
        val eliminarButton: Button = itemView.findViewById(R.id.eliminarButton)
    }

    companion object {
        const val ACCION_AGREGAR = "agregar"
        const val ACCION_ELIMINAR = "eliminar"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        Glide.with(holder.itemView.context)
            .load(producto.imagenUrl)
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
                    Toast.makeText(holder.itemView.context, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
                }
            }
            ACCION_ELIMINAR -> {
                holder.eliminarButton.text = "Eliminar"
                holder.eliminarButton.setOnClickListener {
                    onEliminarProducto(producto)
                    Toast.makeText(holder.itemView.context, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return productos.size
    }
}