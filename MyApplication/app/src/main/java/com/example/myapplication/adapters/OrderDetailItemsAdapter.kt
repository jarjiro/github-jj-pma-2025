package com.example.myapplication.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Helper.PriceFormatter
import com.example.myapplication.activities.DetailActivity
import com.example.myapplication.databinding.ViewholderOrderProductItemBinding
import com.example.myapplication.domain.ItemsModel

class OrderDetailItemsAdapter(private val items: ArrayList<ItemsModel>) :
    RecyclerView.Adapter<OrderDetailItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderOrderProductItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.itemTitle.text = item.title
        holder.binding.itemQty.text = "${item.numberInCart}x"
        
        // OPRAVENO: Použití PriceFormatter pro jednotný formát i ve viewholderu
        holder.binding.itemPrice.text = PriceFormatter.format(item.price)
        
        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .into(holder.binding.itemPic)

        // PROKLIK NA DETAIL PRODUKTU
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("object", item)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val binding: ViewholderOrderProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
