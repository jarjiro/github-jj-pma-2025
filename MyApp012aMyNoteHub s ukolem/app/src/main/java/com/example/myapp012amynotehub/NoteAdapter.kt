package com.example.myapp012amynotehub

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp012amynotehub.data.Note
import com.example.myapp012amynotehub.databinding.ItemNoteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// DOPLNĚNO: callbacky pro editaci a mazání
class NoteAdapter(
    private val onEditClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    // Seznam poznámek, které se mají zobrazit
    private var notes: List<Note> = emptyList()

    // ViewHolder drží binding jednoho itemu (jedné poznámky)
    class NoteViewHolder(val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Vytvoří nový ViewHolder – tedy grafiku jedné položky v seznamu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NoteViewHolder(binding)
    }

    // Naplní jednu položku daty z konkrétní poznámky (title + content)
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notes[position]

        holder.binding.tvNoteTitle.text = currentNote.title
        holder.binding.tvNoteContent.text = currentNote.content

        // DOPLNĚNO: Kliknutí na EDIT ikonu
        holder.binding.ivEdit.setOnClickListener {
            onEditClick(currentNote)
        }

        // DOPLNĚNO: Kliknutí na DELETE ikonu
        holder.binding.ivDelete.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setMessage("Opravdu chcete smazat tuto poznámku?")
                .setPositiveButton("ANO") { dialog, _ ->
                    // Mazání na IO dispatcher, ale spouštíme přes lifecycleScope
                    (holder.itemView.context as? AppCompatActivity)?.lifecycleScope?.launch(
                        Dispatchers.IO) {
                        onDeleteClick(currentNote)
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("NE") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    // Vrátí počet poznámek → RecyclerView ví, kolik řádků má vykreslit
    override fun getItemCount(): Int = notes.size

    // Aktualizuje seznam poznámek a obnoví zobrazení
    fun submitList(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged() // oznámí RecyclerView, že má překreslit data
    }
}