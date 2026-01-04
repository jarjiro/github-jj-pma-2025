package com.example.myapp010

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp010.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var secretNumber = 0
    private var attempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newNumber()

        binding.btnCheck.setOnClickListener{
            val guessTip = binding.etGuess.text.toString()
            if(guessTip.isEmpty()){
                Toast.makeText(this,"Musis zadat cislo!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val guessTipInt = guessTip.toInt()
            attempts++

            if(guessTipInt == secretNumber){

                Toast.makeText(this, "Počet pokusů: $attempts", Toast.LENGTH_SHORT).show()
                newNumber()
                attempts = 0

            }
            else if(guessTipInt < secretNumber){
                Toast.makeText(this, "Zkus vyšší číslo", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "Zkus menší číslo", Toast.LENGTH_SHORT).show()
        }
        }
    }

    private fun newNumber() {
        secretNumber = Random.nextInt(1,11)
    }
}