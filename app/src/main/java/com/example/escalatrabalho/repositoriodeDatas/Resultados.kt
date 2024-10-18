package com.example.escalatrabalho.repositoriodeDatas

sealed class Resultados {
    class Ok(val l:List<Datas>)
    class caregando()
    class erro()
}