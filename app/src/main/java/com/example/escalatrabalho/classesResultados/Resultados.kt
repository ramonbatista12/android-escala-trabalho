package com.example.escalatrabalho.classesResultados

import com.example.escalatrabalho.repositoriodeDatas.Datas

sealed class Resultados {
    //usado para emcapisular os dados que sao emitidos pelo fluxo para que seja posivel a view saber o
    //prossesso de caregamento da lisata para mostra ao usuaria o prossesso de salvar caregar e erro
    //ele emcapisula o resultado emitido pelo viewmodel pois que determina a logica do que e pasado para a
    //e o viewmodel ele e o detentor desssa responsabilade
    //isso tabem faz parte das indicacoes dadas pela google pois e ruim a view saber muito sobre os dados
    // lembrando que deve aver separacao entre as responsabilidasde
    class Ok(val l:List<Datas>)
    class caregando()
    class erro()
}

enum class ResultadosSalvarDatasFolgas{
     salvando,
     salvo,
    clicavel

}