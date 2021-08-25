package com.example.todoapp

import android.content.ClipData
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Query
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
            layoutManager = GridLayoutManager(this@MainActivity,2,GridLayoutManager.VERTICAL,false)
            adapter = this@MainActivity.adapter
        }

        db.getTaskDao().getAllTask().observe(this,Observer{
            if(!it.isNullOrEmpty()){
                TaskList.clear()
                TaskList.addAll(it)
                adapter.notifyDataSetChanged()
            }
            else{
                TaskList.clear()
                adapter.notifyDataSetChanged()
            }
        })
        initSwipe()
    }

    private fun initSwipe() {
       val simpleItemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0,
           ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
           override fun onMove(
               recyclerView: RecyclerView,
               viewHolder: RecyclerView.ViewHolder,
               target: RecyclerView.ViewHolder
           ): Boolean  = false

           override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val position = viewHolder.adapterPosition
               if(direction==ItemTouchHelper.LEFT){
                   db.getTaskDao().deleteTask(adapter.getItemId(position))
               }
               else if(direction==ItemTouchHelper.RIGHT){
                   db.getTaskDao().finishTask(adapter.getItemId(position))
               }
           }

/*
           override fun onChildDraw(
               canvas: Canvas,
               recyclerView: RecyclerView,
               viewHolder: RecyclerView.ViewHolder,
               dX: Float,
               dY: Float,
               actionState: Int,
               isCurrentlyActive: Boolean
           ) {
               if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                   val itemView = viewHolder.itemView

                   val paint = Paint()
                   var icon :Bitmap

                   if (dX > 0) {
                        icon  =BitmapFactory.decodeResource(resources,R.mipmap.check)
                       paint.color = Color.parseColor("#388E3C")

                       canvas.drawRect(
                           itemView.left.toFloat(), itemView.top.toFloat(),
                           itemView.left.toFloat() + dX, itemView.bottom.toFloat(), paint
                       )

                       canvas.drawBitmap(
                           icon,
                           itemView.left.toFloat(),
                           itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height.toFloat()) / 2,
                           paint
                       )
                   } else {
                        icon = BitmapFactory.decodeResource(resources,R.mipmap.delete)
                       paint.color = Color.parseColor("#D32F2F")

                       canvas.drawRect(
                           itemView.right.toFloat() + dX, itemView.top.toFloat(),
                           itemView.right.toFloat(), itemView.bottom.toFloat(), paint
                       )

                       canvas.drawBitmap(
                           icon,
                           itemView.right.toFloat() - icon.width,
                           itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height.toFloat()) / 2,
                           paint
                       )
                   }
                   viewHolder.itemView.translationX = dX

               }
               else{
                   super.onChildDraw(
                       canvas,
                       recyclerView,
                       viewHolder,
                       dX,
                       dY,
                       actionState,
                       isCurrentlyActive
                   )
               }
           }
 */
       }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(ToDoRV)
    }

    fun goToTaskActivity(view:View){
        startActivity(Intent(this,TaskActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val item = menu!!.findItem(R.id.search)
        val searchView = item.actionView as androidx.appcompat.widget.SearchView

        item.setOnActionExpandListener(object:MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean{
                displayTask()
                return true
            }
            override fun onMenuItemActionCollapse(item: MenuItem?):Boolean{
                displayTask()
                return true
            }
        })
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(!newText.isNullOrEmpty()){
                    displayTask(newText)
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun displayTask(query: String = ""){
            db.getTaskDao().getAllTask().observe(this, Observer {
                if(!it.isNullOrEmpty()){
                    TaskList.clear()
                    TaskList.addAll(
                        it.filter {task->
                            task.title.contains(query,true)
                        }
                    )
                    adapter.notifyDataSetChanged()
                }
            })
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