package com.example.escalatrabalho.repositorio.repositoriodeDatas

//eu uso a classe calendar para pegar a data selecionada e inserir no banco de dados
// pois esiste a nessesitade de receber um nuimerro e comverter ele em uma date
// para nao ter que lidar com essa logica eu usei o classe nativa do java calendar apesar de averem outras nativas como
//gregorian calendar e etc eu opitei por esse ja que ja usei ela uma vez quando aprendi java
//usei ela para trabalhar com datas piois antes de fazer o projeto pesquisei sobre e me deparei com algo semelhante no stackOwverflow



import android.util.Log
import com.example.escalatrabalho.views.calendario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import java.util.Calendar
import java.util.Date

class RepositorioDatas {
    private var data = Calendar.getInstance()
    private val tabelaMeses = listOf("JANEIRO","FEVERREIRO","MARCO","ABRIL","MAIO","JUNHO","JULHO","AGOSTO","SETEMBRO","OUTUBRO","NOVEMBRO","DEZEMBRO")
    var mes =0
    var ano =0

    init {
        mes=data.get(Calendar.MONTH)
        ano=data.get(Calendar.YEAR)
    }

    fun getDatas():Flow <List<Datas>> = flow {
        var list = mutableListOf<Datas>()
        data=Calendar.getInstance()
        var ultimoDia = data.getActualMaximum(Calendar.DAY_OF_MONTH)+1

        for (i in 1..ultimoDia) {
            data.roll(Calendar.DAY_OF_MONTH, 1)
            data.set(Calendar.DAY_OF_MONTH, i)
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
            var d = Datas("${data.get(Calendar.MONTH)}", "${data.get(Calendar.YEAR)}", "${data.get(Calendar.DAY_OF_MONTH)}", diaS)
            list.add(d)


        }
        if (list.get(0).diaSemana != SemanaDia.doming) {
            Log.e("listas checagem","valor do dia semana :${list.get(0).diaSemana},numero dia ${list.get(0).dia}")
            var lAux=getDiasAnteriores(list.get(0).diaSemana,data.get(Calendar.MONTH))
            list= (lAux+list) as MutableList<Datas>
        }






        data = Calendar.getInstance()

        emit(list)
    }

    suspend fun getDiasAnteriores(dia: SemanaDia, mesAtual: Int): List<Datas> {
        data = Calendar.getInstance()
        data.roll(Calendar.MONTH, -1)
        var l = mutableListOf<Datas>()
        var masimo = data.getActualMaximum(Calendar.DAY_OF_MONTH)
        data.set(Calendar.DAY_OF_MONTH, masimo)
        for (i in masimo downTo 1) {
            data.roll(Calendar.DAY_OF_MONTH, -1)
            data.set(Calendar.DAY_OF_MONTH, i)
            l.add(
                Datas(
                    "${data.get(Calendar.MONTH)}", "${data.get(Calendar.YEAR)}", "${data.get(Calendar.DAY_OF_MONTH)}",
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
        data =Calendar.getInstance()
        return l
    }

    suspend fun getDiaAtualEprosimo():DiasChecagen{
        data = Calendar.getInstance()
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
        val numeroDia=data.get(Calendar.DAY_OF_MONTH)
        val mes =data.get(Calendar.MONTH)
        val ano =data.get(Calendar.YEAR)
        data.roll(Calendar.DAY_OF_MONTH,1)
        data.set(Calendar.DAY_OF_MONTH,numeroDia+1)
        val prosimodiaS = when (data.get(Calendar.DAY_OF_WEEK)) {
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
        val prosimoNumeroDia=data.get(Calendar.DAY_OF_MONTH)
        val mesProsimodia =data.get(Calendar.MONTH)
        val anoProsimodia=data.get(Calendar.YEAR)
        return DiasChecagen(numeroDia,diaS,prosimoNumeroDia,prosimodiaS, mes = mes,mesProsimodia=mesProsimodia,ano=ano,anoProsimodia=anoProsimodia)
    }

    fun getMes():String{
        return tabelaMeses[data.get(Calendar.MONTH)]
    }




}

class DiasChecagen(val dia:Int,val diasemana: SemanaDia,val prosimoDia:Int,val prosimoDiasemana: SemanaDia,val mes:Int,val mesProsimodia:Int,val ano:Int,val anoProsimodia:Int)
