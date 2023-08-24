package com.dicoding.todoapp.ui.add

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.TaskRepository
import com.dicoding.todoapp.ui.list.TaskActivity
import com.dicoding.todoapp.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var viewModel: AddTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        supportActionBar?.title = getString(R.string.add_task)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                //TODO 12 : Create AddTaskViewModel and insert new task to database

                val title = findViewById<TextView>(R.id.add_ed_title).text.toString()
                val description = findViewById<TextView>(R.id.add_ed_description).text.toString()
                val due = findViewById<TextView>(R.id.add_tv_due_date).text.toString()

                viewModel = AddTaskViewModel(TaskRepository.getInstance(this))
                if (title.isEmpty() || description.isEmpty() || due.isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.add_task_fail),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                } else {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = dateFormat.parse(due)
                    val dueDateMillis = date?.time ?: System.currentTimeMillis()

                    viewModel.newTask(title, description, dueDateMillis)
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.add_task_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }
}