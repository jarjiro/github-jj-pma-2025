package com.example.myapp009aimagetoapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp009aimagetoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentScale = 1.0f // aktuální měřítko obrázku

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Výběr obrázku z galerie
        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { binding.ivImage.setImageURI(it) }
        }

        binding.btnTakeImage.setOnClickListener {
            getContent.launch("image/*")
        }

        // Přiblížení obrázku
        binding.btnZoomIn.setOnClickListener {
            currentScale += 0.1f
            binding.ivImage.scaleX = currentScale
            binding.ivImage.scaleY = currentScale
        }

        // Oddálení obrázku
        binding.btnZoomOut.setOnClickListener {
            currentScale -= 0.1f
            if (currentScale < 0.1f) currentScale = 0.1f // minimální velikost
            binding.ivImage.scaleX = currentScale
            binding.ivImage.scaleY = currentScale
        }
        // Reset obrázku na původní velikost
        binding.btnReset.setOnClickListener {
            currentScale = 1.0f
            binding.ivImage.scaleX = currentScale
            binding.ivImage.scaleY = currentScale
        }
    }
}
