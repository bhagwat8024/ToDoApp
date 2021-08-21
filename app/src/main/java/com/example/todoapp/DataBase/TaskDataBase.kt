package com.example.todoapp.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskModel::class],version = 1)
abstract class TaskDataBase: RoomDatabase() {
    abstract fun getTaskDao():TaskDAO
}