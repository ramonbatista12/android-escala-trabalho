package com.example.escalatrabalho.intentes

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import com.example.escalatrabalho.alarmemanager.BroadcastRacever
import kotlinx.coroutines.Job
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class IntenteAlarmes(var c:Context?){
    final val  RequestCod=0
    private var intent: Intent?= Intent(c,BroadcastRacever::class.java)
    private val  job = Job()
    private val scope = CoroutineScope(kotlinx.coroutines.Dispatchers.Main + job)

    private fun checarAlarme():ResultadoIntentes{
        val alarme =c?.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
        val pendingIntent=PendingIntent.getBroadcast(c,
                                                     RequestCod,
                                                     intent!!,
                                                     PendingIntent.FLAG_IMMUTABLE or  PendingIntent.FLAG_NO_CREATE)

        when{
            alarme!=null && pendingIntent!=null ->{ return ResultadoIntentes.criada }
            else ->{return ResultadoIntentes.naocriada}

        }
    }

    fun agendarAlarme(horario:Long){
        cancelarAlarme()
        val alarme =c?.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
        val pendingIntent=PendingIntent.getBroadcast(c,
                                                     RequestCod,
                                                     intent!!,
                                                     PendingIntent.FLAG_IMMUTABLE)
        alarme.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,horario,pendingIntent)

    }

    fun cancelarAlarme(){

        when(checarAlarme()){
            ResultadoIntentes.criada->{
                val alarme =c?.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
                val pendingIntent=PendingIntent.getBroadcast(c,
                                                             RequestCod,
                                                             intent!!,
                                                             PendingIntent.FLAG_IMMUTABLE or  PendingIntent.FLAG_NO_CREATE)
                alarme.cancel(pendingIntent)

            }
            ResultadoIntentes.naocriada->{}
            else ->{}

        }


    }

    //funcao de cancelar tarefas e cancelar a intente
    fun finalizar(){
        c=null
        intent =null
        job.cancel()
    }


}

enum class ResultadoIntentes(){
   criada,
   naocriada

}