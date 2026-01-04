package com.example.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.os.Looper // <-- Přidán chybějící import
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activities.ItemsListActivity
import com.example.myapplication.databinding.ViewholderCategoryBinding
import com.example.myapplication.domain.CategoryModel

class CategoryAdapter(val items: MutableList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    inner class ViewHolder(val binding: ViewholderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleCat.text = item.title

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = holder.adapterPosition // Je bezpečnější použít adapterPosition
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)

            // OPRAVA 1: Handler je odstraněn pro okamžitou reakci
            val intent = Intent(context, ItemsListActivity::class.java).apply {
                // OPRAVA 2: ID se předává v jeho původním typu, není třeba .toString()
                putExtra("id", item.id)
                putExtra("title", item.title)
            }
            // OPRAVA 3: Použití jednodušší metody startActivity
            context.startActivity(intent)
        }

        // OPRAVA 4: Doplněna změna barvy textu
        if (selectedPosition == position) {
            // Styl pro VYBRANOU položku
            holder.binding.titleCat.setBackgroundResource(R.drawable.orange_full_corner_bg)
            holder.binding.titleCat.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            // Styl pro NEVYBRANOU položku
            holder.binding.titleCat.setBackgroundResource(R.drawable.orange_2_full_corner)
            holder.binding.titleCat.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    override fun getItemCount(): Int = items.size
}
