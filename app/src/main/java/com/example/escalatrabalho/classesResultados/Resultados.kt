package com.example.escalatrabalho.classesResultados

import com.example.escalatrabalho.repositorio.repositoriodeDatas.Datas
import com.example.escalatrabalho.roomComfigs.DatasFolgas
//aqui estao os resultados que serao usados no app para mostrar as informacoes
//eles serao usados para representatar esdtados como caregando erro etc....


//usado para reprensentar estados  ok caregando e erro  das datas
sealed interface Resultados {

   data class Ok(val l:List<Datas>): Resultados
   data object caregando: Resultados
   data  object  erro: Resultados
}


//resultados para as datas de folgas
enum class ResultadosSalvarDatasFolgas{
     salvando,
     salvo,
     clicavel

}

//seriam usados para reprensentar as permicoes
sealed interface Permicoes{
    data object comsedidas: Permicoes
    data class  naocomsedidas(val l: List<String>) : Permicoes

}
//represntao as resposntas das requisicoes http dp retro fit
sealed interface Requisicaoweb{
    data object  ok: Requisicaoweb
    data class  erro(val erro:String): Requisicaoweb

}

//resultados para as horas dos alarmes
enum  class ResultadosSalvarHora{
    Salvo, clicavel , salvando,comcluido

}


//resultados para as datas de folgas
sealed class ResultadosDatasFolgas {
    class caregando()
    class erro()
    class ok(val lista: List<DatasFolgas>)
}

//resultados para os modelos de escala
sealed class ResultadosModeloTrabalh{
    class salvando()
    class visualizando()


}