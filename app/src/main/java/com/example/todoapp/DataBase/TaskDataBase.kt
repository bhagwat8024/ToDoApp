package com.example.todoapp.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.DB_NAME
import java.util.concurrent.locks.Lock

@Database(entities = [TaskModel::class],version = 1)
abstract class TaskDataBase: RoomDatabase() {
    abstract fun getTaskDao():TaskDAO
    companion object{
        private var Instance:TaskDataBase? = null
        fun getDatabase(context: Context):TaskDataBase{
            var tempInstance = Instance
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                tempInstance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDataBase::class.java,
                    DB_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            Instance = tempInstance
            return Instance!!
        }
    }
}