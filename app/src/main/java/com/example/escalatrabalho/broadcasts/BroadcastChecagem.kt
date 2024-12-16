package com.example.escalatrabalho.broadcasts

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.BroadcastReceiver
import android.util.Log
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.escalatrabalho.applicatio.AplicationCuston
import com.example.escalatrabalho.worlk.AgendarAlarmes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BroadcastChecagem: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val work = PeriodicWorkRequest.Builder(
            AgendarAlarmes::class.java,
            600000,
            java.util.concurrent.TimeUnit.MINUTES).build()
        val context = AplicationCuston.context
        WorkManager.getInstance(context).enqueue(work)
        val alarme =context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
        val intent=Intent(context,BroadcastChecagem::class.java)
        val pendingIntent= PendingIntent.getBroadcast(context,0,intent, PendingIntent.FLAG_IMMUTABLE)
            val horario = System.currentTimeMillis() + 600000*15

            alarme.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,horario,pendingIntent)
            Log.i("alarme","alarme agendadod")


    }
}