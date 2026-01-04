package com.example.myapplication.Helper

import android.content.Context
import android.widget.Toast
import com.example.myapplication.domain.ItemsModel
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

class ManagmentCart(val context: Context) {
    private val tinyDB = TinyDB(context)
    private val auth = FirebaseAuth.getInstance()

    // Dynamický klíč podle přihlášeného uživatele
    private fun getCartKey(): String {
        val userId = auth.currentUser?.uid ?: "anonymous"
        return "CartList_$userId"
    }

    fun insertItems(item: ItemsModel) {
        val listItems = getListCart()
        val currentTotalCount = getTotalItemsCount()
        
        if (currentTotalCount + item.numberInCart > 20) {
            Toast.makeText(context, "Nelze přidat. Maximální počet produktů v objednávce je 20.", Toast.LENGTH_LONG).show()
            return
        }

        val index = listItems.indexOfFirst { it.title == item.title && it.selectedVariant == item.selectedVariant }

        if (index != -1) {
            listItems[index].numberInCart += item.numberInCart // Oprava: přičítáme k existujícímu
        } else {
            listItems.add(item)
        }
        
        tinyDB.putListObject(getCartKey(), listItems)
        
        val variantInfo = if (item.selectedVariant.isNullOrEmpty()) "" else " (${item.selectedVariant})"
        Toast.makeText(context, "${item.title}$variantInfo přidáno do košíku", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): ArrayList<ItemsModel> {
        return tinyDB.getListObject(getCartKey()) ?: ArrayList()
    }

    fun getTotalItemsCount(): Int {
        val listItems = getListCart()
        var total = 0
        for (item in listItems) {
            total += item.numberInCart
        }
        return total
    }

    fun minusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (listItems[position].numberInCart == 1) {
            listItems.removeAt(position)
        } else {
            listItems[position].numberInCart--
        }
        tinyDB.putListObject(getCartKey(), listItems)
        listener.onChanged()
    }

    fun plusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (getTotalItemsCount() >= 20) {
            Toast.makeText(context, "Dosažen limit 20 produktů.", Toast.LENGTH_SHORT).show()
            return
        }
        listItems[position].numberInCart++
        tinyDB.putListObject(getCartKey(), listItems)
        listener.onChanged()
    }

    fun removeItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        listItems.removeAt(position)
        tinyDB.putListObject(getCartKey(), listItems)
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listItems = getListCart()
        var fee = 0.0
        for (item in listItems) {
            fee += item.price * item.numberInCart
        }
        return fee
    }

    fun clearCart() {
        tinyDB.putListObject(getCartKey(), ArrayList<ItemsModel>())
    }
}
