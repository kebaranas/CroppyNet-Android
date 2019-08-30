package com.kebaranas.croppynet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kebaranas.croppynet.R
import com.kebaranas.croppynet.models.Product
import kotlinx.android.synthetic.main.product_row.view.*

class ProductsAdapter(
    private val products: List<Product>,
    private val onClickProduct: (title: String, photoUrl: String, photoView: View) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product: Product = products[position]
        Glide.with(holder.view).load(product.photoUrl).into(holder.image)
        holder.title.text = product.title
        holder.price.text = product.price.toString()
        holder.image.setOnClickListener {
            onClickProduct.invoke(product.title, product.photoUrl, holder.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: View = itemView
        val image: ImageView = itemView.itemImageView
        val title: TextView = itemView.titleTextView
        val price: TextView = itemView.priceTextView
    }
}