package com.example.todolistapp

import android.R
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.Room
import com.example.todolistapp.adapter.TodoListAdapter
import com.example.todolistapp.database.TodoDatabase
import com.example.todolistapp.databinding.ActivityHomeBinding
import com.example.todolistapp.model.Todo
import java.util.Locale


class HomeActivity : AppCompatActivity() {
    //Important variable Available
    lateinit var binding: ActivityHomeBinding
    lateinit var todoAdapter: TodoListAdapter
    var todoNoteList = mutableListOf<Todo>()
    private lateinit var db: TodoDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Animation

        // Get the background, which has been compiled to an AnimationDrawable object
        val animation = binding.mainLayout.background as AnimationDrawable

        // Start the animation (looped playback by default).
        animation.setEnterFadeDuration(2500)
        // Start the animation (looped playback by default).
        animation.setExitFadeDuration(5000)
        animation.start()


        //Search
        binding.searchBar.clearFocus()
        binding.searchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               filterList(newText)
                return true
            }

        })


        //Room database file create
        db = Room.databaseBuilder(this, TodoDatabase::class.java, "todolist.db")
            .allowMainThreadQueries().build()

        //Add Note Btn
        binding.addNote.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        //Adapter Set List View
        todoAdapter = TodoListAdapter(this, todoNoteList)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        binding.recyclerView.adapter = todoAdapter

        //Adapter Data Parsing Home to Add Note Put data
        todoAdapter.setOnItemClickListener(object : TodoListAdapter.OnItemClickListener {
            override fun onItemClicked(index: Int, todo: Todo) {
                var intent = Intent(applicationContext, AddNoteActivity::class.java)
                intent.putExtra("TODOLIST", todo)
                startActivity(intent)
            }
        })

    }

    private fun filterList(newText: String?) {
        if (newText!=null){
            var filter = ArrayList<Todo>()
            for(i in todoNoteList){
                if (i.title.lowercase(Locale.ROOT).contains(newText) || i.msg.lowercase(Locale.ROOT).contains(newText)){
                    filter.add(i)
                }
            }
            if (filter.isEmpty()){
                Toast.makeText(this, "Not Found!", Toast.LENGTH_SHORT).show()
            }
            else{
                todoAdapter.setFilterList(filter)
            }
        }
    }

    private fun updateList() {
        todoNoteList = db.todoDao().getTodoList()
        todoAdapter.setItems(todoNoteList)
    }

    override fun onResume() {
        super.onResume()
        if (db != null) {
            updateList()
        }
    }
}