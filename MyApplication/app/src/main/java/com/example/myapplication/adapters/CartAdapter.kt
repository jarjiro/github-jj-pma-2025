package com.example.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.Helper.ChangeNumberItemsListener
import com.example.myapplication.Helper.ManagmentCart
import com.example.myapplication.Helper.PriceFormatter
import com.example.myapplication.databinding.ViewholderCartBinding
import com.example.myapplication.domain.ItemsModel

class CartAdapter(
    private val listItemSelected: ArrayList<ItemsModel>,
    context: Context,
    private val changeNumberItemsListener: ChangeNumberItemsListener? = null
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private val managmentCart = ManagmentCart(context)

    inner class ViewHolder(val binding: ViewholderCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItemSelected[position]

        holder.binding.titleTxt.text = item.title
        
        // Zobrazení varianty
        if (item.selectedVariant.isNotEmpty()) {
            holder.binding.sizeTxt.text = "Provedení: ${item.selectedVariant}"
        } else {
            holder.binding.sizeTxt.text = ""
        }
        
        // 1. Nahoře (totalEachItem v XML) -> Cena za jeden kus
        holder.binding.totalEachItem.text = "${PriceFormatter.format(item.price)} / ks"
        
        // 2. Pod tím (feeEachItem v XML) -> Celková cena za položku (CZK)
        val totalItemPrice = item.numberInCart * item.price
        holder.binding.feeEachItem.text = PriceFormatter.format(totalItemPrice)
        
        holder.binding.numberInCartTxt.setText(item.numberInCart.toString())

        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.picCart)

        // TLAČÍTKO PLUS
        holder.binding.plusBtn.setOnClickListener {
            managmentCart.plusItem(listItemSelected, position, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }

        // TLAČÍTKO MINUS
        holder.binding.minusBtn.setOnClickListener {
            managmentCart.minusItem(listItemSelected, position, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }

        // TLAČÍTKO ODSTRANIT (X)
        holder.binding.removeItemBtn.setOnClickListener {
            managmentCart.removeItem(listItemSelected, position, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}
