package com.example.todolistapp

import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.example.todolistapp.database.TodoDatabase
import com.example.todolistapp.databinding.ActivityAddNoteBinding
import com.example.todolistapp.model.Todo
import java.lang.Exception

class AddNoteActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddNoteBinding
    lateinit var db: TodoDatabase
    private var todo: Todo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //Delete Btn Hide
        binding.btnDelete.visibility = View.GONE


        //Data Parsing Home to Note Get Data (this work only api 21 but api 33 is new method if(api 33) and else(api 33>below))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            todo = intent.getParcelableExtra("TODOLIST", Todo::class.java)
        } else {
            todo = intent.getParcelableExtra("TODOLIST")
        }

        //add list
        if (todo != null) {
            binding.etTitle.setText(todo!!.title)
            binding.etNote.setText(todo!!.msg)
            //Show Delete Btn
            binding.btnDelete.visibility = View.VISIBLE
        }

        // Add Database File
        db = Room.databaseBuilder(this, TodoDatabase::class.java, "todolist.db")
            .allowMainThreadQueries().build()

        binding.btnAdd.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val msg = binding.etNote.text.toString()

            val todoList = if (todo != null) {
                //Update Edite Note
                Todo(id = todo!!.id, title = title, msg = msg)
            } else {
                //Add Edite Note
                Todo(title = title, msg = msg)
            }

            try {
                if (todo != null) {
                    //Update Todolist database
                    db.todoDao().update(todoList)
                } else {
                    //Add Todolist database
                    db.todoDao().insert(todoList)
                }
                onBackPressedDispatcher.onBackPressed()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.btnDelete.setOnClickListener {
            try {
                if (todo != null) {
                    //Delete
                    var builder = AlertDialog.Builder(this)
                        .setTitle("Delete")
                        .setMessage("Are You Sure Delete From User Data?")
                        .setPositiveButton("Delete",DialogInterface.OnClickListener { dialogInterface, i ->
                            db = Room.databaseBuilder(this,TodoDatabase::class.java,"todolist.db").allowMainThreadQueries().build()
                            var todoDao = db.todoDao()
                            todoDao.deleteRecord(todo!!)
                            Toast.makeText(this, "Delete Note", Toast.LENGTH_SHORT).show()
                            onBackPressedDispatcher.onBackPressed()
                        })
                        .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                        })
                    builder.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}