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
import com.example.escalatrabalho.roomComfigs.DiasOpcionais
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
            scop.launch {
            registrarCanal()
            criarCanalNotificacao(MensagemNoticacaoWork.MensagemsChecando.mensagem)
            notificar()
            val job = Job()
            val scop = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main + job)
            val repositorio = RepositorioExecutado(AplicationCuston.db.db.dao())
            val repositorioPrincipal =
                RepositorioPrincipal(AplicationCuston.db, AplicationCuston.db.endpoint)
            val modeloDeEscala = repositorioPrincipal.getmodeloObjeto()?: ModeloDeEScala(0,"",false)
            val opcionais=repositorioPrincipal.getOpcionaisObjeto(modeloDeEscala!!.modelo) ?: DiasOpcionais(0,"","")
            val auxiliarDeDecisoes = WorkAuxiliarDecisoes()
            val listaFolgas=repositorioPrincipal.fluxoDatasFolgas.first().map { it.data }
            val diasChecagen=repositorioPrincipal.getDiaAtualEprosimo()?:DiasChecagen(0,com.example.escalatrabalho.repositorio.repositoriodeDatas.SemanaDia.doming,0,com.example.escalatrabalho.repositorio.repositoriodeDatas.SemanaDia.doming)
            val feriados=repositorioPrincipal.fluxoFeriados.first()
            val alarmes =repositorioPrincipal.getObjetosHorariosAlsrme()?:HorioDosAlarmes(0,0,0)
            when (modeloDeEscala.modelo) {
                NomesDeModelosDeEscala.Modelo61.nome -> {
                    Log.i(TAG, "modelo 61")
                    auxiliarDeDecisoes.modelo61(opcionais)
                }

                NomesDeModelosDeEscala.Modelo1236.nome -> {
                    Log.i(TAG, "modelo 1236")
                    auxiliarDeDecisoes.modelo1232(opcionais,
                        listaFolgas,
                        diasChecagen,
                        alarmes, {
                            scop.launch {
                                agendarAlarme(
                                    alarmes.hora,
                                    alarmes.minuto
                                )
                            }

                        })
                }

                NomesDeModelosDeEscala.ModeloSegSex.nome -> {
                    Log.i(TAG, "modelo seg sex")

                    auxiliarDeDecisoes.modeloSegundaSexta(opcionais,
                        feriados,
                        diasChecagen,
                        alarmes, {
                            scop.launch {
                                agendarAlarme(
                                   alarmes.hora,
                                    alarmes.minuto
                                )
                            }

                        })

                }

                else -> {}


            }


            job.cancel()
        }
        job.join()
        job.cancel()
            return Result.success()
        }




        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun modelo1232(diasOpcionais: DiasOpcionais, dia: DiasChecagen, repositorio:RepositorioExecutado, repositorioPrincipal: RepositorioPrincipal){
            val estadoAlarme= MutableStateFlow(HorioDosAlarmes(0,0,0))
            val estadoFolgas = MutableStateFlow(emptyList<Int>())
            GlobalScope.launch {
                repositorioPrincipal.fluxoDatasFolgas.map { it.map { it.data } } .collect{
                    estadoFolgas.value=it
                }
            }
            GlobalScope.launch {
                repositorioPrincipal.fluxoHorariosDosAlarmes.collect{
                    estadoAlarme.value=it?:HorioDosAlarmes(0,0,0)

                }
                when(diasOpcionais.opicional){
                    OpicionalModelo1236.Impar.opcao->{
                        if(dia.dia%2==0){
                            if(!estadoFolgas.value.contains(dia.dia)){

                                agendarAlarme(estadoAlarme.value.hora,estadoAlarme.value.minuto)

                            }

                        }else {
                            val agora = LocalDateTime.now()

                            val horarioAlvo = agora.withHour(estadoAlarme.value.hora)
                                .withMinute(estadoAlarme.value.minuto)
                                .withSecond(0).withNano(0)
                            val horarioLong= horarioAlvo.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            val calendar = Calendar.getInstance()
                            val horalocal =LocalDateTime.now()
                            if(horalocal.isBefore(horarioAlvo)){
                                Log.e("teste ","a hora ja passou")
                            }

                        }
                    }
                    OpicionalModelo1236.Par.opcao->{


                        if(dia.dia%2!=0){


                        }
                        else{

                        }}

                    OpicionalModelo1236.Vasio.opcao->{

                    }
                }
            }
        }
        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun agendarAlarme(hora:Int, minuto:Int){
            val agora = LocalDateTime.now()
            val amanha = agora.plusDays(1)
            val horarioAlvo = amanha.withHour(hora).withMinute(minuto).withSecond(0).withNano(0)
            val horarioLong= horarioAlvo.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            alarme(horarioLong)
        }
        suspend fun alarme(horario:Long){
            val alarme =c.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
            val intent=Intent(c,BroadcastRacever::class.java)
            val pendingIntent=PendingIntent.getBroadcast(c,0,intent,PendingIntent.FLAG_IMMUTABLE)
            withContext(Dispatchers.Main) {
            alarme.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,horario,pendingIntent)
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

