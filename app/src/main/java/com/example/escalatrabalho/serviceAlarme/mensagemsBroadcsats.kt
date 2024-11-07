package com.example.escalatrabalho.serviceAlarme

sealed class BroadcastReceiverMensagems(val mensagems:String){
     object Para:BroadcastReceiverMensagems("PARAR")
     object Soneca:BroadcastReceiverMensagems("SONECA")
}