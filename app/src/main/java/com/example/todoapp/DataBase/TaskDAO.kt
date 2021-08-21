package com.example.todoapp.DataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDAO {

    @Insert
    fun insertTask(task:TaskModel):Long

    @Query("SELECT * FROM TaskModel WHERE isFinished!=-1")
    fun getAllTask():LiveData<List<TaskModel>>

    @Query("UPDATE TaskModel SET isFinished=1 WHERE id=:id")
    fun finishTask(id:Long)

    @Query("DELETE FROM TaskModel WHERE id=:id")
    fun deleteTask(id:Long)
}