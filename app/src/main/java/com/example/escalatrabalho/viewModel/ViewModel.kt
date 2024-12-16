package com.example.escalatrabalho.viewModel
//classe view model e reponsavel por transmitir os dados a view separando as responsabilidades
// os dados que o view model emite a view sao diferentes pois a view nao presisa saber todos os detalhes sobre os dados
// e nem sobre a logica de negocia na versdade nem mesmo o view model
//ele e usado como intermediario entre a view e o repositorio foenesendoe formatando dados para a view




import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.escalatrabalho.broadcasts.BroadcastRacever
import com.example.escalatrabalho.applicatio.AplicationCuston
import com.example.escalatrabalho.classesResultados.ResultadosDatasFolgas
import com.example.escalatrabalho.classesResultados.ResultadosSalvarDatasFolgas
import com.example.escalatrabalho.classesResultados.ResultadosSalvarHora
import com.example.escalatrabalho.classesResultados.ResultadosVisualizacaoDatas
import com.example.escalatrabalho.enums.IdsDeModelosDeEscala
import com.example.escalatrabalho.enums.NomesDeModelosDeEscala
import com.example.escalatrabalho.roomComfigs.DatasFolgas
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import com.example.escalatrabalho.roomComfigs.RoomDb
import com.example.escalatrabalho.repositorio.RepositorioPrincipal
import com.example.escalatrabalho.retrofit.CalendarioApi
import com.example.escalatrabalho.viewModel.modelosParaView.mdcheck

import com.example.escalatrabalho.enums.TelaNavegacaoSimples
import com.example.escalatrabalho.enums.TelaNavegacaoSinplesAlturaCompacta
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModelo1236
import com.example.escalatrabalho.roomComfigs.DiasOpcionais
import com.example.escalatrabalho.viewModel.modelosParaView.FeriasView
import com.example.escalatrabalho.viewModel.modelosParaView.HorarioView
import com.example.escalatrabalho.worlk.AgendarAlarmes
import com.example.escalatrabalho.worlk.ChecartrocaDeDeEscala
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
class ViewModelTelas(private val repositorio: RepositorioPrincipal, private val workManager: WorkManager):ViewModel() {

    var scopo = viewModelScope         //escopo de corotina
    var estadosVm = EstadosAuxVm()    //os estados estao emcapisulsulados por essa classe


    //responsavel por emitir o fluxo das datas ds folgas
    val fluxoDatasFolgas = repositorio.fluxoDatasFolgas.map {
        ResultadosDatasFolgas.ok(it)
    }
    //emite o fluxo com check value do modelo de trabalho 12/36
    val estadosModeloTrabalho1236 = repositorio.fluxoModeloDeTrabalho1236.map {
        mdcheck(it.id, it.check)
    }.stateIn(
        scope = scopo,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = mdcheck(1, false)
             )

    //responsavel por emitir o fluxo com check value do modelo de trabalho 6/1
    val estadosModeloDeTrabalho61 = repositorio.fluxoModeloDeTrabalho61.map {
        mdcheck(it.id, it.check)
    }.stateIn(
        scope = scopo,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = mdcheck(2, false)
              )//responsavel por emitir o fluxo com check value do modelo de trabalho seg-sexta
    val estadoModloTrabalhoSegsext = repositorio.fluxoModeloDeTrabalhoSegsext.map {
        mdcheck(it.id, it.check)
    }.stateIn(
        scope = scopo,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = mdcheck(3, false)
             )

    val fluxoViewCalendario = repositorio.fluxoDatasTrabalhado.map {
        if(it.isEmpty()) ResultadosVisualizacaoDatas.caregando
        else ResultadosVisualizacaoDatas.Ok(it)
    }
        .stateIn(
        scope = scopo,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = ResultadosVisualizacaoDatas.caregando
               )
    val fluxoDiasOpcionais = repositorio.fluxoOpcionais
        .stateIn(
        scope = scopo,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = DiasOpcionais(id=0,"", OpicionalModelo1236.Vasio.opcao)
                  )
    val nomeMes = repositorio.nomeDomes// nome do mes
    val numeroMes = repositorio.numeroMes//numero do mes
    val fluxoFeriados = repositorio.fluxoFeriados
        .stateIn(
            scope = scopo,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
               )
    val fluxoFeriass = repositorio.fluxoDeferias
        .stateIn(
        scope = scopo,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = FeriasView(0,0,0,0,0,0,0,false)
               )
    val fluxoHorariosDosAlarmes = repositorio.fluxoHorariosDosAlarmes.map {
        if(it ==null) HorarioView(" -- ", " -- ")
        else HorarioView("${if(it.hora<10) "0" else ""}${it.hora}","${if(it.minuto<10) "0" else ""}${it.minuto}")
    }.stateIn(
        scope = scopo,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
       HorarioView(" -- "," -- ")
    )
    val hostState = SnackbarHostState()//responsavel por mostrar snackbar)

