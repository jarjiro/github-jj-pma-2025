package com.example.myapplication007kol

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity(), ListFragment.OnPlanetSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Pokud je savedInstanceState null, znamená to, že aplikace startuje poprvé
        // a musíme vložit první fragment (seznam planet).
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragment_container, ListFragment())
            }
        }
    }

    override fun onPlanetSelected(name: String, description: String) {
        // Při kliknutí na planetu v seznamu nahradíme ListFragment za DetailFragment
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, DetailFragment.newInstance(name, description))
            addToBackStack(null) // Umožní návrat tlačítkem zpět
        }
    }
}