package com.example.escalatrabalho.worlk

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.escalatrabalho.R
import com.example.escalatrabalho.applicatio.AplicationCuston
import com.example.escalatrabalho.repositorio.RepositorioExecutado
import com.example.escalatrabalho.alarmemanager.BroadcastRacever
import com.example.escalatrabalho.enums.MensagemNoticacaoWork
import com.example.escalatrabalho.enums.NomesDeModelosDeEscala
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModelo1236
import com.example.escalatrabalho.repositorio.RepositorioPrincipal
import com.example.escalatrabalho.repositorio.repositoriodeDatas.DiasChecagen
import com.example.escalatrabalho.repositorio.repositoriodeDatas.SemanaDia
import com.example.escalatrabalho.roomComfigs.DiasOpcionais
import com.example.escalatrabalho.roomComfigs.Feriados
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

val Tag = "AgendarAlarmes"

    class AgendarAlarmes(private val c: Context,val p: WorkerParameters):CoroutineWorker(c,p) {
        private final val TAG = "AgendarAlarmes teste "
        lateinit var notificao: android.app.Notification
        private val job= Job()
        private val scop= kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main + job)
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("ScheduleExactAlarm")

        override suspend fun doWork(): Result {
            registrarCanal()
            criarCanalNotificacao(MensagemNoticacaoWork.MensagemsChecando.mensagem)
            notificar()
            val repositorioPrincipal = RepositorioPrincipal(AplicationCuston.db, AplicationCuston.endpoint)
            val repositorioExecutado = RepositorioExecutado(AplicationCuston.db.db.dao())
            val auxiliarDecisoes= WorkAuxiliarDecisoes()

          val _job= scop.launch(Dispatchers.IO) {
               val modeloDeEscala =  repositorioPrincipal.getmodeloObjeto()?: ModeloDeEScala(0,"",false)
               val horioDosAlarmes = repositorioPrincipal.getObjetosHorariosAlsrme()?: HorioDosAlarmes(0,0,0)
               val diasOpcionais = repositorioPrincipal.getOpcionaisObjeto(modeloDeEscala.modelo)?: DiasOpcionais(0,"","")
               val dia = repositorioPrincipal.getDiaAtualEprosimo()?: DiasChecagen(0,SemanaDia.doming,0,SemanaDia.doming)
               val lisat =MutableStateFlow<List<Int>>(emptyList())
               val feriado =MutableStateFlow<List<Feriados>>(emptyList())
               repositorioPrincipal.fluxoDatasFolgas.first{
                   lisat.value=it.map { it.data }
                   true
               }
               repositorioPrincipal.fluxoFeriados.first {
                   feriado.value=it
                   true
               }

               when(modeloDeEscala.modelo){
                   NomesDeModelosDeEscala.Modelo1236.nome-> auxiliarDecisoes.modelo1236(diasOpcionais= diasOpcionais,
                                                                                        diasDeFolgas = lisat.value,
                                                                                        diasChecagen = dia,horioDosAlarmes,
                                                                                        {h,m->
                                                                                        scop.launch {agendarAlarmeProsimoDia(h,m)}
                                                                                         },{h,m->
                                                                                            scop.launch {agendarAlarmeHoje(h,m)}
                                                                                           })
                   NomesDeModelosDeEscala.Modelo61.nome-> auxiliarDecisoes.modelo61(diasOpcionais)

                   NomesDeModelosDeEscala.ModeloSegSex.nome->auxiliarDecisoes.modeloSegundaSexta(diasOpcionais=diasOpcionais,
                                                                                            feriados=feriado.value,
                                                                                            diasChecagen = dia,
                                                                                            horioDosAlarmes,
                                                                                            {h,m->
                                                                                            scop.launch {agendarAlarmeHoje(h,m)}
                                                                                            },
                                                                                            {h,m->
                                                                                             scop.launch { agendarAlarmeProsimoDia(h,m) }
                                                                                            })
                   else->{Log.i("modelo selecionado","modelo nao encontrado")}
               }

           }
            _job.join()
           while (!_job.isCompleted) Log.i("executando work","loop while trabalhando emquanto o job nao for completo")
            return Result.success()
        }





        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun agendarAlarmeProsimoDia (hora:Int, minuto:Int){
            val agora = LocalDateTime.now()
            val amanha = agora.plusDays(1)
            val horarioAlvo = amanha.withHour(hora).withMinute(minuto).withSecond(0).withNano(0)
            val horarioLong= horarioAlvo.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            alarme(horarioLong)
        }
        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun agendarAlarmeHoje (hora:Int, minuto:Int){
            val agora = LocalDateTime.now()
            val horarioAlvo = agora.withHour(hora).withMinute(minuto).withSecond(0).withNano(0)
            val horarioLong= horarioAlvo.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            Log.i("horarioAlvo","${horarioAlvo} alvo long number ${horarioLong}")
            alarme(horarioLong)
        }
        suspend fun alarme(horario:Long){
            val alarme =c.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
            val intent=Intent(c,BroadcastRacever::class.java)
            val pendingIntent=PendingIntent.getBroadcast(c,0,intent,PendingIntent.FLAG_IMMUTABLE)
            withContext(Dispatchers.Main) {

            alarme.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,horario,pendingIntent)
                Log.i("alarme","alarme agendadod")

            }
        }
        fun criarCanalNotificacao(mensagem:String){
             notificao= NotificationCompat.Builder(this.c , "canal")
            .setSmallIcon(R.drawable.baseline_access_alarms_24)
            .setContentTitle("escala trabalho")
            .setContentText(mensagem)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun registrarCanal(){
        val canal = NotificationChannel("canal",
            "escala trabalho",
            NotificationManager.IMPORTANCE_HIGH)
            .apply {
                description="motificao dos alarmes"
            }
        val notificationManager = c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(canal)
    }
    @SuppressLint("MissingPermission")
    fun notificar(){
        val notificationManage= NotificationManagerCompat.from(this.c)
        notificationManage.notify(2,notificao)
    }

}

