package com.example.escalatrabalho.alarmemanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.impl.utils.ForceStopRunnable.BroadcastReceiver
import com.example.escalatrabalho.worlk.AgendarAlarmes

@SuppressLint("RestrictedApi")
class BroadcastBoot:BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val work = PeriodicWorkRequest.Builder(
            AgendarAlarmes::class.java,
            600000,
            java.util.concurrent.TimeUnit.MINUTES).build()
        WorkManager.getInstance(context).enqueue(work)
        super.onReceive(context, intent)
    }
}