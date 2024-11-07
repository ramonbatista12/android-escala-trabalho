package com.example.escalatrabalho.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.escalatrabalho.classesResultados.Requisicaoweb
import com.example.escalatrabalho.repositorio.RepositorioPrincipal
import com.example.escalatrabalho.retrofit.CalendarioApi
import com.example.escalatrabalho.roomComfigs.RoomDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(val repositorio :RepositorioPrincipal) : ViewModel() {
    val scop=viewModelScope
    private val estadoPermicao = MutableStateFlow(false)
    val _estadoPermicao: SharedFlow<Boolean> = estadoPermicao.stateIn(
        scope = scop,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
    val dialog = mutableStateOf(if (estadoPermicao.value) false else true)
    val datasColetadas =MutableStateFlow(true)
    val _datasColetadas:SharedFlow<Boolean> =datasColetadas.stateIn(
        scope = scop,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    fun mudarEstado(it: Boolean) {
        estadoPermicao.value = it
        dialog.value = if (estadoPermicao.value) false else true


    }

    fun checarFeriados(){
       scop.launch(Dispatchers.IO) {
           val vasio= repositorio.checarRepositorioFeriados()
         withContext(Dispatchers.Main) {if(vasio==0){
               datasColetadas.value=false
           }else datasColetadas.value=true}
       }

    }

    fun carregarFeriados(){
        scop.launch {
            val r =repositorio.caregarDatasFeriados()
            when(val resposta =r){
                is Requisicaoweb.ok->{
                    datasColetadas.value=true
                                      }
                is Requisicaoweb.erro->{
                    datasColetadas.value=false
                                       }
            }

        }
    }

}


class Fabricarvmain(){
    fun fabricar(bd: RoomDb, calenderios: CalendarioApi)=object : ViewModelProvider.Factory{
        //funcao que recebe um objeto do tipo viewmodelProvider.Factory e retorna um objeto do tipo MainViewModel

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repositorio = RepositorioPrincipal( bd,datasFeriados = calenderios))   as T
        }
    }
}