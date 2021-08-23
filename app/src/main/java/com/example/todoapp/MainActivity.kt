package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.todoapp.DataBase.TaskDataBase
import com.example.todoapp.DataBase.TaskModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var TaskList = arrayListOf<TaskModel>()
    var adapter = TaskAdapter(TaskList)

    val db by lazy {
        TaskDataBase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        ToDoRV.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapter
        }

        db.getTaskDao().getAllTask().observe(this,Observer{
            if(!it.isNullOrEmpty()){
                TaskList.clear()
                TaskList.addAll(it)
                adapter.notifyDataSetChanged()
                Toast.makeText(this,TaskList.size.toString(),Toast.LENGTH_LONG).show()
            }
            else{
                TaskList.clear()
                Toast.makeText(this,"0",Toast.LENGTH_LONG).show()
                adapter.notifyDataSetChanged()
            }
        })
        Toast.makeText(this,db.hashCode().toString(),Toast.LENGTH_LONG).show()
    }

    fun goToTaskActivity(view:View){
        startActivity(Intent(this,TaskActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if(itemId==R.id.history){
            startActivity(Intent(baseContext,HistoryActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}