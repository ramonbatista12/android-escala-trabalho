package com.example.escalatrabalho.repositorio.OpicionaiSealedClassess
//aqui comtem os opicionais que vao ser usados no app para imformar opcoes nos modelos de trabalho
//cada opcional tem sua propria string e classe portato o app deve saber como ele deve ser usado
// exeplo se o modelo selecionado for 12/36 o app deve permirtir apenas uma das tres opcoes Impar,Par ou vasio
//vasio sera usado quando o modelo nao for selecionado
// emtao todas as opcoes terao sua propria classe e string e uma opcao defaut que e vasio

//opcoes para modelo 12/36
sealed class OpicionalModelo1236(val opcao:String){
    object Impar : OpicionalModelo1236("Impar")
    object Par : OpicionalModelo1236("Par")
    object Vasio : OpicionalModelo1236("Vasio")
}


//opcoes para modelo 6/1
sealed class OpicionalModelo61(val opcao:String){
    object Segunda : OpicionalModelo61("Seg")
    object Terca : OpicionalModelo61("Ter")
    object Quarta : OpicionalModelo61("Qua")
    object Quinta : OpicionalModelo61("Qui")
    object Sexta : OpicionalModelo61("Sex")
    object Sabado : OpicionalModelo61("Sab")
    object Domingo : OpicionalModelo61("Dom")
    object Feriado : OpicionalModelo61("Feriado")
    object Vasio : OpicionalModelo61("Vasio")


}
// opcoes para modelo seg-sexta
sealed class OpicionalModeloSegSex(val opcao:String){
    object Sbados : OpicionalModeloSegSex("Sabados")
    object Domingos : OpicionalModeloSegSex("Domingos")
    object Feriados : OpicionalModeloSegSex("Feriados")
    object Vasios : OpicionalModeloSegSex("Vasio")


}