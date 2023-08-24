package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var detailTaskViewModel: DetailTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action

        val factory = ViewModelFactory.getInstance(this)
        detailTaskViewModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)

        detailTaskViewModel.setTaskId(intent.getIntExtra(TASK_ID, 0))

        detailTaskViewModel.task.observe(this) { task ->
            if (task != null) {
                showDetailTask(task)
            }
        }

        val deleteButton = findViewById<TextView>(R.id.btn_delete_task)
        deleteButton.setOnClickListener {
            detailTaskViewModel.deleteTask()
            finish()
        }

    }

    private fun showDetailTask(task: Task) {
        val title = findViewById<TextView>(R.id.detail_ed_title)
        val description = findViewById<TextView>(R.id.detail_ed_description)
        val date = findViewById<TextView>(R.id.detail_ed_due_date)
        title.text = task.title
        description.text = task.description
        date.text = DateConverter.convertMillisToString(task.dueDateMillis)
    }
}