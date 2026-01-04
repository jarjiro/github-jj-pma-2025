package com.example.myapp17_vanocniaplikacce.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapp17_vanocniaplikacce.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        
        val formattedList = possibleGifts.joinToString("\n\n") { "✨ $it" }
        binding.textSlideshow.text = formattedList

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
