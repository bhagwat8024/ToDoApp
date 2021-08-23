package com.example.todoapp

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.DataBase.TaskModel
import kotlinx.android.synthetic.main.task_item.view.*
import java.sql.Types.NULL
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.Inflater

class TaskAdapter(val TaskList: List<TaskModel>): RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder{
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.task_item,
            parent,
            false
        )
        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(TaskList[position])
    }

    override fun getItemCount(): Int  = TaskList.size

    inner class TaskHolder(var itemview:View):RecyclerView.ViewHolder(itemview){
        fun bind(taskModel: TaskModel) {
            with(itemview){
                taskTitletxt.text = taskModel.title.toString()
                taskDesctxt.text = taskModel.description.toString()
                taskCategorytxt.text = taskModel.category.toString()

                updateTime(taskModel.time)
                updateDate(taskModel.date)

            }
        }

        private fun updateDate(date: Long) {
            val format = "EEE ,d mm yyyy"
            val sdf = SimpleDateFormat(format)
            itemview.taskDatetxt.setText(sdf.format(Date(date)))
        }

        private fun updateTime(time: Long) {
            val format = "h:mm a"
            val sdf = SimpleDateFormat(format)
            itemview.taskTimetxt.setText(sdf.format(Date(time)))
        }
    }
}