package com.example.escalatrabalho.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class ViewmodelActivitAlarme: ViewModel() {

   private val scope=viewModelScope
   private  val hora = MutableStateFlow<String>("")
   val  _hora:SharedFlow<String> =  hora.stateIn(
      scope=scope,
      started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
      initialValue = "00:00"
   )
   init {
      scope.launch(Dispatchers.Default) {
         horario().collect {
            hora.value = it
         }
      }

   }
  fun horario(): Flow<String> = flow{
      var horas=""
     while (true){

        val caendar =Calendar.getInstance().time
        val horaformatada =   DateFormat.getTimeInstance(DateFormat.SHORT).format(caendar)
        emit(horaformatada)





     }




  }
}