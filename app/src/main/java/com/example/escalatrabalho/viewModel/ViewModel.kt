package com.example.escalatrabalho.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.escalatrabalho.applicatio.AplicationCuston
import com.example.escalatrabalho.calendarComtrato.Calendario
import com.example.escalatrabalho.classesResultados.ResultadosDatasFolgas
import com.example.escalatrabalho.repositoriodeDatas.RepositorioDatas
import com.example.escalatrabalho.classesResultados.Resultados
import com.example.escalatrabalho.classesResultados.ResultadosSalvarDatasFolgas
import com.example.escalatrabalho.roomComfigs.DatasFolgas
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import com.example.escalatrabalho.roomComfigs.RoomDb
import com.example.escalatrabalho.roomComfigs.repositorio.RepositorioPrincipal
import com.example.escalatrabalho.views.ResultadosSalvarHora
import com.example.escalatrabalho.views.TelaNavegacaoSimples
import com.example.escalatrabalho.worlk.AgendarAlarmes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class ViewModelTelas(private val db: RoomDb, private val workManager: WorkManager):ViewModel() {
    var trabalhado = arrayOf("tab","folg")
    var scopo =viewModelScope
    var estadosVm =EstadosAuxVm()//os estados estao emcapisulsulados por essa classe
    val reposisitorioDatas =RepositorioDatas()//e a clasee responsavel por criar as datas mostradas no calendario
    val nomeMes=reposisitorioDatas.getMes()//pega o nome do mes
    val repositoriGeral = RepositorioPrincipal(db)//e a classe responsavel por gerenciar todos os repositorios
    val fluxoDatasFolgas = repositoriGeral.repositorioDatas.select(reposisitorioDatas.mes,reposisitorioDatas.ano).map {
        ResultadosDatasFolgas.ok(it)
    }//responsavel por emitir o fluxo das datas ds folgas
    val estadosModeloTrabalho1236= repositoriGeral.repositorioModeloDeTrabalho.select("12/36").map {
        if(it.isEmpty())mdcheck(id = 1  ,false)
        else mdcheck(it.get(0).id,it.get(0).check)
    }//responsavel por emitir o fluxo com check value do modelo de trabalho 12/36
    val estadosModeloDeTrabalho61=repositoriGeral.repositorioModeloDeTrabalho.select("6/1").map {
        if(it.isEmpty())mdcheck(2,false)
        else mdcheck(it.get(0).id,it.get(0).check)
    }//responsavel por emitir o fluxo com check value do modelo de trabalho 6/1
    val estadoModloTrabalhoSegsext=repositoriGeral.repositorioModeloDeTrabalho.select("seg-sext").map {
        if(it.isEmpty())mdcheck(3,false)
        else mdcheck(it.get(0).id,it.get(0).check)
    }//responsavel por emitir o fluxo com check value do modelo de trabalho seg-sext
    val fluxo=reposisitorioDatas.getDatas().map { Resultados.Ok(it) }//o fluxo quente esposto e mapeado para um valor sealed Resultados



    init {
      scopo.launch(context =  Dispatchers.IO) {  // joga o agendamento para trhead de defaut usada para cauculos pesados
        val mk = PeriodicWorkRequestBuilder<AgendarAlarmes>(10,TimeUnit.MINUTES,5,TimeUnit.MINUTES)
            .addTag("agendamento de alarmes")
        workManager.enqueue(mk.build())

    }

    }

    //sao responsaveis por inserir e deletar as datas de folgas
    fun inserirDatasFolgas(datasFolgas: DatasFolgas){
        scopo.launch(context = Dispatchers.IO) {

            repositoriGeral.repositorioDatas.insert(datasFolgas)
        }
    }//vai enserir as datas atraves do metodo inserte criado pelo room
    fun deletarDatasFolgas(datasFolgas: DatasFolgas){
        scopo.launch(context = Dispatchers.IO) {
            repositoriGeral.repositorioDatas.delete(datasFolgas)
        }

    }//responsavel por deletar as datas de folgas

    //vai enserir as datas atraves do metodo inserte criado pelo room
    fun inserirHorariosDosAlarmes(horariosDosAlarmes: HorioDosAlarmes,calbakSnackbar:suspend (String)->Unit){//vai inserir o horario atraves do metodo inserte criado pelo room
        estadosVm.salvandoHorariosResultados.value=ResultadosSalvarHora.salvando//estado de salvando horario
        scopo.launch(Dispatchers.IO) {
            val aux =repositoriGeral.repositorioHorariosDosAlarmes.count()
            if(aux == 0){
                Log.e("vm inseri horario","entrou no if valor aux $aux")
                repositoriGeral.repositorioHorariosDosAlarmes.insert(horariosDosAlarmes)
        }
            else{
                val obj =repositoriGeral.repositorioHorariosDosAlarmes.getPrimeiro()
                Log.e("vm inseri horario","entrou no else valor aux $aux id ${obj.id}")
                repositoriGeral.repositorioHorariosDosAlarmes.update(HorioDosAlarmes(obj.id,horariosDosAlarmes.hora,horariosDosAlarmes.minuto))
            }


        }.invokeOnCompletion { scopo.launch {
            estadosVm.salvandoHorariosResultados.value=ResultadosSalvarHora.comcluido
            calbakSnackbar("Salvo com sucesso")
            delay(1000)
            estadosVm.salvandoHorariosResultados.value=ResultadosSalvarHora.clicavel

        } }
    }

    @SuppressLint("SuspiciousIndentation")
    //vai inserir o modelo de trabalho se estiver vasia se nao vai atualizar
    fun inserirModeloDeTrabalho(modeloDeTrabalho:ModeloDeEScala) {
          scopo.launch(context = Dispatchers.IO) {
          var count = repositoriGeral.repositorioModeloDeTrabalho.count()//melhor maneira que achei para saber se alista ta vasia
              Log.e("vm inseri modelo trabalho","valor de count $count")
             if(count==0){ //quando vasia eu crio e insiro os valores do check
                 var l =listOf(ModeloDeEScala(1,"12/36",false),ModeloDeEScala(2,"6/1",false),ModeloDeEScala(3,"seg-sext",false))
                 repositoriGeral.repositorioModeloDeTrabalho.insert(l)
             }
              else{//se a lista tiver algun registro eu atualizo o valor do check
                  Log.e("vm irei mudar o valor","entrou no else")
                repositoriGeral.repositorioModeloDeTrabalho.update(modeloDeTrabalho)

              }


          }

    }



}
data class mdcheck(val id:Int,val check:Boolean)
data class visulizacaoDatas(val dia:Int,val trabalhado:String)
class EstadosAuxVm(){//classe criada para manter os estados do viewmodel
    var disparaDialogoFerias =mutableStateOf(false)//estado dialog datas de ferias
    var disparaDatass=mutableStateOf(false)//estado dialog datas fiogas
    var transicaoDatPiker =MutableTransitionState(false)//estado transicao animacao Datapiker selecao hr
    var telas=mutableStateOf(TelaNavegacaoSimples.calendario)//estado das telas da navegacao simples
    var transicaoData=MutableTransitionState(false)//estado da transicao anomacao que mostra as datas das folgas
    var transicaoModeloTrabalho= MutableTransitionState(false)//estado transicao Modelo de trabalho
    var transicaoFerias=MutableTransitionState(false) //estado transicao anomacao mostra a parte modelo de trabalho
    var salvandoHorario= mutableStateOf(false)//estado de salvando horario
    var salvandoHorariosResultados= mutableStateOf(ResultadosSalvarHora.clicavel)//estado de salvando horarios
    var resultadosSalvarDatas=mutableStateOf(ResultadosSalvarDatasFolgas.clicavel)//estado de salvando datas de folgasSalvarDatasFolgas)//estado de salvando datas de folgas
}

class Fabricar(){
    // por ser um projeto peque opitei opo nao usar uma libe de gerenciamento de dependencias
    fun fabricar(bd:RoomDb,workManager: WorkManager)=object : ViewModelProvider.Factory{
        //funcao que recebe um objeto do tipo viewmodelProvider.Factory e retorna um objeto do tipo ViewModelTelas
        //que recebe um objeto do tipo RoomDb e um objeto do tipo WorkManager
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ViewModelTelas(db = bd, workManager = workManager)   as T
        }
    }




}