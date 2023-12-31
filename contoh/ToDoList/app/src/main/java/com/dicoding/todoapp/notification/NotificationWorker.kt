package com.dicoding.todoapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import com.dicoding.todoapp.ui.detail.DetailTaskActivity
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.NOTIFICATION_CHANNEL_ID
import com.dicoding.todoapp.utils.TASK_ID

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val channelName = inputData.getString(NOTIFICATION_CHANNEL_ID)

    private fun getPendingIntent(task: Task): PendingIntent? {
        val intent = Intent(applicationContext, DetailTaskActivity::class.java).apply {
            putExtra(TASK_ID, task.id)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

    override fun doWork(): Result {
        //TODO 14 : If notification preference on, get nearest active task from repository and show notification with pending intent

        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val notificationEnabled = preferences.getBoolean(
            applicationContext.getString(R.string.pref_key_notify),
            true
        )

        if (!notificationEnabled) {
            return Result.success()
        }

        val taskRepository = TaskRepository.getInstance(applicationContext)
        val nearestActiveTask = taskRepository.getNearestActiveTask()

        if (nearestActiveTask != null) {
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelName,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }

            val notificationIntent = getPendingIntent(nearestActiveTask)
            val notification = channelName?.let {
                val date = DateConverter.convertMillisToString(nearestActiveTask.dueDateMillis)
                val stringDate = applicationContext.getString(R.string.notify_content, date)
                NotificationCompat.Builder(applicationContext, it)
                    .setContentTitle(nearestActiveTask.title)
                    .setContentText(stringDate)
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setContentIntent(notificationIntent)
                    .setAutoCancel(true)
                    .build()
            }
            notificationManager.notify(nearestActiveTask.id, notification)
        }

        return Result.success()
    }

}
