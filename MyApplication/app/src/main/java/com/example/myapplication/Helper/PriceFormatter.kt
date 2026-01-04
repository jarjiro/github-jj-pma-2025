package com.example.myapplication.Helper

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object PriceFormatter {
    fun format(price: Double): String {
        val symbols = DecimalFormatSymbols(Locale.getDefault())
        symbols.groupingSeparator = ' ' // Mezera pro tisíce
        symbols.decimalSeparator = ','   // Čárka pro desetinná místa
        
        val df = DecimalFormat("###,###.00", symbols)
        return "CZK " + df.format(price)
    }
}
