package com.example.escalatrabalho.serviceAlarme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BroadcastsService : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("brodcastereceiver  service","onreceive service broadcast")
        if(intent?.action!=null){
            when(intent.action){
                BroadcastReceiverMensagems.Para.mensagems->{
                    val intent=Intent(context,ServicoAlarme::class.java).also {
                        it.action=BroadcastReceiverMensagems.Para.mensagems
                        context?.startService(it)
                    }
                }
                BroadcastReceiverMensagems.Soneca.mensagems->{
                    val intent=Intent(context,ServicoAlarme::class.java).also {
                        it.action=BroadcastReceiverMensagems.Soneca.mensagems
                        context?.startService(it)
                    }
                }



            }



        }

        }
}