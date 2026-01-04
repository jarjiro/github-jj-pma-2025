package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.myapplication.domain.BannerModel
import com.example.myapplication.domain.CategoryModel
import com.example.myapplication.domain.ItemsModel
import com.example.myapplication.repository.MainRepository

class MainViewModel: ViewModel() {
    private val repository = MainRepository()

    fun loadBanner(): LiveData<MutableList<BannerModel>> {
        return repository.loadBanner()
    }

    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }

    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        return repository.loadPopular()
    }

    fun loadAllItems(): LiveData<MutableList<ItemsModel>> {
        return repository.loadAllItems()
    }

    fun loadItems(categoryId: String): LiveData<MutableList<ItemsModel>> {
        return repository.loadItemsCategory(categoryId)
    }

    // Funkce pro vyhledávání
    fun searchItems(searchText: String): LiveData<MutableList<ItemsModel>> {
        return repository.loadAllItems().map { items ->
            items.filter { it.title.contains(searchText, ignoreCase = true) }.toMutableList()
        }
    }
}