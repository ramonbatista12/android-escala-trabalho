package com.example.escalatrabalho.worlk

import android.util.Log
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModelo1236
import com.example.escalatrabalho.repositorio.RepositorioExecutado
import com.example.escalatrabalho.repositorio.RepositorioPrincipal
import com.example.escalatrabalho.repositorio.repositoriodeDatas.DiasChecagen
import com.example.escalatrabalho.repositorio.repositoriodeDatas.SemanaDia
import com.example.escalatrabalho.roomComfigs.DiasOpcionais
import com.example.escalatrabalho.roomComfigs.Feriados
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class WorkAuxiliarEstados(val repositorioPrincipal: RepositorioPrincipal, val repositorioExecutado: RepositorioExecutado) {
    private val job = Job()
    val scop = CoroutineScope(Dispatchers.Main + job)
    private val horioDosAlarmes = MutableStateFlow(HorioDosAlarmes(0,0,0))
    private val modeloDeEscala = MutableStateFlow(ModeloDeEScala(0,"",false))
    private val opcionais = MutableStateFlow(DiasOpcionais(0,"defaut",OpicionalModelo1236.Vasio.opcao))
    private val alarmes = MutableStateFlow(HorioDosAlarmes(0,0,0))
    private val listaDiasDefolgas = MutableStateFlow(emptyList<Int>())
    private val diasParachecagem= MutableStateFlow(DiasChecagen(0,SemanaDia.doming,0,SemanaDia.doming))
    private val feriados = MutableStateFlow(emptyList<Feriados>())
    val _horarioDosAlarmes: StateFlow<HorioDosAlarmes> = horioDosAlarmes.asStateFlow()
    val _modeloDeEscala: StateFlow<ModeloDeEScala> = modeloDeEscala.asStateFlow()
    val _opcionais: StateFlow<DiasOpcionais> = opcionais.asStateFlow()
    val _alarmes: StateFlow<HorioDosAlarmes> = alarmes.asStateFlow()
    val _listaDiasDefolgas: StateFlow<List<Int>> = listaDiasDefolgas.asStateFlow()
    val _diasParachecagem: StateFlow<DiasChecagen> = diasParachecagem.asStateFlow()
    val _feriados: StateFlow<List<Feriados>> = feriados.asStateFlow()

   suspend fun caregarEstados() {
       Log.i("comstrutor iniciado", "corotina vai ser iiciado")
      val asinc= scop.async {
       val jobs = listOf(
           scop.launch(Dispatchers.IO) {
               modeloDeEscala.value =
                   repositorioPrincipal.getmodeloObjeto() ?: ModeloDeEScala(0, "", false)
           },

           scop.launch(Dispatchers.IO) {
               horioDosAlarmes.value =
                   repositorioPrincipal.getObjetosHorariosAlsrme() ?: HorioDosAlarmes(0, 0, 0)

           },


           scop.launch {
               repositorioPrincipal.fluxoDatasFolgas.first(){
                   listaDiasDefolgas.value = it.map { it.data }
                   true
               }

           },

           scop.launch {
               diasParachecagem.value = repositorioPrincipal.getDiaAtualEprosimo() ?: DiasChecagen(
                   0,
                   SemanaDia.doming,
                   0,
                   SemanaDia.doming
               )
           },
           scop.launch(Dispatchers.IO) {
            opcionais.value =
                repositorioPrincipal.getOpcionaisObjeto(modeloDeEscala.value.modelo)?:DiasOpcionais(0,"","")


           },

           scop.launch {
               repositorioPrincipal.fluxoFeriados.first {
                   feriados.value = it
                   true
               }
           }
       )

          jobs.joinAll() }
       asinc.await()



   }


    fun finalizar(){
        job.cancel()
    }


}