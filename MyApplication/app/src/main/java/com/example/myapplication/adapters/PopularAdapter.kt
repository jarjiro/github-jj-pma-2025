package com.example.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Helper.ManagmentCart
import com.example.myapplication.Helper.ManagmentFavorite
import com.example.myapplication.Helper.PriceFormatter
import com.example.myapplication.R
import com.example.myapplication.activities.DetailActivity
import com.example.myapplication.databinding.ViewholderPopularBinding
import com.example.myapplication.domain.ItemsModel

class PopularAdapter(val items: MutableList<ItemsModel>) :
    RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(val binding: ViewholderPopularBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        val binding =
            ViewholderPopularBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val managmentCart = ManagmentCart(context)
        val managmentFavorite = ManagmentFavorite(context)

        holder.binding.titleTxt.text = item.title
        holder.binding.subtitleTxt.text = item.extra
        
        // OPRAVENO: Použití PriceFormatter pro profesionální vzhled ceny
        holder.binding.priceTxt.text = PriceFormatter.format(item.price)

        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .into(holder.binding.pic)

        if (managmentFavorite.isFavorite(item)) {
            holder.binding.favBtn.setImageResource(R.drawable.ic_heart_full)
            holder.binding.favBtn.setColorFilter(context.getColor(R.color.orange))
        } else {
            holder.binding.favBtn.setImageResource(R.drawable.ic_heart_outline)
            holder.binding.favBtn.setColorFilter(context.getColor(R.color.orange))
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("object", item)
            context.startActivity(intent)
        }

        holder.binding.imageView5.setOnClickListener {
            if (item.categoryId == "0") {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("object", item)
                context.startActivity(intent)
            } else {
                item.numberInCart = 1
                managmentCart.insertItems(item)
            }
        }

        holder.binding.favBtn.setOnClickListener {
            managmentFavorite.insertItem(item)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = items.size
}
