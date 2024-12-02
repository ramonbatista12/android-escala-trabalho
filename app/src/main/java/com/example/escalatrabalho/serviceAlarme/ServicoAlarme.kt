package com.example.escalatrabalho.serviceAlarme

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.escalatrabalho.ActivityAlarrme
import com.example.escalatrabalho.R
import com.example.escalatrabalho.alarmemanager.BroadcastRacever

class ServicoAlarme:Service() {
    lateinit var notificao: Notification
    final val  canalNome="escala trabalho Service"
    final val  canalId=1
    inner class ServicoAlarmeBinder: Binder(){
        fun getService(): ServicoAlarme=this@ServicoAlarme
    }
    lateinit var ringtone: Ringtone
    private var mensagem=""

    private val binder=ServicoAlarmeBinder()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent?.action!=null){
            when(intent.action) {
             BroadcastReceiverMensagems.Para.mensagems -> {
                 Log.i("servico","parar")
                 mensagem=BroadcastReceiverMensagems.Para.mensagems
                 stopSelf()
             }
             BroadcastReceiverMensagems.Soneca.mensagems -> {
                 Log.i("servico","soneca")
                 mensagem=BroadcastReceiverMensagems.Soneca.mensagems
                 stopSelf()

             }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
    return  binder
    }

    @SuppressLint("ForegroundServiceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {

        Log.i("servico","oncreate")
        super.onCreate()
        registrarCanal()
        criarCanalNotificacao()
        startForeground(canalId,notificao)

        val ringtoneUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone=RingtoneManager.getRingtone(this,ringtoneUri)
        ringtone.isLooping=true
        ringtone.play()


    }
    override fun onDestroy() {
        Log.i("servico","ondestroy")

        if(mensagem==BroadcastReceiverMensagems.Para.mensagems){
          if(ringtone.isPlaying) ringtone.stop()
            val notificationcompat=NotificationManagerCompat.from(this)
            notificationcompat.cancel(canalId)

        }
  else if(mensagem==BroadcastReceiverMensagems.Soneca.mensagems){
            if(ringtone.isPlaying)ringtone.stop()
            val alarme =this.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
            val intent=Intent(this, BroadcastRacever::class.java)
            val pendingIntent=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_IMMUTABLE)
            val horarios= System.currentTimeMillis()+((60*1000)*5)
            alarme.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,horarios,pendingIntent)
            notifirSoneca()
            notificar()
  }
        super.onDestroy()
    }
   fun criarCanalNotificacao(){
       val intentParar = Intent(this,BroadcastsService::class.java).also {
          it.action=BroadcastReceiverMensagems.Para.mensagems

       }

       val intentSoneca = Intent(this,BroadcastsService::class.java).also {
           it.action=BroadcastReceiverMensagems.Soneca.mensagems
       }
       notificao= NotificationCompat.Builder(this ,canalNome)
           .setOngoing(true)
           .setSmallIcon(R.drawable.baseline_access_alarms_24)
           .setContentTitle("escala trabalho")
           .setContentText("Voce vai trabalhar hoje")
           .setPriority(NotificationCompat.PRIORITY_MAX)
           .addAction(
               R.drawable.baseline_access_alarms_24,"parar",
               PendingIntent.getBroadcast(this,
                                      1,
                                                 intentParar,
                                                 PendingIntent.FLAG_IMMUTABLE
                                          )
                      )
           .addAction(
               R.drawable.ic_launcher_foreground,"soneca",
               PendingIntent.getBroadcast(this,
                                      2,
                                                 intentSoneca,
                                                 PendingIntent.FLAG_IMMUTABLE
                                         )
                     )
           .setContentIntent(
               PendingIntent.getActivity(this,
                              0,
                                        Intent(this,
                                               ActivityAlarrme::class.java)
                                        ,PendingIntent.FLAG_IMMUTABLE))
           .setFullScreenIntent(
               PendingIntent.getActivity(this,
                                      0,
                                        Intent(this,
                                               ActivityAlarrme::class.java)
                                        ,PendingIntent.FLAG_IMMUTABLE),true
                                  )
           .setDeleteIntent(PendingIntent.getBroadcast(
                                            this,2,intentSoneca,PendingIntent.FLAG_IMMUTABLE
                                        ))


           .build()
       notificao.flags= notificao.flags or Notification.FLAG_NO_CLEAR
   }

  fun notifirSoneca(){

      notificao= NotificationCompat.Builder(this ,canalNome)
          .setSmallIcon(R.drawable.baseline_access_alarms_24)
          .setContentTitle("escala trabalho")
          .setContentText("O alarme disparara novamente em 5 minutos")
          .setPriority(NotificationCompat.PRIORITY_MAX)
          .build()
  }
    @RequiresApi(Build.VERSION_CODES.O)
   fun registrarCanal(){
       val canal = NotificationChannel(canalNome,
                                  "canal",
                                    NotificationManager.IMPORTANCE_HIGH)
           .apply {
           description="motificao dos alarmes"
                   }
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(canal)
   }
   @SuppressLint("MissingPermission")
   fun notificar(){
       val notificationManage= NotificationManagerCompat.from(this)
       notificationManage.notify(canalId,notificao)
   }

}