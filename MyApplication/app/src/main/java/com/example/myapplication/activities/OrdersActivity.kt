package com.example.myapplication.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Helper.ManagmentOrders
import com.example.myapplication.Helper.TinyDB
import com.example.myapplication.R
import com.example.myapplication.adapters.OrdersAdapter
import com.example.myapplication.databinding.ActivityOrdersBinding
import com.example.myapplication.domain.OrderModel

class OrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersBinding
    private lateinit var managmentOrders: ManagmentOrders
    private var allOrders = ArrayList<OrderModel>()
    private var filteredOrders = ArrayList<OrderModel>()

    private var currentSort = "newest"
    private var showOdeslana = true
    private var showDokoncena = true
    private var showZrusena = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentOrders = ManagmentOrders(this)
        
        binding.backBtn.setOnClickListener { finish() }
        binding.clearHistoryBtn.setOnClickListener { showClearDialog() }
        binding.filterBtn.setOnClickListener { showFilterDialog() }

        // NASTAVENÍ SWIPE TO REFRESH
        binding.swipeRefresh.setColorSchemeResources(R.color.orange)
        binding.swipeRefresh.setOnRefreshListener {
            loadData()
            // Animaci ukončíme po 1 sekundě
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
            }, 1000)
        }

        loadData()
    }

    private fun loadData() {
        allOrders = managmentOrders.getOrders()
        applyFilters()
    }

    private fun applyFilters() {
        filteredOrders = ArrayList(allOrders.filter {
            (it.status == "Odeslána" && showOdeslana) ||
            (it.status == "Dokončena" && showDokoncena) ||
            (it.status == "Zrušena" && showZrusena)
        })

        when (currentSort) {
            "newest" -> filteredOrders.sortByDescending { it.date }
            "oldest" -> filteredOrders.sortBy { it.date }
            "priceHigh" -> filteredOrders.sortByDescending { it.totalPrice }
            "priceLow" -> filteredOrders.sortBy { it.totalPrice }
        }

        updateUI()
    }

    private fun updateUI() {
        if (filteredOrders.isEmpty()) {
            binding.emptyTxt.visibility = View.VISIBLE
            binding.ordersView.visibility = View.GONE
        } else {
            binding.emptyTxt.visibility = View.GONE
            binding.ordersView.visibility = View.VISIBLE
            binding.ordersView.layoutManager = LinearLayoutManager(this)
            binding.ordersView.adapter = OrdersAdapter(filteredOrders)
        }
    }

    private fun showFilterDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_order_filter, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        val rbNewest = dialogView.findViewById<RadioButton>(R.id.rbNewest)
        val rbOldest = dialogView.findViewById<RadioButton>(R.id.rbOldest)
        val rbPriceHigh = dialogView.findViewById<RadioButton>(R.id.rbPriceHigh)
        val rbPriceLow = dialogView.findViewById<RadioButton>(R.id.rbPriceLow)
        
        val cbOdeslana = dialogView.findViewById<CheckBox>(R.id.cbOdeslana)
        val cbDokoncena = dialogView.findViewById<CheckBox>(R.id.cbDokoncena)
        val cbZrusena = dialogView.findViewById<CheckBox>(R.id.cbZrusena)

        when(currentSort) {
            "newest" -> rbNewest.isChecked = true
            "oldest" -> rbOldest.isChecked = true
            "priceHigh" -> rbPriceHigh.isChecked = true
            "priceLow" -> rbPriceLow.isChecked = true
        }
        cbOdeslana.isChecked = showOdeslana
        cbDokoncena.isChecked = showDokoncena
        cbZrusena.isChecked = showZrusena

        builder.setPositiveButton("Použít") { _, _ ->
            currentSort = when {
                rbNewest.isChecked -> "newest"
                rbOldest.isChecked -> "oldest"
                rbPriceHigh.isChecked -> "priceHigh"
                else -> "priceLow"
            }
            showOdeslana = cbOdeslana.isChecked
            showDokoncena = cbDokoncena.isChecked
            showZrusena = cbZrusena.isChecked
            applyFilters()
        }
        builder.setNegativeButton("Zrušit", null)
        builder.show()
    }

    private fun showClearDialog() {
        AlertDialog.Builder(this)
            .setTitle("Smazat historii")
            .setMessage("Opravdu chcete smazat všechny objednávky?")
            .setPositiveButton("Ano") { _, _ ->
                TinyDB(this).putListOrder("OrdersList", arrayListOf())
                loadData()
            }
            .setNegativeButton("Ne", null)
            .show()
    }
}
