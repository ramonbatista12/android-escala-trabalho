package com.example.escalatrabalho.enums
/*
*aqui estao presentes os enums usados no app
*
*
* */

// usodo para nvegacao simples  ->navega apenas calendarior e configuracoes
enum class TelaNavegacaoSimples {
    calendario,comfig
}

//usado em telas compactas pois por falta de espasso foi nesesario quebrar a visualizacao em varias partes
enum class TelaNavegacaoSinplesAlturaCompacta{
     calendario,relogio,selecoes,datasFolgas
 }

sealed class MensagemNoticacaoWork(val mensagem:String){
    object MensagemsChecando:MensagemNoticacaoWork("Checando se voce precisa trabalhar ")
    object Folga:MensagemNoticacaoWork("nao agendarei alarme  voce estara de folga")
    object compensado:MensagemNoticacaoWork("nao agendarei alarme  o dia de trabalho foi compensado")
    object Feriado:MensagemNoticacaoWork("nao agendarei alarme amanha e feriado Feriado")
    object FeriasInicio:MensagemNoticacaoWork("Amanha comeca suas  Ferias")
    object feriadoTrabalhado:MensagemNoticacaoWork("agendarei um alarme para o prosimo feriado")
    object Ferias:MensagemNoticacaoWork("nao agendarei alarme hoje voce esta de Ferias")
    object ultimoDiaFerias:MensagemNoticacaoWork("ultimo dia de ferias !!!!")

}

sealed class NomesDeModelosDeEscala(val nome:String){
    object Modelo1236:NomesDeModelosDeEscala("12/36")
    object Modelo61:NomesDeModelosDeEscala("6/1")
    object ModeloSegSex:NomesDeModelosDeEscala("seg-sext")

}

sealed class IdsDeModelosDeEscala(val id:Int){
    object IdModelo1236:IdsDeModelosDeEscala(1)
    object IdModelo61:IdsDeModelosDeEscala(2)
    object IdModeloSegSex:IdsDeModelosDeEscala(3)
}