    init {
        scopo.launch(context = Dispatchers.Default) {  // joga o agendamento para trhead de defaut usada para cauculos pesados
            val mk = PeriodicWorkRequestBuilder<AgendarAlarmes>(
                15,
                TimeUnit.MINUTES,
                5,
                TimeUnit.MINUTES
            )
                .addTag("agendamento de alarmes")
            workManager.enqueueUniquePeriodicWork("agendamento de alarmes",
                ExistingPeriodicWorkPolicy.UPDATE,mk.build())


            val timer =LocalDateTime.now()
            val timer2=timer.plusDays(1)
            timer2.withHour(0).withMinute(0)
            val deley=timer2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val duracao=Duration.ofNanos(deley)
            val workchecagem= PeriodicWorkRequestBuilder<ChecartrocaDeDeEscala>(1,TimeUnit.DAYS)
                                                                                .setInitialDelay(duracao)
                                                                                .addTag("checagem de horario")
                                                                                 .build()
             workManager.enqueueUniquePeriodicWork("checagem de horario",ExistingPeriodicWorkPolicy.UPDATE, workchecagem)


        }

    }


    //sao responsaveis por inserir e deletar as datas de folgas
    fun inserirDatasFolgas(datasFolgas: DatasFolgas) {
        scopo.launch(context = Dispatchers.IO) {
            repositorio.inserirDatasdefolgas(datasFolgas)
                                               }
            .invokeOnCompletion { estadosVm.disparaDatass.value=false }


    }

        //responsavel por deletar as datas de folgas                                                }
    fun deletarDatasFolgas(datasFolgas: DatasFolgas) {
            scopo.launch(context = Dispatchers.IO) {
                repositorio.apargarDataDasFolgas(datasFolgas)
                                                    }
                                                       }

   //vai inserir o horario atraves do metodo inserte criado pelo room
    fun inserirHorariosDosAlarmes(
            horariosDosAlarmes: HorioDosAlarmes,
            calbakSnackbar: suspend (String) -> Unit
        ) {

            estadosVm.salvandoHorariosResultados.value =
                ResultadosSalvarHora.salvando//estado de salvando horario
            scopo.launch(Dispatchers.IO) {
                repositorio.inserirHorariosDosAlarmes(horariosDosAlarmes)
            }
                .invokeOnCompletion {
                    scopo.launch {
                        estadosVm.salvandoHorariosResultados.value = ResultadosSalvarHora.comcluido
                        calbakSnackbar("Salvo com sucesso")
                        delay(1000)
                        estadosVm.salvandoHorariosResultados.value = ResultadosSalvarHora.clicavel

                    }
                    scopo.launch(Dispatchers.Default) {
                       apagarAlarmesEreagendar()
                    }
                }
        }

        @SuppressLint("SuspiciousIndentation")
        //vai inserir o modelo de trabalho se estiver vasia se nao vai atualizar
        fun inserirModeloDeTrabalho(modeloDeTrabalho: ModeloDeEScala) {
            scopo.launch(context = Dispatchers.IO) {
                repositorio.inserirModeloDeTrabalho(modeloDeTrabalho)

            }
        }


