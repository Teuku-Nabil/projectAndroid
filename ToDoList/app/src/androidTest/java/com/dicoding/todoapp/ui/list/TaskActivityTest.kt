package com.dicoding.todoapp.ui.list

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.add.AddTaskActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//TODO 16 : Write UI test to validate when user tap Add Task (+), the AddTaskActivity displayed

@RunWith(AndroidJUnit4::class)
class TaskActivityTest {

    @Before
    fun setUp() {
        ActivityScenario.launch(TaskActivity::class.java)
        init()
    }

    @Test
    fun addTaskActivity() {
        onView(withId(R.id.fab)).perform(click())
        intended(hasComponent(AddTaskActivity::class.java.name))
    }

    @After
    fun tearDown() {
        release()
    }
}