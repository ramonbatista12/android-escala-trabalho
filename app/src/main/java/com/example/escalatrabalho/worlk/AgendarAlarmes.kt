package com.example.escalatrabalho.worlk

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

val Tag = "AgendarAlarmes"

class AgendarAlarmes(private val c: Context,val p: WorkerParameters):Worker(c,p) {

    override  fun doWork(): Result {
        var repositio = RepositorioExecutado(AplicationCuston.db.db.dao())
        val i = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR,22)
            putExtra(AlarmClock.EXTRA_MINUTES, 50)
            putExtra(AlarmClock.EXTRA_MESSAGE, "Alarme de trabalho")
            putExtra(AlarmClock.EXTRA_VIBRATE,true)
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        }

        Log.i(Tag,"Alarme agendado")
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION )
      applicationContext.startActivity(i)

        return Result.success()
    }

}