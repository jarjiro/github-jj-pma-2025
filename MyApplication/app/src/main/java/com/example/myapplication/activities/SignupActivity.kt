package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.signupBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (password.length >= 6) {
                    binding.progressBar.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            binding.progressBar.visibility = View.GONE
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Účet vytvořen!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Chyba: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Heslo musí mít alespoň 6 znaků", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vyplňte všechna pole", Toast.LENGTH_SHORT).show()
            }
        }

        binding.goToLoginBtn.setOnClickListener {
            finish() // Vrátí se na Login
        }
    }
}
