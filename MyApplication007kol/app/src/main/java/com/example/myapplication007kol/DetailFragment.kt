package com.example.myapplication007kol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class DetailFragment : Fragment() {

    companion object {
        private const val ARG_NAME = "planet_name"
        private const val ARG_DESC = "planet_desc"

        fun newInstance(name: String, description: String): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putString(ARG_NAME, name)
            args.putString(ARG_DESC, description)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val name = arguments?.getString(ARG_NAME)
        val description = arguments?.getString(ARG_DESC)

        view.findViewById<TextView>(R.id.tv_planet_name).text = name
        view.findViewById<TextView>(R.id.tv_planet_description).text = description

        view.findViewById<Button>(R.id.btn_back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}