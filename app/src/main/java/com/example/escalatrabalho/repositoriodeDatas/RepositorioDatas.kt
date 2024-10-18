package com.example.escalatrabalho.repositoriodeDatas

import android.util.Log
import com.example.escalatrabalho.views.calendario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import java.util.Calendar
import java.util.Date

class RepositorioDatas {
    private var data = Calendar.getInstance()
    private val tabelaMeses = listOf("Janeiro","Feverreiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro")

     fun getDatas():Flow <List<Datas>> = flow {
        var list = mutableListOf<Datas>()

        var ultimoDia = data.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..ultimoDia) {
            data.set(Calendar.DAY_OF_MONTH, 1)

            val diaS = when (data.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SUNDAY -> SemanaDia.doming
                Calendar.MONDAY -> SemanaDia.segunda
                Calendar.TUESDAY -> SemanaDia.terca
                Calendar.WEDNESDAY -> SemanaDia.quarta
                Calendar.THURSDAY -> SemanaDia.quinta
                Calendar.FRIDAY -> SemanaDia.sexta
                Calendar.SATURDAY -> SemanaDia.sabado
                else -> {
                    SemanaDia.sexta
                }

            }
            var d = Datas("${data.get(Calendar.MONTH)}", "${data.get(Calendar.YEAR)}", "${i},", diaS)
            list.add(d)


        }
        if (list.get(0).diaSemana != SemanaDia.doming) {
        Log.e("listas checagem","valor do dia semana :${list.get(0).diaSemana},numero dia ${list.get(0).dia}")
         var lAux=getDiasAnteriores(list.get(0).diaSemana,data.get(Calendar.MONTH))
            list= (lAux+list) as MutableList<Datas>
        }








        emit(list)
    }

    suspend fun getDiasAnteriores(dia: SemanaDia, mesAtual: Int): List<Datas> {
        data.set(Calendar.MONTH, mesAtual - 1)
        var l = mutableListOf<Datas>()
        var masimo = data.getActualMaximum(Calendar.DAY_OF_MONTH)
        data.set(Calendar.DAY_OF_MONTH, masimo)
        for (i in masimo downTo 1) {
            data.set(Calendar.DAY_OF_MONTH, i)
            l.add(
                Datas(
                    "${data.get(Calendar.MONTH)}", "${data.get(Calendar.YEAR)}", "${i}",
                    when (data.get(Calendar.DAY_OF_WEEK)) {
                        Calendar.SUNDAY -> SemanaDia.doming
                        Calendar.MONDAY -> SemanaDia.segunda
                        Calendar.TUESDAY -> SemanaDia.terca
                        Calendar.WEDNESDAY -> SemanaDia.quarta
                        Calendar.THURSDAY -> SemanaDia.quinta
                        Calendar.FRIDAY -> SemanaDia.sexta
                        Calendar.SATURDAY -> SemanaDia.sabado
                        else -> {
                            SemanaDia.sexta
                        }
                    }
                )
            )
            if (l.get(l.lastIndex).diaSemana == SemanaDia.doming){

                return l.asReversed()
            }

        }
    return l
    }

 fun getMes():String{
     return tabelaMeses[data.get(Calendar.MONTH)]
 }
}
