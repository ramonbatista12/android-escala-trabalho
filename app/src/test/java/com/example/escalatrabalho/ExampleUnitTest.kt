package com.example.escalatrabalho

import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.worlk.AgendarAlarmes
import com.example.escalatrabalho.worlk.WorkAuxiliarDecisoes
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

class TesteAgendarAlarme{
    @Test
    fun agendarAlarme(): Unit {
        val WorkAuxiliarDecisoes=WorkAuxiliarDecisoes()

     val a=   WorkAuxiliarDecisoes.checaHorarioParaAgendadmento(HorioDosAlarmes(0,0,0))
     val b =WorkAuxiliarDecisoes.checaHorarioParaAgendadmento(HorioDosAlarmes(1,20,0))
     val localT=LocalTime.now()
     System.out.println("${localT.hour}:${localT.minute},${a},${b}")

    }
}