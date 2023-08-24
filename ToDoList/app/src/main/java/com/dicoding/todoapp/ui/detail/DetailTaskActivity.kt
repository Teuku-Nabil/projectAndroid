package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        val detailTaskViewModel: DetailTaskViewModel
        val factory = ViewModelFactory.getInstance(this)
        detailTaskViewModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)

            detailTaskViewModel.apply {
                setTaskId(taskId)
                task.observe(this@DetailTaskActivity) { mTask ->
                    if (mTask == null) finish()
                    else {
                        findViewById<TextInputEditText>(R.id.detail_ed_title).setText(mTask.title)
                        findViewById<TextInputEditText>(R.id.detail_ed_description).setText(mTask.description)
                        findViewById<TextInputEditText>(R.id.detail_ed_due_date).setText(
                            DateConverter.convertMillisToString(
                                mTask.dueDateMillis
                            )
                        )

                        findViewById<Button>(R.id.btn_delete_task).setOnClickListener {
                            deleteTask()
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.delete_task),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
                }
            }
        }
    }
}