package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Helper.ManagmentOrders
import com.example.myapplication.Helper.PriceFormatter
import com.example.myapplication.adapters.OrderDetailItemsAdapter
import com.example.myapplication.databinding.ActivityOrderDetailBinding
import com.example.myapplication.domain.OrderModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var order: OrderModel
    private lateinit var managmentOrders: ManagmentOrders

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentOrders = ManagmentOrders(this)
        
        val receivedOrder = intent.getSerializableExtra("order") as? OrderModel
        if (receivedOrder != null) {
            order = receivedOrder
            setupUI()
            setupActions()
        } else {
            Toast.makeText(this, "Chyba při načítání objednávky", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupUI() {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val dateString = dateFormat.format(Date(order.date))

        val receiptText = StringBuilder()
        receiptText.append("OBJEDNÁVKA: #${order.orderId}\n")
        receiptText.append("DATUM: $dateString\n")
        receiptText.append("STAV: ${order.status}\n")
        receiptText.append("------------------------------\n")
        
        for (item in order.items) {
            val variant = if (item.selectedVariant.isEmpty()) "" else " (${item.selectedVariant})"
            receiptText.append("${item.numberInCart}x ${item.title}$variant\n")
            receiptText.append("   ${PriceFormatter.format(item.price * item.numberInCart)}\n")
        }
        
        receiptText.append("------------------------------\n")
        receiptText.append("DOPRAVA: ${PriceFormatter.format(order.deliveryCost)}\n")
        receiptText.append("CELKEM: ${PriceFormatter.format(order.totalPrice)}\n")
        receiptText.append("------------------------------\n")
        receiptText.append("DĚKUJEME ZA NÁKUP!")

        binding.receiptTxt.text = receiptText.toString()

        binding.itemsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.itemsRecyclerView.adapter = OrderDetailItemsAdapter(ArrayList(order.items))

        if (order.status != "Odeslána" && order.status != "Čeká na vyřízení") {
            binding.adminActionsLayout.visibility = View.GONE
        }
        
        binding.shareBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Účtenka - Objednávka #${order.orderId}")
            shareIntent.putExtra(Intent.EXTRA_TEXT, receiptText.toString())
            startActivity(Intent.createChooser(shareIntent, "Sdílet účtenku přes"))
        }
    }

    private fun setupActions() {
        binding.backBtn.setOnClickListener { finish() }

        binding.approveBtn.setOnClickListener {
            updateStatus("Dokončena")
        }

        binding.rejectBtn.setOnClickListener {
            updateStatus("Zrušena")
        }
    }

    private fun updateStatus(newStatus: String) {
        order.status = newStatus
        managmentOrders.updateOrderStatus(order.orderId.toString(), newStatus)
        
        // Změna zprávy v Toastu dle požadavku
        val message = if (newStatus == "Dokončena") "Objednávka byla zaplacena" else "Objednávka byla zrušena"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        
        setupUI()
    }
}
