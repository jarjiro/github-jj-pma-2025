package com.example.myapp17_vanocniaplikacce.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapp17_vanocniaplikacce.DataStoreManager
import com.example.myapp17_vanocniaplikacce.databinding.FragmentGalleryBinding
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        dataStoreManager = DataStoreManager(requireContext())

        // Úkol 015: Načítání dat z DataStore
        lifecycleScope.launch {
            dataStoreManager.myGiftsFlow.collect { gifts ->
                if (gifts.isEmpty()) {
                    binding.textGallery.text = "Zatím jsi pod stromečkem nic nenašel. Utíkej do obýváku!"
                } else {
                    val formattedGifts = gifts.joinToString("\n\n") { "🎁 $it" }
                    binding.textGallery.text = formattedGifts
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
