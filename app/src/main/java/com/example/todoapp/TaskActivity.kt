package com.example.todoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ReceiverCallNotAllowedException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.room.Room
import com.example.todoapp.DataBase.TaskDataBase
import com.example.todoapp.DataBase.TaskModel
import kotlinx.android.synthetic.main.activity_task.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import java.util.Timer

const val DB_NAME = "ToDo.db"
class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalendar: Calendar
    lateinit var datePickerListener: DatePickerDialog.OnDateSetListener
    lateinit var timePickerListener: TimePickerDialog.OnTimeSetListener
    val labels = arrayListOf("Personal","Professional","Shopping","Budget")

    val db by lazy {
        Room.databaseBuilder(this,TaskDataBase::class.java, DB_NAME)
            .allowMainThreadQueries()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        dateEdit.setOnClickListener(this)
        timeEdit.setOnClickListener(this)
        saveTask.setOnClickListener(this)
        setUpSpinner()

    }

    private fun setUpSpinner() {
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,labels)
        labels.sort()
        labelSpinner.adapter = adapter
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.dateEdit->{
                setlistener()
            }
            R.id.timeEdit->{
                setTimeListener()
            }
            R.id.saveTask->{
                saveTask()
            }
        }
    }

    private fun saveTask() {
        val title = titleEdit.text.toString()
        val taskDesc = taskDesc.text.toString()
        val taskCategory = labelSpinner.selectedItem.toString()
    }

    private fun setTimeListener() {

        timePickerListener = TimePickerDialog.OnTimeSetListener{ timePicker: TimePicker, hour: Int, minute: Int ->
            myCalendar.set(Calendar.HOUR_OF_DAY,hour)
            myCalendar.set(Calendar.MINUTE,minute)
            updateTime()
        }
        val timePickerDialog = TimePickerDialog(
            this,
            timePickerListener,
            myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE),
            false
        )

        timePickerDialog.show()
    }

    private fun setlistener() {
        myCalendar = Calendar.getInstance()
        datePickerListener = DatePickerDialog.OnDateSetListener{ datePicker: DatePicker, year: Int, month: Int, day: Int ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,day)
            updateDate()
        }

        val datePickerDialog = DatePickerDialog(
            this,datePickerListener,myCalendar.get(Calendar.YEAR)
        ,myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        val MyFormat = "EEE,d MMM YYYY"
        val sdf = SimpleDateFormat(MyFormat)
        dateEdit.setText(sdf.format(myCalendar.time))
        timeInput.visibility = View.VISIBLE
    }
    private fun updateTime(){
        val MyFormat = "h:mm a"
        val sdf = SimpleDateFormat(MyFormat)
        timeEdit.setText(sdf.format(myCalendar.time))
    }

}