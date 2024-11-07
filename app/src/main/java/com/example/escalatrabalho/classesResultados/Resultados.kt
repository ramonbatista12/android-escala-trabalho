package com.example.escalatrabalho.classesResultados

import com.example.escalatrabalho.repositorio.repositoriodeDatas.Datas

sealed interface Resultados {
    //usado para emcapisular os dados que sao emitidos pelo fluxo para que seja posivel a view saber o
    //prossesso de caregamento da lisata para mostra ao usuaria o prossesso de salvar caregar e erro
    //ele emcapisula o resultado emitido pelo viewmodel pois que determina a logica do que e pasado para a
    //e o viewmodel ele e o detentor desssa responsabilade
    //isso tabem faz parte das indicacoes dadas pela google pois e ruim a view saber muito sobre os dados
    // lembrando que deve aver separacao entre as responsabilidasde
   data class Ok(val l:List<Datas>): Resultados
   data object caregando: Resultados
   data  object  erro: Resultados
}

enum class ResultadosSalvarDatasFolgas{
     salvando,
     salvo,
    clicavel

}
sealed interface Permicoes{
    data object  comsedidas: Permicoes
    data class naocomsedidas(val l: List<String>) : Permicoes

}

sealed interface Requisicaoweb{
    data object  ok: Requisicaoweb
    data class  erro(val erro:String): Requisicaoweb

}