        fun inserirOpcionalModelo(modelo:String,diasOpcionais:String){
           val opcional =  when(modelo){
               NomesDeModelosDeEscala.Modelo1236.nome-> DiasOpcionais(id=IdsDeModelosDeEscala.IdModelo1236.id,
                                                                     NomesDeModelosDeEscala.Modelo1236.nome,
                                                                     diasOpcionais)
               NomesDeModelosDeEscala.Modelo61.nome->  DiasOpcionais(id=IdsDeModelosDeEscala.IdModelo61.id,
                                                                     NomesDeModelosDeEscala.Modelo61.nome,
                                                                     diasOpcionais)
               NomesDeModelosDeEscala.ModeloSegSex.nome-> DiasOpcionais(id=IdsDeModelosDeEscala.IdModeloSegSex.id,
                                                                       NomesDeModelosDeEscala.ModeloSegSex.nome,
                                                                       diasOpcionais)
               else -> DiasOpcionais(0,"", OpicionalModelo1236.Vasio.opcao)
           }
            scopo.launch(context = Dispatchers.IO) {
                repositorio.inserirOpcionais(opcional)
            }
        }

    fun apagarFerias(){
        scopo.launch(context = Dispatchers.IO) {
            repositorio.apagarFerias()
        }

                                         }


    fun inserirFerias(ferias: FeriasView){
        scopo.launch(context = Dispatchers.IO) {
            repositorio.inserirFerias(ferias)
        }.invokeOnCompletion {
            estadosVm.disparaDialogoFerias.value=false
        }
    }


    private suspend fun apagarAlarmesEreagendar(){
        val applicationContext =AplicationCuston.context
        val alarme =applicationContext.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
        val intent= Intent(applicationContext, BroadcastRacever::class.java)
        val pendingIntent=PendingIntent.getBroadcast(applicationContext,0,intent, PendingIntent.FLAG_IMMUTABLE)

        if(pendingIntent!=null)
           alarme.cancel(pendingIntent)
        val onitime = OneTimeWorkRequestBuilder<AgendarAlarmes>().build()
        val mk = workManager.enqueue(onitime)

    }


}//fim vm



class EstadosAuxVm(){//classe criada para manter os estados do viewmodel
    var disparaDialogoFerias =mutableStateOf(false)                                     //estado dialog datas de Ferias
    var disparaDatass=mutableStateOf(false)                                             //estado dialog datas fiogas
    var transicaoDatPiker =MutableTransitionState(true)                             //estado transicao animacao Datapiker selecao hr
    var telas=mutableStateOf(TelaNavegacaoSimples.calendario)                                //estado das telas da navegacao simples
    var telasAlturaCompacta=mutableStateOf(TelaNavegacaoSinplesAlturaCompacta.calendario)    //estado das telas da navegacao simples
    var transicaoData=MutableTransitionState(false)                                //estado da transicao anomacao que mostra as datas das folgas
    var transicaoModeloTrabalho= MutableTransitionState(false)                     //estado transicao Modelo de trabalho
    var transicaoFerias=MutableTransitionState(false)                              //estado transicao anomacao mostra a parte modelo de trabalho
    var salvandoHorario= mutableStateOf(false)                                        //estado de salvando horario
    var salvandoHorariosResultados= mutableStateOf(ResultadosSalvarHora.clicavel)           //estado de salvando horarios
    var resultadosSalvarDatas=mutableStateOf(ResultadosSalvarDatasFolgas.clicavel)          //estado de salvando datas de folgasSalvarDatasFolgas)//estado de salvando datas de folgas
}

class Fabricar() {
    // por ser um projeto peque opitei opo nao usar uma libe de gerenciamento de dependencias
    fun fabricar(
        bd: RoomDb,
        calenderios: CalendarioApi,
        workManager: WorkManager
    ) = object : ViewModelProvider.Factory {
        //funcao que recebe um objeto do tipo viewmodelProvider.Factory e retorna um objeto do tipo ViewModelTelas
        //que recebe um objeto do tipo RoomDb e um objeto do tipo WorkManager
        @RequiresApi(Build.VERSION_CODES.O)
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ViewModelTelas(repositorio = RepositorioPrincipal(
                                  bd = bd,
                                  datasFeriados = calenderios),
                                  workManager = workManager) as T
        }
    }


}

/*
* class RepositorioPrincipal(val bd: RoomDb, val datasFeriados: CalendarioApiService.CalendarioApi) {
    // O código do seu repositório
    suspend fun getDatasFeriados() {
        val response = datasFeriados.getDatas(2024)
        // Lide com a resposta
    }
}

* */