package com.example.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Helper.PriceFormatter
import com.example.myapplication.Helper.TinyDB
import com.example.myapplication.R
import com.example.myapplication.activities.OrderDetailActivity
import com.example.myapplication.databinding.ViewholderOrderBinding
import com.example.myapplication.domain.OrderModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrdersAdapter(private var ordersList: ArrayList<OrderModel>) :
    RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderOrderBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = ordersList[position]

        holder.binding.orderTitleTxt.text = "Objednávka #${order.orderId}"
        // POUŽITÍ NOVÉHO FORMÁTU
        holder.binding.orderTotalTxt.text = PriceFormatter.format(order.totalPrice)
        
        holder.binding.statusTxt.text = order.status
        when (order.status) {
            "Dokončena" -> holder.binding.statusTxt.setBackgroundResource(R.drawable.green_2_semi_corner)
            "Zrušena" -> holder.binding.statusTxt.setBackgroundResource(R.drawable.red_2_semi_corner)
            else -> holder.binding.statusTxt.setBackgroundResource(R.drawable.orange_2_semi_corner)
        }

        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        holder.binding.orderDateTxt.text = sdf.format(Date(order.date))

        holder.binding.viewItemsBtn.setOnClickListener {
            if (holder.binding.itemsRecyclerView.visibility == android.view.View.VISIBLE) {
                holder.binding.itemsRecyclerView.visibility = android.view.View.GONE
            } else {
                holder.binding.itemsRecyclerView.visibility = android.view.View.VISIBLE
                holder.binding.itemsRecyclerView.layoutManager = LinearLayoutManager(context)
                holder.binding.itemsRecyclerView.adapter = OrderDetailItemsAdapter(order.items)
            }
        }

        holder.binding.deleteOrderBtn.setOnClickListener {
            showDeleteConfirm(position)
        }

        holder.binding.viewReceiptBtn.setOnClickListener {
            val intent = Intent(context, OrderDetailActivity::class.java)
            intent.putExtra("order", order)
            context.startActivity(intent)
        }
    }

    private fun showDeleteConfirm(position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Smazat objednávku")
            .setMessage("Opravdu chcete tuto objednávku odstranit?")
            .setPositiveButton("Smazat") { _, _ ->
                ordersList.removeAt(position)
                TinyDB(context).putListOrder("OrdersList", ordersList)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, ordersList.size)
            }
            .setNegativeButton("Zrušit", null)
            .show()
    }

    override fun getItemCount(): Int = ordersList.size
    class ViewHolder(val binding: ViewholderOrderBinding) : RecyclerView.ViewHolder(binding.root)
}
