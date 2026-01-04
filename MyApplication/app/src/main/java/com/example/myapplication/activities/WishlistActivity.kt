package com.example.myapplication.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.Helper.ManagmentFavorite
import com.example.myapplication.R
import com.example.myapplication.adapters.ItemListCategoryAdapter
import com.example.myapplication.databinding.ActivityFavListBinding

class WishlistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavListBinding
    private lateinit var managmentFavorite: ManagmentFavorite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentFavorite = ManagmentFavorite(this)

        initList()
        setupSwipeToRefresh()
        
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Znovu načteme seznam z TinyDB
            initList()
            // Ukončíme animaci
            binding.swipeRefreshLayout.isRefreshing = false
        }
        
        // Nastavení firemní barvy pro animaci
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.orange)
    }

    private fun initList() {
        val list = managmentFavorite.getListFavorite()
        
        if (list.isEmpty()) {
            binding.emptyTxt.visibility = View.VISIBLE
            binding.swipeRefreshLayout.visibility = View.GONE
        } else {
            binding.emptyTxt.visibility = View.GONE
            binding.swipeRefreshLayout.visibility = View.VISIBLE
            
            binding.favListView.layoutManager = GridLayoutManager(this, 2)
            binding.favListView.adapter = ItemListCategoryAdapter(list)
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Aktualizujeme seznam při každém návratu na obrazovku (např. po odebrání v detailu)
        initList()
    }
}
