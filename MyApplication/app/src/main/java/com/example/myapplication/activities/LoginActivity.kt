package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        binding.progressBar.visibility = View.GONE
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            // PŘELOŽENÉ CHYBY PRO PŘIHLÁŠENÍ
                            val errorMessage = when (task.exception) {
                                is FirebaseAuthInvalidUserException -> "Uživatel s tímto e-mailem neexistuje."
                                is FirebaseAuthInvalidCredentialsException -> "Nesprávné heslo nebo e-mail."
                                else -> "Přihlášení se nezdařilo. Zkontrolujte připojení."
                            }
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Vyplňte všechna pole", Toast.LENGTH_SHORT).show()
            }
        }

        binding.goToSignupBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
