package com.example.myapplication.Helper

import android.content.Context
import android.widget.Toast
import com.example.myapplication.domain.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ManagmentOrders(val context: Context) {
    private val tinyDB = TinyDB(context)
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun addOrder(order: OrderModel) {
        val currentUser = auth.currentUser
        
        if (currentUser != null) {
            order.userId = currentUser.uid
            order.userEmail = currentUser.email ?: "anonym@email.cz"
        }

        order.orderId = (100000..999999).random()

        sendOrderToFirestore(order)
    }

    private fun sendOrderToFirestore(order: OrderModel) {
        val itemsMap = order.items.map { item ->
            mapOf(
                "title" to item.title,
                "price" to item.price,
                "quantity" to item.numberInCart,
                "picUrl" to (if (item.picUrl.isNotEmpty()) item.picUrl[0] else "")
            )
        }

        val orderData = hashMapOf(
            "orderId" to order.orderId,
            "totalPrice" to order.totalPrice,
            "deliveryCost" to order.deliveryCost,
            "date" to order.date,
            "status" to order.status,
            "userId" to order.userId,
            "userEmail" to order.userEmail,
            "items" to itemsMap
        )

        db.collection("Orders")
            .document(order.orderId.toString())
            .set(orderData)
            .addOnSuccessListener {
                Toast.makeText(context, "Objednávka #${order.orderId} odeslána.", Toast.LENGTH_SHORT).show()
                
                val currentOrders = getLocalOrders()
                currentOrders.add(0, order)
                tinyDB.putListOrder("OrdersList_" + auth.currentUser?.uid, currentOrders)
            }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        // Aktualizace ve Firestore
        db.collection("Orders").document(orderId).update("status", newStatus)
            .addOnSuccessListener {
                // Po úspěchu ve Firebase aktualizujeme i lokální TinyDB pro aktuálního uživatele
                val userId = auth.currentUser?.uid ?: return@addOnSuccessListener
                val orders = getLocalOrders()
                val idInt = orderId.toIntOrNull() ?: return@addOnSuccessListener
                
                val index = orders.indexOfFirst { it.orderId == idInt }
                if (index != -1) {
                    orders[index].status = newStatus
                    tinyDB.putListOrder("OrdersList_$userId", orders)
                }
            }
    }

    fun fetchUserOrders(onSuccess: (ArrayList<OrderModel>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        
        db.collection("Orders")
            .whereEqualTo("userId", userId)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val ordersList = ArrayList<OrderModel>()
                for (document in documents) {
                    val order = document.toObject(OrderModel::class.java)
                    ordersList.add(order)
                }
                tinyDB.putListOrder("OrdersList_" + userId, ordersList)
                onSuccess(ordersList)
            }
            .addOnFailureListener {
                onSuccess(getLocalOrders())
            }
    }

    fun getLocalOrders(): ArrayList<OrderModel> {
        val userId = auth.currentUser?.uid ?: return arrayListOf()
        return tinyDB.getListOrder("OrdersList_" + userId) ?: arrayListOf()
    }
    
    fun getOrders(): ArrayList<OrderModel> {
        return getLocalOrders()
    }
}
