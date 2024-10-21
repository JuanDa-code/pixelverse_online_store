package com.pixelverse.onlinestore.Producto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pixelverse.onlinestore.R

class ProductoAdapter(private val productos: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val imagenProducto: ImageView = itemView.findViewById(R.id.imagenProducto)
        val checkboxProducto: CheckBox = itemView.findViewById(R.id.checkboxProducto)
        val tituloProducto: TextView = itemView.findViewById(R.id.tituloProducto)
        val precioProducto: TextView = itemView.findViewById(R.id.precioProducto)
        val eliminarButton: Button = itemView.findViewById(R.id.eliminarButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.imagenProducto.setImageResource(producto.imagenUrl)
        holder.tituloProducto.text = producto.nombre
        holder.precioProducto.text = "$ ${producto.precio}"

        holder.eliminarButton.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return productos.size
    }
}