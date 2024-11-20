package com.example.escalatrabalho.alarmemanager

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.escalatrabalho.R
import com.example.escalatrabalho.serviceAlarme.ServicoAlarme

 class BroadcastRacever: BroadcastReceiver() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
       val intentService=Intent(context!!.applicationContext, ServicoAlarme::class.java)
       context?.startForegroundService(intentService)
       Log.i("brodcastereceiver","onreceive")

    }





}