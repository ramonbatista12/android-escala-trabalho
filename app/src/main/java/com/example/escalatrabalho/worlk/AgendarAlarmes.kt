package com.example.escalatrabalho.worlk

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.escalatrabalho.applicatio.AplicationCuston
import com.example.escalatrabalho.repositorio.RepositorioExecutado
import com.example.escalatrabalho.alarmemanager.BroadcastRacever

val Tag = "AgendarAlarmes"

class AgendarAlarmes(private val c: Context,val p: WorkerParameters):Worker(c,p) {

    @SuppressLint("ScheduleExactAlarm")
    override  fun doWork(): Result {
        var repositio = RepositorioExecutado(AplicationCuston.db.db.dao())
         val alarme =c.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
         val intent=Intent(c,BroadcastRacever::class.java)
         val pendingIntent=PendingIntent.getBroadcast(c,0,intent,PendingIntent.FLAG_IMMUTABLE)
         val horarios= System.currentTimeMillis()+60*1000
        alarme.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,horarios,pendingIntent)
        return Result.success()
    }

}