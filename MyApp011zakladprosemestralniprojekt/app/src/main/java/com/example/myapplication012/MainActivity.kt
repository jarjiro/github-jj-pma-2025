package com.example.myapplication012

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication012.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import android.view.ScaleGestureDetector
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private var scaleFactor = 1.0f
    private var isZoomed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Pinch-to-zoom ---
        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleFactor = 0.5f.coerceAtLeast(scaleFactor.coerceAtMost(3.0f))
                binding.imagePrinter.scaleX = scaleFactor
                binding.imagePrinter.scaleY = scaleFactor
                return true
            }
        })

        // --- Double-tap ---
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                if (!isZoomed) {
                    binding.imagePrinter.scaleX = 1.0f
                    binding.imagePrinter.scaleY = 1.0f
                    isZoomed = true
                } else {
                    binding.imagePrinter.scaleX = 1.0f
                    binding.imagePrinter.scaleY = 1.0f
                    isZoomed = false
                }
                return true
            }
        })

        binding.imagePrinter.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)
        }


// Nastavení počáteční ikony podle aktuálního režimu
        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        binding.btnToggleTheme.setImageResource(
            if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) R.drawable.ic_sun else R.drawable.ic_moon
        )

// Kliknutí pro přepnutí světlo/tma
        binding.btnToggleTheme.setOnClickListener {
            val nightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
            if (nightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.btnToggleTheme.setImageResource(R.drawable.ic_moon)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.btnToggleTheme.setImageResource(R.drawable.ic_sun)
            }
        }
        // =============================================================


        // --- Změna obrázku podle RadioButtonu ---
        binding.radioGroupPrinter.setOnCheckedChangeListener { _, checkedId ->
            // Reset zoomu při změně obrázku
            isZoomed = false
            scaleFactor = 1.0f
            binding.imagePrinter.scaleX = 1.0f
            binding.imagePrinter.scaleY = 1.0f

            binding.imagePrinter.setImageResource(
                when (checkedId) {
                    R.id.radioM10 -> R.drawable.oiz_m10_tr
                    R.id.radioM20 -> R.drawable.oiz_m20_tr
                    R.id.radioM30 -> R.drawable.oiz_m30_tr
                    R.id.radioM40 -> R.drawable.oiz_m40_tr
                    else -> R.drawable.oiz_m10_tr
                }
            )
        }
        //zobrazení obrázku když kliknu na checkbox
        binding.checkSupport.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.imagePrinter.setImageResource(R.drawable.manual_image)
            else binding.imagePrinter.setImageResource(R.drawable.oiz_m10_tr)
        }

        binding.checkColor.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.imagePrinter.setImageResource(R.drawable.filament_image)
            else binding.imagePrinter.setImageResource(R.drawable.oiz_m10_tr)
        }

        binding.checkFast.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.imagePrinter.setImageResource(R.drawable.fastkit_image)
            else binding.imagePrinter.setImageResource(R.drawable.oiz_m10_tr)
        }



        // --- Objednávka ---
            binding.btnOrder.setOnClickListener {
                val name = binding.editName.text.toString().trim()
                val email = binding.editEmail.text.toString().trim()

                if (name.isEmpty()) {
                    Toast.makeText(this, "Zadejte prosím jméno", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val allowedDomains = listOf(".cz", ".com", ".org") // povolené koncovky

                if (email.isEmpty() || !email.contains("@") || allowedDomains.none { email.endsWith(it) }) {
                    Toast.makeText(this, "Zadejte platný e-mail s koncovkou .cz, .com nebo .org", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (binding.radioGroupPrinter.checkedRadioButtonId == -1) {
                    Toast.makeText(this, "Vyberte typ tiskárny", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val printer = when (binding.radioGroupPrinter.checkedRadioButtonId) {
                    R.id.radioM10 -> "Prusa Mk4"
                    R.id.radioM20 -> "Ender V3"
                    R.id.radioM30 -> "Voron"
                    R.id.radioM40 -> "BambuLab"
                    else -> "žádný výběr"
                }

                val options = mutableListOf<String>()
                if (binding.checkSupport.isChecked) options.add("Manual pro začátečníky")
                if (binding.checkColor.isChecked) options.add("Filament")
                if (binding.checkFast.isChecked) options.add("Vybavení k 3D tisku")

                val summary = "Name: $name\nEmail: $email\nPrinter: $printer\nOptions: ${options.joinToString()}"
                binding.tvSummary.text = summary

                Toast.makeText(this, "Objednávka připravena!", Toast.LENGTH_SHORT).show()
                Snackbar.make(binding.root, "Objednávka shrnuta", Snackbar.LENGTH_SHORT).show()

                // AlertDialog se summary
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                builder.setTitle("Souhrn objednávky")
                builder.setMessage(summary)
                builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                builder.show()
            }
        }

        // --- Přesměrování dotyků pro pinch-to-zoom a double-tap ---
        override fun onTouchEvent(event: MotionEvent): Boolean {
            scaleGestureDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)
            return true
        }
}

