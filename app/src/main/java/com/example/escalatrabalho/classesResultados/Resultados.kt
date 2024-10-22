package com.example.escalatrabalho.classesResultados

import com.example.escalatrabalho.repositoriodeDatas.Datas

sealed class Resultados {
    class Ok(val l:List<Datas>)
    class caregando()
    class erro()
}

enum class ResultadosSalvarDatasFolgas{
     salvando,
     salvo,
    clicavel

}