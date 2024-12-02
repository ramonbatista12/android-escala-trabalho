package com.example.escalatrabalho.repositorio.repositoriodeDatas

data class Datas(val mes:String,val ano:String,val dia:String,val diaSemana: SemanaDia)


enum class SemanaDia{
    doming,segunda,terca,quarta,quinta,sexta,sabado
}