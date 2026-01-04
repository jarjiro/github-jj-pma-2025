package com.example.myapp012amynotehub

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp012amynotehub.data.Note
import com.example.myapp012amynotehub.data.NoteDao
import com.example.myapp012amynotehub.data.NoteHubDatabaseInstance
import com.example.myapp012amynotehub.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteDao: NoteDao
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FLOW: hledání + kategorie
        val searchQuery = MutableStateFlow("")
        val categoryFilter = MutableStateFlow("Vše")

        // ---------- FAB: Přidání poznámky ----------
        binding.fabAddNote.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        // ---------- RecyclerView ----------
        adapter = NoteAdapter(
            onEditClick = { note ->
                val intent = Intent(this, EditNoteActivity::class.java)
                intent.putExtra("note_id", note.id)
                startActivity(intent)
            },
            onDeleteClick = { note -> deleteNote(note) }
        )

        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotes.adapter = adapter

        // ---------- DAO ----------
        noteDao = NoteHubDatabaseInstance.getDatabase(this).noteDao()

        // ---------- KATEGORIE (Spinner) ----------
        val categories = listOf("Vše", "Ostatní", "Škola", "Práce", "Osobní")

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilterCategory.adapter = spinnerAdapter

        binding.spinnerFilterCategory.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    categoryFilter.value = categories[position]
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
            }

        // ---------- SEARCHVIEW ----------
        binding.searchViewNotes.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery.value = newText ?: ""
                return true
            }
        })

        // ---------- KOMBINACE: kategorie + search ----------
        lifecycleScope.launch {
            combine(searchQuery, categoryFilter) { search, category ->
                Pair(search, category)
            }.collectLatest { (search, category) ->

                when {
                    // Kategorie + text
                    category != "Vše" && search.isNotEmpty() -> {
                        noteDao.searchNotes(search).collectLatest { notes ->
                            adapter.submitList(notes.filter { it.category == category })
                        }
                    }

                    // Jen kategorie
                    category != "Vše" -> {
                        noteDao.filterByCategory(category).collectLatest { notes ->
                            adapter.submitList(notes)
                        }
                    }

                    // Jen text
                    search.isNotEmpty() -> {
                        noteDao.searchNotes(search).collectLatest { notes ->
                            adapter.submitList(notes)
                        }
                    }

                    // Nic — vše
                    else -> {
                        noteDao.getAllNotes().collectLatest { notes ->
                            adapter.submitList(notes)
                        }
                    }
                }
            }
        }
    }

    private fun deleteNote(note: Note) {
        lifecycleScope.launch(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }
}
