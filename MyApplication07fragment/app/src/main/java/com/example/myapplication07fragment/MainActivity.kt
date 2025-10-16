package com.example.myapplication07fragment

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapplication07fragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Použijte binding.root k nastavení obsahu pohledu
        setContentView(binding.root)

        // Tento kód by měl nyní fungovat správně
        binding.btnFragment1.setOnClickListener {
            replaceFragment(Fragment1())
        }
        binding.btnFragment2.setOnClickListener {
            replaceFragment(Fragment2())
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        //získání instance správce fragmentu
        val fragmentManager = supportFragmentManager
        //vytvoření transakce
        val fragmentTransaction = fragmentManager.beginTransaction()
        //nahrazení fragmentu
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()

    }
}