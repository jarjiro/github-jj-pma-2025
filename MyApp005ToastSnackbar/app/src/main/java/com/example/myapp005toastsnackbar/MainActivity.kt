package com.example.myapp005toastsnackbar

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp005toastsnackbar.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nastavení akce pro tlačítko Zobrazit TOAST s ikonou (Custom Layout)
        binding.btnShowToast.setOnClickListener {
            // 1. Nafouknutí (inflate) vlastního layoutu
            val inflater: LayoutInflater = layoutInflater
            val layout = inflater.inflate(R.layout.layout_custom_toast, findViewById(R.id.custom_toast_container))

            // 2. Nastavení textu v layoutu
            val text: TextView = layout.findViewById(R.id.toast_text)
            text.text = "Zdravím s ikonou!"

            // 3. Vytvoření a zobrazení Toastu
            with (Toast(applicationContext)) {
                duration = Toast.LENGTH_LONG
                view = layout // Zde přiřazujeme náš vlastní vzhled
                show()
            }
        }

        // Nastavení akce pro tlačítko Zobrazit SNACKBAR
        binding.btnShowSnackbar.setOnClickListener {
            val snackbar = Snackbar.make(binding.root, "Jsem Snackbar = jsem víc než TOAST", Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.parseColor("#FFCC50"))
                .setTextColor(Color.BLACK)
                .setDuration(7000)
                .setActionTextColor(Color.WHITE)
                .setAction("Zavřít") {
                    Toast.makeText(this, "Zavírám SNACKBAR", Toast.LENGTH_SHORT).show()
                }
            
            snackbar.show()
        }
    }
}
