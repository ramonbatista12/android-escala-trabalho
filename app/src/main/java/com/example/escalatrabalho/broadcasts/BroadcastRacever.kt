package com.example.escalatrabalho.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.escalatrabalho.serviceAlarme.ServicoAlarme

 class BroadcastRacever: BroadcastReceiver() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
       val intentService=Intent(context!!.applicationContext, ServicoAlarme::class.java)
       context?.startForegroundService(intentService)
       Log.i("brodcastereceiver","onreceive")

    }





}