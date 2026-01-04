package com.example.myapplication.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityFullscreenImageBinding

class FullscreenImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullscreenImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUrl = intent.getStringExtra("imageUrl")

        if (imageUrl != null) {
            Glide.with(this)
                .load(imageUrl)
                .into(binding.photoView)
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }
}