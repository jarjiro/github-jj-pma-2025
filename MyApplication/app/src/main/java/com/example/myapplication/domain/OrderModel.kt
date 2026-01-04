package com.example.myapplication.domain

import java.io.Serializable

data class OrderModel(
    var orderId: Int = (1000..9999).random(),
    var items: ArrayList<ItemsModel> = ArrayList(),
    var totalPrice: Double = 0.0,
    var deliveryCost: Double = 0.0,
    var date: Long = System.currentTimeMillis(),
    var status: String = "Odeslána",
    var userId: String = "",    // ID uživatele z Firebase Auth
    var userEmail: String = ""  // Email uživatele
) : Serializable
