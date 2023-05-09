package com.example.todolistapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.databinding.CardListBinding
import com.example.todolistapp.model.Todo

class TodoListAdapter(var context: Context, var todoNoteList: MutableList<Todo>):RecyclerView.Adapter<TodoListAdapter.MyViewHolder>(){
    lateinit var binding: CardListBinding
    lateinit var listener: OnItemClickListener
    class MyViewHolder(var binding: CardListBinding) : RecyclerView.ViewHolder(binding.root)


    interface OnItemClickListener{
        fun onItemClicked(index:Int, todo: Todo)
    }

    fun setFilterList(todolist:MutableList<Todo>){
        this.todoNoteList = todolist
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(updateListener: OnItemClickListener){
        this.listener = updateListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = CardListBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return todoNoteList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Variable TodoList Index position read
        var todoNote = todoNoteList[position]
        holder.binding.tvTitle.text = todoNote.title
        holder.binding.tvNote.text = todoNote.msg

        holder.binding.listCardView.setOnClickListener {
            listener.onItemClicked(position, todoNote)
        }
    }

    fun setItems(todoNoteList: MutableList<Todo>){
        this.todoNoteList = todoNoteList
        notifyDataSetChanged()
    }
}