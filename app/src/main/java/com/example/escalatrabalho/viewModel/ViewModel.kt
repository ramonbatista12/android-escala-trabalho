package com.example.escalatrabalho.viewModel
//classe view model e reponsavel por transmitir os dados a view separando as responsabilidades
// os dados que o view model emite a view sao diferentes pois a view nao presisa saber todos os detalhes sobre os dados
// e nem sobre a logica de negocia na versdade nem mesmo o view model
//ele e usado como intermediario entre a view e o repositorio foenesendoe formatando dados para a view




import android.annotation.SuppressLint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.escalatrabalho.classesResultados.ResultadosDatasFolgas
import com.example.escalatrabalho.classesResultados.ResultadosSalvarDatasFolgas
import com.example.escalatrabalho.classesResultados.ResultadosSalvarHora
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
import com.example.escalatrabalho.worlk.AgendarAlarmes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ViewModelTelas(private val repositorio: RepositorioPrincipal, private val workManager: WorkManager):ViewModel() {
    var trabalhado = arrayOf("tab", "folg", "cp")
    var scopo = viewModelScope         //escopo de corotina
    var estadosVm = EstadosAuxVm()    //os estados estao emcapisulsulados por essa classe


    //responsavel por emitir o fluxo das datas ds folgas
    val fluxoDatasFolgas = repositorio.fluxoDatasFolgas.map {
        ResultadosDatasFolgas.ok(it)
    }
    val estadosModeloTrabalho1236 = repositorio.fluxoModeloDeTrabalho1236.map {
        mdcheck(it.id, it.check)
    }.stateIn(
        scope = scopo,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = mdcheck(1, false)
    )

    //responsavel por emitir o fluxo com check value do modelo de trabalho 12/36
    val estadosModeloDeTrabalho61 = repositorio.fluxoModeloDeTrabalho61.map {
        mdcheck(it.id, it.check)
    }.stateIn(
        scope = scopo,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = mdcheck(2, false)
    )//responsavel por emitir o fluxo com check value do modelo de trabalho 6/1
    val estadoModloTrabalhoSegsext = repositorio.fluxoModeloDeTrabalhoSegsext.map {
        mdcheck(it.id, it.check)
    }.stateIn(
        scope = scopo,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = mdcheck(3, false)
    )//responsavel por emitir o fluxo com check value do modelo de trabalho seg-sext
    val fluxoDatas = repositorio.fluxoDasDatas//o fluxo que emite as datas do mes
    val fluxoViewCalendario = repositorio.fluxoDatasTrabalhado.stateIn(
        scope = scopo,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val fluxoDiasOpcionais = repositorio.fluxoOpcionais.stateIn(
        scope = scopo,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = DiasOpcionais(id=0,"", OpicionalModelo1236.Vasio.opcao)
    )

    //fluxo criado com combine me permite checar as outras variaveis para criar o fluxo que vai esiberir no calendario
    val nomeMes = repositorio.nomeDomes// nome do mes
    val fluxoFeriados = repositorio.fluxoFeriados
        .stateIn(
            scope = scopo,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val hostState = SnackbarHostState()//responsavel por mostrar snackbar)

    init {
        scopo.launch(context = Dispatchers.IO) {  // joga o agendamento para trhead de defaut usada para cauculos pesados
            val mk = PeriodicWorkRequestBuilder<AgendarAlarmes>(
                10,
                TimeUnit.MINUTES,
                5,
                TimeUnit.MINUTES
            )
                .addTag("agendamento de alarmes")
            workManager.enqueue(mk.build())


        }

    }


    //sao responsaveis por inserir e deletar as datas de folgas
    fun inserirDatasFolgas(datasFolgas: DatasFolgas) {
        scopo.launch(context = Dispatchers.IO) {
            repositorio.inserirDatasdefolgas(datasFolgas)
                                               }     }

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
               "12/36"-> DiasOpcionais(1,"12/36",diasOpcionais)
               "6/1"->  DiasOpcionais(2,"6/1",diasOpcionais)
               "seg-sext"-> DiasOpcionais(3,"seg-sext",diasOpcionais)
               else -> DiasOpcionais(0,"", OpicionalModelo1236.Vasio.opcao)
           }
            scopo.launch(context = Dispatchers.IO) {
                repositorio.inserirOpcionais(opcional)
            }
        }




}//fim vm



class EstadosAuxVm(){//classe criada para manter os estados do viewmodel
    var disparaDialogoFerias =mutableStateOf(false)                                     //estado dialog datas de ferias
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