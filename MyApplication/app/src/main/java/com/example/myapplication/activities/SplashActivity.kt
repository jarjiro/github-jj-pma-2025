package com.example.myapplication.activities

import android.animation.ObjectAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var inactivityRunnable: Runnable? = null
    private var bottomTextAnimator: ObjectAnimator? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()

        // 1. Najdeme prvky
        val videoView = findViewById<VideoView>(R.id.videoBtn)
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)
        val bottomPrompt = findViewById<TextView>(R.id.bottomPrompt)

        bottomPrompt.alpha = 0f 

        // 2. Nastavení videa
        val path = "android.resource://" + packageName + "/" + R.raw.tisk_tlacitko
        videoView.setVideoURI(Uri.parse(path))

        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            videoView.start()
        }

        // 3. Příprava animace
        bottomTextAnimator = ObjectAnimator.ofFloat(bottomPrompt, "alpha", 0f, 1f).apply {
            duration = 1000 
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE 
            interpolator = AccelerateDecelerateInterpolator()
        }

        // 4. Časovač na 5 sekund
        inactivityRunnable = Runnable {
            if (!isFinishing && !isDestroyed) {
                bottomTextAnimator?.start()
            }
        }
        handler.postDelayed(inactivityRunnable!!, 5000)

        // 5. Kliknutí kamkoliv -> Kontrola přihlášení a přechod dál
        mainLayout.setOnClickListener {
            stopAll()
            
            // --- LOGIKA PŘIHLÁŠENÍ ---
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // Uživatel je přihlášen -> Jdeme na hlavní obrazovku
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Uživatel NENÍ přihlášen -> Jdeme na Login
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }
    }

    private fun stopAll() {
        inactivityRunnable?.let { handler.removeCallbacks(it) }
        bottomTextAnimator?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAll()
    }
}
