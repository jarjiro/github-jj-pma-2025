package com.example.mapplication014

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapplication014.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore

    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore

        // Nastavení adapteru
        adapter = TaskAdapter(
            tasks = emptyList(),
            onChecked = { task -> toggleCompleted(task) },
            onDelete = { task -> deleteTask(task) },
            onEdit = { task -> editTask(task) }

        )

        binding.recyclerViewTasks.adapter = adapter
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        // Přidání úkolu
        binding.buttonAdd.setOnClickListener {
            val title = binding.inputTask.text.toString()
            if (title.isNotEmpty()) {
                addTask(title)
                binding.inputTask.text.clear()
            }
        }

        // Realtime sledování Firestore
        listenForTasks()
    }

    private fun addTask(title: String) {
        println("DEBUG: addTask called with title = $title")
        val task = Task(title = title, completed = false)
        db.collection("tasks").add(task)
    }

    private fun toggleCompleted(task: Task) {

        // Vyhledá v databázi všechny dokumenty, které mají stejné title jako kliknutý úkol
        db.collection("tasks")
            .whereEqualTo("title", task.title)
            .get()
            .addOnSuccessListener { docs ->

                // Pro každý nalezený dokument změní hodnotu "completed" na opačnou
                for (doc in docs) {
                    db.collection("tasks")
                        .document(task.id)
                        .update("completed", !task.completed)
                }
            }
    }

    private fun deleteTask(task: Task) {
        db.collection("tasks")
            .whereEqualTo("title", task.title)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    db.collection("tasks").document(task.id).delete()
                }
            }
    }
    private fun editTask(task: Task) {
        val input = EditText(this)
        input.setText(task.title)
        AlertDialog.Builder(this)
            .setTitle("Editovat úkol")
            .setView(input)
            .setPositiveButton("Uložit") { _, _ ->
                val newTitle = input.text.toString()
                if (newTitle.isNotEmpty()) {
                    db.collection("tasks")
                        .document(task.id)
                        .update("title", newTitle)
                }
            }
            .setNegativeButton("Zrušit", null)
            .show()
    }


    private fun listenForTasks() {
        db.collection("tasks")
            .addSnapshotListener { snapshots, _ ->
                val taskList = snapshots?.map { doc ->
                    Task(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        completed = doc.getBoolean("completed") ?: false
                    )
                } ?: emptyList()
                adapter.submitList(taskList)
            }
    }
}
