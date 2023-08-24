package com.dicoding.courseschedule.ui.home

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.junit.Assert.*
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.add.AddCourseActivity
import org.junit.Rule
import org.junit.Test

class HomeActivityTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun testAddCourseActivity() {
        onView(withId(R.id.action_add)).check(matches(isDisplayed())).perform(click())

        val addCourseCheck = getAddCourseActivity()
        assertTrue(addCourseCheck?.javaClass == AddCourseActivity::class.java)
    }

    private fun getAddCourseActivity(): Activity? {
        var currentCourseActivity: Activity? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            run {
                currentCourseActivity = ActivityLifecycleMonitorRegistry.getInstance()
                    .getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0)
            }
        }
        return currentCourseActivity
    }
}