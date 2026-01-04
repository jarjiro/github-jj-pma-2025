package com.example.myapplication.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.Helper.ManagmentCart
import com.example.myapplication.Helper.ManagmentOrders
import com.example.myapplication.R
import com.example.myapplication.adapters.CategoryAdapter
import com.example.myapplication.adapters.PopularAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel = MainViewModel()
    private lateinit var managmentCart: ManagmentCart
    private lateinit var managmentOrders: ManagmentOrders

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        managmentCart = ManagmentCart(this)
        managmentOrders = ManagmentOrders(this)
        
        initBanner()
        initCategory()
        initPopular()
        initBottomMenu()
        initSearch()
        setupSearchSuggestions()
    }

    override fun onResume() {
        super.onResume()
        updateCartBadge()
        updateOrderNotification() 
    }

    private fun updateCartBadge() {
        val count = managmentCart.getTotalItemsCount()
        if (count > 0) {
            binding.cartBadgeTxt.visibility = View.VISIBLE
            binding.cartBadgeTxt.text = count.toString()
        } else {
            binding.cartBadgeTxt.visibility = View.GONE
        }
    }

    private fun updateOrderNotification() {
        val orders = managmentOrders.getOrders()
        val hasNewOrder = orders.any { it.status == "Odeslána" }
        if (hasNewOrder) {
            binding.orderNotificationDot.visibility = View.VISIBLE
        } else {
            binding.orderNotificationDot.visibility = View.GONE
        }
    }

    private fun setupSearchSuggestions() {
        viewModel.loadPopular().observe(this) { items ->
            val productNames = items.map { it.title }
            val adapter = ArrayAdapter(
                this, 
                R.layout.viewholder_search_suggestion, 
                productNames
            )
            binding.editTextText.setAdapter(adapter)
            binding.editTextText.threshold = 1
            binding.editTextText.setOnItemClickListener { parent, _, position, _ ->
                val selectedQuery = parent.getItemAtPosition(position) as String
                startSearch(selectedQuery)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSearch() {
        binding.editTextText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2 
                if (binding.editTextText.compoundDrawables[drawableEnd] != null) {
                    val iconWidth = binding.editTextText.compoundDrawables[drawableEnd].bounds.width()
                    if (event.rawX >= (binding.editTextText.right - iconWidth - binding.editTextText.paddingEnd)) {
                        binding.editTextText.text.clear()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        binding.editTextText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.editTextText.text.toString()
                if (query.isNotEmpty()) {
                    startSearch(query)
                }
                true
            } else {
                false
            }
        }
    }

    private fun startSearch(query: String) {
        val intent = Intent(this, ItemsListActivity::class.java)
        intent.putExtra("searchQuery", query)
        intent.putExtra("title", "Výsledky pro: $query")
        startActivity(intent)
    }

    private fun initBottomMenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        binding.allBtn.setOnClickListener {
            startActivity(Intent(this, AllProductsActivity::class.java))
        }
        binding.favBtnMain.setOnClickListener {
            startActivity(Intent(this, WishlistActivity::class.java))
        }
        binding.ordersBtn.setOnClickListener {
            binding.orderNotificationDot.visibility = View.GONE
            startActivity(Intent(this, OrdersActivity::class.java))
        }
        // PROPOJENÍ TLAČÍTKA PROFILU
        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun initPopular() {
        binding.apply { 
            progressBarPopular.visibility = View.VISIBLE
            viewModel.loadPopular().observe(this@MainActivity) {
                popularView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                popularView.adapter = PopularAdapter(it)
                progressBarPopular.visibility = View.GONE
            }
        }
    }

    private fun initCategory(){
        binding.apply {
            progressBarCategory.visibility = View.VISIBLE
            viewModel.loadCategory().observe(this@MainActivity) {
                categoryView.adapter = CategoryAdapter(it)
                categoryView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                progressBarCategory.visibility = View.GONE
            }
        }
    }

    private fun initBanner(){
        binding.apply {
            progressBarBanner.visibility = View.VISIBLE
            viewModel.loadBanner().observe(this@MainActivity) {
                if (it.isNotEmpty()) {
                    Glide.with(this@MainActivity)
                        .load(it[0].url)
                        .into(banner)
                }
                progressBarBanner.visibility = View.GONE
            }
        }
    }
}
