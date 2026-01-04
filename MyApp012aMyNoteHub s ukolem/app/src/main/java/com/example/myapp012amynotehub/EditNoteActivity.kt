package com.example.myapp012amynotehub

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp012amynotehub.data.Note
import com.example.myapp012amynotehub.data.NoteDao
import com.example.myapp012amynotehub.data.NoteHubDatabaseInstance
import com.example.myapp012amynotehub.databinding.ActivityEditNoteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var noteDao: NoteDao
    private var noteId: Int = -1

    private val categories = listOf("Ostatní", "Škola", "Práce", "Osobní")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteDao = NoteHubDatabaseInstance.getDatabase(this).noteDao()

        // ---------- Načtení ID poznámky ----------
        noteId = intent.getIntExtra("note_id", -1)
        binding.tvNoteId.text = "ID: $noteId"

        // ---------- Nastavení Kategorie Spinneru ----------
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEditCategory.adapter = spinnerAdapter

        // ---------- Načtení poznámky z DB ----------
        lifecycleScope.launch {
            val allNotes = noteDao.getAllNotes().first()
            val note = allNotes.find { it.id == noteId }

            if (note != null) {
                binding.etEditTitle.setText(note.title)
                binding.etEditContent.setText(note.content)

                // Nastavení Spinneru podle kategorie
                val index = categories.indexOf(note.category)
                if (index >= 0) {
                    binding.spinnerEditCategory.setSelection(index)
                }
            }
        }

        // ---------- Tlačítko ULOŽIT změny ----------
        binding.btnSaveChanges.setOnClickListener {

            val updatedTitle = binding.etEditTitle.text.toString()
            val updatedContent = binding.etEditContent.text.toString()
            val updatedCategory = binding.spinnerEditCategory.selectedItem.toString()

            val updatedNote = Note(
                id = noteId,
                title = updatedTitle,
                content = updatedContent,
                category = updatedCategory
            )

            lifecycleScope.launch(Dispatchers.IO) {
                noteDao.update(updatedNote)
                finish()
            }
        }
    }
}
