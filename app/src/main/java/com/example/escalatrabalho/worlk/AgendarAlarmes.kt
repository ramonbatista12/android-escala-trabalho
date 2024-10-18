package com.example.escalatrabalho.worlk

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
val Tag = "AgendarAlarmes"

class AgendarAlarmes(c: Context, p: WorkerParameters): Worker(c,p) {
    override fun doWork(): Result {
        val i = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR,13)
            putExtra(AlarmClock.EXTRA_MINUTES, 58)
            putExtra(AlarmClock.EXTRA_MESSAGE, "Alarme de trabalho")
        }
        Log.i(Tag,"Alarme agendado")
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      applicationContext.startActivity(i)
        return Result.success()
    }

}