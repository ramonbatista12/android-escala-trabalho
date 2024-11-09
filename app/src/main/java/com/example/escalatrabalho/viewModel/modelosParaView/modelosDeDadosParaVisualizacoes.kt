package com.example.escalatrabalho.viewModel.modelosParaView


//aqui estao os modelos de dados que o view model esibe para a view
// a view nao presisa saber como e o dado antes da formatacao do view model
data class mdcheck(val id:Int,val check:Boolean)//classe auxiliar que a view vai receber

data class visulizacaoDatas(val dia:Int,val trabalhado:String)//a view vai sualizar esses dados no calendario
