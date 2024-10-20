package com.example.escalatrabalho.viewModel

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.escalatrabalho.classesResultados.ResultadosDatasFolgas
import com.example.escalatrabalho.repositoriodeDatas.RepositorioDatas
import com.example.escalatrabalho.repositoriodeDatas.Resultados
import com.example.escalatrabalho.roomComfigs.DatasFolgas
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.RoomDb
import com.example.escalatrabalho.roomComfigs.repositorio.RepositorioPrincipal
import com.example.escalatrabalho.viewModel.ViewModelTelas
import com.example.escalatrabalho.views.ResultadosSalvarHora
import com.example.escalatrabalho.views.TelaNavegacaoSimples
import com.example.escalatrabalho.worlk.AgendarAlarmes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ViewModelTelas(private val db: RoomDb, private val workManager: WorkManager):ViewModel() {

    var scopo =viewModelScope
    var estadosVm =EstadosAuxVm()
    val reposisitorioDatas =RepositorioDatas()//e a clasee responsavel por criar as datas mostradas no calendario
    val fluxo=reposisitorioDatas.getDatas().map {
        delay(1000)
        Resultados.Ok(it)


    }//o fluxo quente esposto e mapeado para um valor sealed Resultados
    val nomeMes=reposisitorioDatas.getMes()//pega o nome do mes
    val repositoriGeral = RepositorioPrincipal(db)//e a classe responsavel por gerenciar todos os repositorios
    val fluxoDatasFolgas = repositoriGeral.repositorioDatas.select(reposisitorioDatas.mes,reposisitorioDatas.ano).map {
        delay(1000)
        if (it.size==0) ResultadosDatasFolgas.caregando()
       else ResultadosDatasFolgas.ok(it)
    }

    init {

        val mk = PeriodicWorkRequestBuilder<AgendarAlarmes>(10,TimeUnit.MINUTES,5,TimeUnit.MINUTES)
            .addTag("agendamento de alarmes")
        workManager.enqueue(mk.build())

    }

    fun inserirDatasFolgas(datasFolgas: DatasFolgas){
        scopo.launch {
            repositoriGeral.repositorioDatas.insert(datasFolgas)
        }
    }

    fun inserirHorariosDosAlarmes(horariosDosAlarmes: HorioDosAlarmes){
        scopo.launch {
            repositoriGeral.repositorioHorariosDosAlarmes.insert(horariosDosAlarmes)
        }
    }


}

class EstadosAuxVm(){
    var disparaDialogoFerias =mutableStateOf(false)//estado dialog datas de ferias
    var disparaDatass=mutableStateOf(false)//estado dialog datas fiogas
    var transicaoDatPiker =MutableTransitionState(false)//estado transicao animacao Datapiker selecao hr
    var telas=mutableStateOf(TelaNavegacaoSimples.calendario)//estado das telas da navegacao simples
    var transicaoData=MutableTransitionState(false)//estado da transicao anomacao que mostra as datas das folgas
    var transicaoModeloTrabalho= MutableTransitionState(false)//estado transicao Modelo de trabalho
    var transicaoFerias=MutableTransitionState(false) //estado transicao anomacao mostra a parte modelo de trabalho
    var salvandoHorario= mutableStateOf(false)//estado de salvando horario
    var salvandoHorariosResultados= mutableStateOf(ResultadosSalvarHora.clicavel)//estado de salvando horarios
}

class Fabricar(){
    // por ser um projeto peque opitei opo nao usar uma libe de gerenciamento de dependencias
    fun fabricar(bd:RoomDb,workManager: WorkManager)=object : ViewModelProvider.Factory{
        //funcao que recebe um objeto do tipo viewmodelProvider.Factory e retorna um objeto do tipo ViewModelTelas
        //que recebe um objeto do tipo RoomDb e um objeto do tipo WorkManager
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ViewModelTelas(db = bd, workManager = workManager) as T
        }
    }




}