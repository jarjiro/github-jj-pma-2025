package com.example.myapp17_vanocniaplikacce.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapp17_vanocniaplikacce.DataStoreManager
import com.example.myapp17_vanocniaplikacce.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStoreManager: DataStoreManager

    private val possibleGifts = listOf(
        "Nový iPhone", "Ponožky s losem", "Lego Star Wars", 
        "Čokoládová kolekce", "Kniha o Kotlinu", "Plyšový tučňák",
        "Dárkový poukaz do Alzy", "Bezdrátová sluchátka"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        dataStoreManager = DataStoreManager(requireContext())

        binding.btnOpenGift.setOnClickListener {
            val randomGift = possibleGifts.random()
            
            lifecycleScope.launch {
                dataStoreManager.addGift(randomGift)
                Toast.makeText(context, "Získal jsi: $randomGift 🎁", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
