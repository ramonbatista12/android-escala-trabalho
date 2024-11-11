package com.example.escalatrabalho.enums

import com.example.escalatrabalho.R
//irei usar esa classe para definir os icones de cada item de navegacao inferior
sealed class ItemNavegacaoInferior(val draeble: Int,val Label:String){
    object calendario:ItemNavegacaoInferior(draeble = R.drawable.ic_launcher_background,"calendario")
    object config:ItemNavegacaoInferior(draeble = R.drawable.ic_launcher_background,"config")

}
//usarei essa classe para definir os icones de cada item de navegacao lateral fixa
sealed class ItemsFixadeDrawer(val icone: Int,val Label:String){
    object calendario:ItemsFixadeDrawer(icone = R.drawable.ic_launcher_background,"calendario")
    object folgaas:ItemsFixadeDrawer(icone = R.drawable.ic_launcher_background,"config")
    object relogio:ItemsFixadeDrawer(icone = R.drawable.ic_launcher_background,"relogio")
    object selecoes:ItemsFixadeDrawer(icone = R.drawable.ic_launcher_background,"selecoes")
}