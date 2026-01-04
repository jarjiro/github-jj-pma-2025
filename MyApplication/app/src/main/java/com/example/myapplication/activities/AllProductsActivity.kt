package com.example.myapplication.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.adapters.ItemListCategoryAdapter
import com.example.myapplication.databinding.ActivityItemsListBinding
import com.example.myapplication.viewmodel.MainViewModel

class AllProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemsListBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initList()
    }

    private fun initList() {
        binding.apply {
            // Nastavíme název na "Všechny produkty"
            categoryTxt.text = "Všechny produkty"
            
            listView.layoutManager = GridLayoutManager(this@AllProductsActivity, 2)
            progressBar2.visibility = View.VISIBLE

            // Voláme novou funkci pro načtení VŠEHO
            viewModel.loadAllItems().observe(this@AllProductsActivity) { items ->
                if (items != null) {
                    listView.adapter = ItemListCategoryAdapter(items)
                }
                progressBar2.visibility = View.GONE
            }

            // backBtn nyní obsahuje ikonu i text "Zpět" jako jeden celek
            backBtn.setOnClickListener { finish() }
        }
    }
}
