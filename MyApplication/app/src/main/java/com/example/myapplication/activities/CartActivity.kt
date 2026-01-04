package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Helper.ChangeNumberItemsListener
import com.example.myapplication.Helper.ManagmentCart
import com.example.myapplication.Helper.ManagmentOrders
import com.example.myapplication.Helper.PriceFormatter
import com.example.myapplication.R
import com.example.myapplication.adapters.CartAdapter
import com.example.myapplication.databinding.ActivityCartBinding
import com.example.myapplication.domain.OrderModel
import com.google.firebase.auth.FirebaseAuth

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managmentCart: ManagmentCart
    private lateinit var managmentOrders: ManagmentOrders
    private var tax: Double = 0.0
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)
        managmentOrders = ManagmentOrders(this)

        setupSwipeToRefresh()
        calculatorCart()
        setVariable()
        initCartList()
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            initCartList()
            calculatorCart()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.orange)
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
        
        binding.checkOutBtn.setOnClickListener {
            // KONTROLA PŘIHLÁŠENÍ PŘED OBJEDNÁVKOU
            if (auth.currentUser == null) {
                Toast.makeText(this, "Pro dokončení objednávky se prosím přihlaste", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, LoginActivity::class.java))
            } else if (managmentCart.getListCart().isEmpty()) {
                showEmptyCartDialog()
            } else {
                showOrderReviewDialog()
            }
        }
    }

    private fun showEmptyCartDialog() {
        AlertDialog.Builder(this)
            .setTitle("Prázdný košík")
            .setMessage("V košíku nemáte žádné produkty.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showOrderReviewDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_order_review, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        val summaryTxt = dialogView.findViewById<TextView>(R.id.orderSummaryTxt)
        val totalTxt = dialogView.findViewById<TextView>(R.id.orderTotalTxt)

        val cartList = managmentCart.getListCart()
        val itemTotal = managmentCart.getTotalFee()
        
        val totalCount = managmentCart.getTotalItemsCount()
        val delivery = when {
            totalCount == 0 -> 0.0
            totalCount <= 3 -> 150.0
            totalCount <= 9 -> 300.0
            else -> 600.0
        }
        
        val totalFinal = itemTotal + delivery + tax

        val summaryText = StringBuilder()
        for (item in cartList) {
            val variantInfo = if (item.selectedVariant.isEmpty()) "" else " (${item.selectedVariant})"
            summaryText.append("${item.numberInCart}x ${item.title}$variantInfo\n")
            summaryText.append("   ${PriceFormatter.format(item.price * item.numberInCart)}\n\n")
        }
        
        summaryTxt.text = summaryText.toString()
        totalTxt.text = "CELKEM: ${PriceFormatter.format(totalFinal)}"

        builder.setPositiveButton("Potvrdit objednávku") { _, _ ->
            // Vytvoření objednávky s údaji přihlášeného uživatele
            val currentUser = auth.currentUser
            val newOrder = OrderModel(
                items = ArrayList(cartList),
                totalPrice = totalFinal,
                deliveryCost = delivery,
                userId = currentUser?.uid ?: "",
                userEmail = currentUser?.email ?: ""
            )
            managmentOrders.addOrder(newOrder)
            managmentCart.clearCart()
            showSuccessDialog()
        }
        builder.setNegativeButton("Zrušit", null)
        builder.show()
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Objednávka odeslána")
            .setMessage("Vaše objednávka byla úspěšně vytvořena. Děkujeme!")
            .setPositiveButton("Do historie") { _, _ ->
                startActivity(Intent(this, OrdersActivity::class.java))
                finish()
            }
            .setNegativeButton("Zavřít") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    private fun initCartList() {
        binding.cartView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.cartView.adapter = CartAdapter(managmentCart.getListCart(), this, object : ChangeNumberItemsListener {
            override fun onChanged() {
                calculatorCart()
            }
        })

        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.visibility = View.VISIBLE
            binding.swipeRefreshLayout.visibility = View.GONE
            binding.footerLayout.visibility = View.GONE
        } else {
            binding.emptyTxt.visibility = View.GONE
            binding.swipeRefreshLayout.visibility = View.VISIBLE
            binding.footerLayout.visibility = View.VISIBLE
        }
    }

    private fun calculatorCart() {
        val percentTax = 0.21
        val itemTotal = managmentCart.getTotalFee()
        
        val totalCount = managmentCart.getTotalItemsCount()
        val delivery = when {
            totalCount == 0 -> 0.0
            totalCount <= 3 -> 150.0
            totalCount <= 9 -> 300.0
            else -> 600.0
        }
        
        tax = Math.round((itemTotal * percentTax) * 100) / 100.0
        val total = Math.round((itemTotal + tax + delivery) * 100) / 100.0

        binding.totalFeeTxt.text = PriceFormatter.format(itemTotal)
        binding.taxTxt.text = PriceFormatter.format(tax)
        binding.deliveryTxt.text = PriceFormatter.format(delivery)
        binding.totalTxt.text = PriceFormatter.format(total)
    }
}
