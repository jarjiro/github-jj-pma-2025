package com.example.myapplication007kol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ListFragment : Fragment() {

    interface OnPlanetSelectedListener {
        fun onPlanetSelected(name: String, description: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val listener = activity as? OnPlanetSelectedListener

        view.findViewById<Button>(R.id.btn_mars).setOnClickListener {
            listener?.onPlanetSelected("Mars", "Mars je čtvrtá planeta sluneční soustavy, druhá nejmenší planeta soustavy po Merkuru.")
        }

        view.findViewById<Button>(R.id.btn_jupiter).setOnClickListener {
            listener?.onPlanetSelected("Jupiter", "Jupiter je největší planeta sluneční soustavy, v pořadí pátá od Slunce.")
        }

        view.findViewById<Button>(R.id.btn_saturn).setOnClickListener {
            listener?.onPlanetSelected("Saturn", "Saturn je v pořadí šestá planeta od Slunce a po Jupiteru druhá největší planeta sluneční soustavy.")
        }

        return view
    }
}