package com.example.myapplication.Helper

import android.content.Context
import android.widget.Toast
import com.example.myapplication.domain.ItemsModel
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

class ManagmentFavorite(private val context: Context) {
    private val tinyDB: TinyDB = TinyDB(context)
    private val auth = FirebaseAuth.getInstance()

    // Pomocná funkce pro získání unikátního klíče pro každého uživatele
    private fun getFavoriteKey(): String {
        val userId = auth.currentUser?.uid ?: "guest"
        return "FavoriteList_$userId"
    }

    fun insertItem(item: ItemsModel) {
        val listFavorite = getListFavorite()
        var existAlready = false
        var n = 0
        for (i in listFavorite.indices) {
            if (listFavorite[i].title == item.title) {
                existAlready = true
                n = i
                break
            }
        }
        if (existAlready) {
            listFavorite.removeAt(n)
            Toast.makeText(context, "${item.title} odstraněno z oblíbených", Toast.LENGTH_SHORT).show()
        } else {
            listFavorite.add(item)
            Toast.makeText(context, "${item.title} přidáno do oblíbených", Toast.LENGTH_SHORT).show()
        }
        tinyDB.putListObject(getFavoriteKey(), listFavorite)
    }

    fun getListFavorite(): ArrayList<ItemsModel> {
        return tinyDB.getListObject(getFavoriteKey()) ?: ArrayList()
    }
    
    fun isFavorite(item: ItemsModel): Boolean {
        val listFavorite = getListFavorite()
        for (i in listFavorite.indices) {
            if (listFavorite[i].title == item.title) {
                return true
            }
        }
        return false
    }
}
