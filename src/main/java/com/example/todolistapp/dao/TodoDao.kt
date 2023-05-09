package com.example.todolistapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todolistapp.model.Todo

@Dao
interface TodoDao {

    @Insert
    fun insert(todo: Todo)

    @Query("select * from `todo-table`")
    fun getTodoList(): MutableList<Todo>

    @Update
    fun update(todo: Todo)

    @Query("update `todo-table` set id = :todoId, title = :title, msg = :msg")
    fun updateRecord(todoId: Int, title: String, msg: String)

    @Delete
    fun deleteRecord(todo: Todo)

    @Query("select * from `todo-table` where id = :todoId")
    fun getTodo(todoId: Int):Todo
}