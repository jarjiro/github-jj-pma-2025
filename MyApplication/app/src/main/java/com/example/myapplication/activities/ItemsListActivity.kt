package com.example.myapplication.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.adapters.ItemListCategoryAdapter
import com.example.myapplication.databinding.ActivityItemsListBinding
import com.example.myapplication.viewmodel.MainViewModel

class ItemsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemsListBinding
    private val viewModel: MainViewModel by viewModels()

    private var id: String = ""
    private var title: String = ""
    private var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundles()
        initList()
    }

    private fun getBundles() {
        // Zjistíme, jestli přicházíme z vyhledávání nebo z kategorie
        searchQuery = intent.getStringExtra("searchQuery")
        
        if (searchQuery != null) {
            title = intent.getStringExtra("title") ?: "Výsledky hledání"
        } else {
            val intId = intent.getIntExtra("id", -1)
            id = intId.toString()
            title = intent.getStringExtra("title") ?: ""
        }

        binding.categoryTxt.text = title
    }

    private fun initList() {
        binding.apply {
            listView.layoutManager = GridLayoutManager(this@ItemsListActivity, 2)
            progressBar2.visibility = View.VISIBLE

            val observer = { items: MutableList<com.example.myapplication.domain.ItemsModel>? ->
                if (items != null && items.isNotEmpty()) {
                    listView.adapter = ItemListCategoryAdapter(items)
                } else {
                    Log.d("SEARCH_DEBUG", "Nenalezeny žádné produkty")
                }
                progressBar2.visibility = View.GONE
            }

            if (searchQuery != null) {
                // Vyhledávání
                viewModel.searchItems(searchQuery!!).observe(this@ItemsListActivity, observer)
            } else {
                // Kategorie
                viewModel.loadItems(id).observe(this@ItemsListActivity, observer)
            }

            backBtn.setOnClickListener { finish() }
        }
    }
}