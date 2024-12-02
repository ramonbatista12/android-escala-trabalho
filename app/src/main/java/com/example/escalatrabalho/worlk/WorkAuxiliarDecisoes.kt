package com.example.escalatrabalho.worlk

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.escalatrabalho.enums.MensagemNoticacaoWork
import com.example.escalatrabalho.enums.NomesDeModelosDeEscala
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModelo1236
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModelo61
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModeloSegSex
import com.example.escalatrabalho.repositorio.repositoriodeDatas.Datas
import com.example.escalatrabalho.repositorio.repositoriodeDatas.DiasChecagen
import com.example.escalatrabalho.repositorio.repositoriodeDatas.SemanaDia
import com.example.escalatrabalho.roomComfigs.DatasFolgas
import com.example.escalatrabalho.roomComfigs.DiasOpcionais
import com.example.escalatrabalho.roomComfigs.Feriados
import com.example.escalatrabalho.roomComfigs.Ferias
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import kotlinx.coroutines.flow.StateFlow
import java.lang.StringBuilder
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
/*
* classe auxiliar ela auxilia o agendador a agendar os alarmes de acordo com o modelo de escala e outras decisoes nesesarias
* ele comtem apenas funcoes pois elas serao chamadaas pelo agendar alarmes ela ajuda a dividir a logica de agendamento de acordo com o modelo de escala
* impedindo que aja estremo aninanha mento de codigos no agendar alarmes  ela cotem tabem funcoes privadas para cada acao especifica
*
* */
class WorkAuxiliarDecisoes {

    @RequiresApi(Build.VERSION_CODES.O)
    fun modelo1236(diasOpcionais:DiasOpcionais,
                   diasDeFolgas:List<Int>,
                   diasChecagen:DiasChecagen,
                   horario: HorioDosAlarmes,
                   calbackAlarmeAmanha:(h:Int,m:Int)->Unit,calbackAlarmeHoje:(h:Int,m:Int)->Unit){

        Log.i("modelo selecionado","modelo 1236")
        when(diasOpcionais.opicional){

            OpicionalModelo1236.Impar.opcao->{
                Log.i("dias ","dia par")
                if(diasChecagen.dia%2==0){
                    if(!diasDeFolgas.contains(diasChecagen.prosimoDia)){
                        Log.i("dias ","dia nao esta na lista")
                        calbackAlarmeAmanha(horario.hora,horario.minuto)
                    }
                }
                else{
                    Log.i("dias ","alarme e para o dia de hoje ")
                    if(checaHorarioParaAgendadmento(horario)){
                        Log.i("dias ","horario do alarme e depois do atual")
                        calbackAlarmeHoje(horario.hora,horario.minuto)

                    }

                }


            }
            OpicionalModelo1236.Par.opcao->{
                if(diasChecagen.dia%2!=0){
                    if(!diasDeFolgas.contains(diasChecagen.prosimoDia)){
                        calbackAlarmeAmanha(horario.hora,horario.minuto)
                    }
                }
                else{
                    if(checaHorarioParaAgendadmento(horario)){
                        calbackAlarmeHoje(horario.hora,horario.minuto)

                    }

                }
            }
            OpicionalModelo1236.Vasio.opcao->{
                Log.i("dias","modelo vasio")
            }
          }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun modelo61(diasOpcionais:DiasOpcionais,
                 diasDeFolgas:List<Int>,
                 feriados:List<Feriados>,
                 diasChecagen:DiasChecagen,
                 horario: HorioDosAlarmes,
                 calbackAlarmeAmanha:(h:Int,m:Int)->Unit, calbackAlarmeHoje:(h:Int,m:Int)->Unit){

        when(diasOpcionais.opicional){
            OpicionalModelo61.Domingo.opcao-> auxiliar61(diasOpcionais=diasOpcionais,
                                                        diaIgnorado = SemanaDia.doming,
                                                        feriados = feriados,
                                                        diasChecagen = diasChecagen,
                                                        horario = horario,
                                                        calbackAlarmeHoje=calbackAlarmeHoje,
                                                        calbackAlarmeAmanha=calbackAlarmeAmanha)

            OpicionalModelo61.Segunda.opcao->auxiliar61(diasOpcionais=diasOpcionais,
                                                        diaIgnorado = SemanaDia.segunda,
                                                        feriados = feriados,
                                                        diasChecagen = diasChecagen,
                                                        horario = horario,
                                                        calbackAlarmeHoje=calbackAlarmeHoje,
                                                        calbackAlarmeAmanha=calbackAlarmeAmanha)

            OpicionalModelo61.Terca.opcao->auxiliar61(diasOpcionais=diasOpcionais,
                                                      diaIgnorado = SemanaDia.terca,
                                                      feriados = feriados,
                                                      diasChecagen = diasChecagen,
                                                      horario = horario,
                                                      calbackAlarmeHoje=calbackAlarmeHoje,
                                                      calbackAlarmeAmanha=calbackAlarmeAmanha)

            OpicionalModelo61.Quarta.opcao->auxiliar61(diasOpcionais=diasOpcionais,
                                                       diaIgnorado = SemanaDia.quarta,
                                                       feriados = feriados,
                                                       diasChecagen = diasChecagen,
                                                       horario = horario,
                                                       calbackAlarmeHoje=calbackAlarmeHoje,
                                                       calbackAlarmeAmanha=calbackAlarmeAmanha)

            OpicionalModelo61.Quinta.opcao->auxiliar61(diasOpcionais=diasOpcionais,
                                                       diaIgnorado = SemanaDia.quinta,
                                                       feriados = feriados,
                                                       diasChecagen = diasChecagen,
                                                       horario = horario,
                                                       calbackAlarmeHoje=calbackAlarmeHoje,
                                                       calbackAlarmeAmanha=calbackAlarmeAmanha)

            OpicionalModelo61.Sexta.opcao-> auxiliar61(diasOpcionais=diasOpcionais,
                                                       diaIgnorado = SemanaDia.sexta,
                                                       feriados = feriados,
                                                       diasChecagen = diasChecagen,
                                                       horario = horario,
                                                       calbackAlarmeHoje=calbackAlarmeHoje,
                                                       calbackAlarmeAmanha=calbackAlarmeAmanha)

            OpicionalModelo61.Sabado.opcao->auxiliar61(diasOpcionais=diasOpcionais,
                                                       diaIgnorado = SemanaDia.sabado,
                                                       feriados = feriados,
                                                       diasChecagen = diasChecagen,
                                                       horario = horario,
                                                       calbackAlarmeHoje=calbackAlarmeHoje,
                                                       calbackAlarmeAmanha=calbackAlarmeAmanha)

            OpicionalModelo61.Vasio.opcao->auxiliar61(diasOpcionais=diasOpcionais,
                                                      diaIgnorado = SemanaDia.doming,
                                                      feriados = feriados,
                                                      diasChecagen = diasChecagen,
                                                      horario = horario,
                                                      calbackAlarmeHoje=calbackAlarmeHoje,
                                                      calbackAlarmeAmanha=calbackAlarmeAmanha)

        }

    }




    @RequiresApi(Build.VERSION_CODES.O)
    fun modeloSegundaSexta(diasOpcionais: DiasOpcionais,
                           feriados:List<Feriados>,
                           diasChecagen: DiasChecagen,
                           horario: HorioDosAlarmes,
                           calbackAlarmeHoje:(h:Int,m:Int)->Unit,
                           calbackAlarmeAmanha: (h: Int, m: Int) -> Unit){
        val feriadosDias=feriados.map { it.dia }


        when(diasOpcionais.opicional){
            OpicionalModeloSegSex.Domingos.opcao->{
               when(diasChecagen.diasemana){
                   SemanaDia.segunda-> auxiliarDomingosEscalaSegundaseSexta(diasChecagen,horario,feriadosDias,calbackAlarmeHoje,calbackAlarmeAmanha)


                   SemanaDia.terca->auxiliarDomingosEscalaSegundaseSexta(diasChecagen,horario,feriadosDias,calbackAlarmeHoje,calbackAlarmeAmanha)

                   SemanaDia.quarta-> auxiliarDomingosEscalaSegundaseSexta(diasChecagen,horario,feriadosDias,calbackAlarmeHoje,calbackAlarmeAmanha)

                   SemanaDia.quinta-> auxiliarDomingosEscalaSegundaseSexta(diasChecagen,horario,feriadosDias,calbackAlarmeHoje,calbackAlarmeAmanha)
                   SemanaDia.sexta-> {
                       if(!feriadosDias.contains(diasChecagen.dia))
                           if(checaHorarioParaAgendadmento(horario))
                               calbackAlarmeHoje(horario.hora,horario.minuto)
                   }

                   SemanaDia.sabado->{
                        if(!feriadosDias.contains(diasChecagen.prosimoDia))
                               calbackAlarmeAmanha(horario.hora,horario.minuto)

                   }
                   SemanaDia.doming-> auxiliarDomingosEscalaSegundaseSexta(diasChecagen,horario,feriadosDias,calbackAlarmeHoje,calbackAlarmeAmanha)
               }
            }
            OpicionalModeloSegSex.Sbados.opcao->{
                when(diasChecagen.diasemana){
                    SemanaDia.segunda->auxiliarSabadosEscalaSegundaseSexta(diasChecagen, horario, feriadosDias, calbackAlarmeHoje, calbackAlarmeAmanha)
                    SemanaDia.terca->auxiliarSabadosEscalaSegundaseSexta(diasChecagen, horario, feriadosDias, calbackAlarmeHoje, calbackAlarmeAmanha)
                    SemanaDia.quarta->auxiliarSabadosEscalaSegundaseSexta(diasChecagen, horario, feriadosDias, calbackAlarmeHoje, calbackAlarmeAmanha)
                    SemanaDia.quinta->auxiliarSabadosEscalaSegundaseSexta(diasChecagen, horario, feriadosDias, calbackAlarmeHoje, calbackAlarmeAmanha)
                    SemanaDia.sexta->auxiliarSabadosEscalaSegundaseSexta(diasChecagen, horario, feriadosDias, calbackAlarmeHoje, calbackAlarmeAmanha)
                    SemanaDia.sabado->{
                        if(!feriadosDias.contains(diasChecagen.dia))
                            if(checaHorarioParaAgendadmento(horario))
                                calbackAlarmeHoje(horario.hora,horario.minuto)
                    }
                    SemanaDia.doming->{
                        if(!feriadosDias.contains(diasChecagen.prosimoDia))
                            calbackAlarmeAmanha(horario.hora,horario.minuto)

                    }


                }
            }
            OpicionalModeloSegSex.Feriados.opcao->{
                when(diasChecagen.diasemana){
                    SemanaDia.segunda->auxiliarFeriadosEscalaSegundaseSexta(diasChecagen, horario, feriadosDias, calbackAlarmeHoje, calbackAlarmeAmanha)
                    SemanaDia.terca->auxiliarFeriadosEscalaSegundaseSexta(diasChecagen, horario, feriadosDias, calbackAlarmeHoje, calbackAlarmeAmanha)
                    SemanaDia.quarta->auxiliarFeriadosEscalaSegundaseSexta(diasChecagen, horario, feriadosDias, calbackAlarmeHoje, calbackAlarmeAmanha)
                    SemanaDia.quinta->auxiliarFeriadosEscalaSegundaseSexta(diasChecagen, horario, feriadosDias, calbackAlarmeHoje, calbackAlarmeAmanha)
                    SemanaDia.sexta->{
                        if(diasChecagen.diasemana!=SemanaDia.sabado&&diasChecagen.diasemana!=SemanaDia.doming)
                            if(checaHorarioParaAgendadmento(horario))
                                calbackAlarmeHoje(horario.hora,horario.minuto)
                    }
                    SemanaDia.sabado->{}
                    SemanaDia.doming->{
                        if(diasChecagen.prosimoDiasemana!=SemanaDia.sabado&&diasChecagen.prosimoDiasemana!=SemanaDia.doming)
                            calbackAlarmeAmanha(horario.hora,horario.minuto)
                    }
                }
            }
            OpicionalModeloSegSex.Vasios.opcao->{
                when(diasChecagen.diasemana){
                    SemanaDia.segunda->auxiliarVasioEscalaSegundaseSexta(diasChecagen, horario, feriadosDias, calbackAlarmeHoje, calbackAlarmeAmanha)
                    SemanaDia.terca->auxiliarVasioEscalaSegundaseSexta(diasChecagen, horario, feriadosDias, calbackAlarmeHoje, calbackAlarmeAmanha)
                    SemanaDia.quarta->auxiliarVasioEscalaSegundaseSexta(diasChecagen, horario, feriadosDias, calbackAlarmeHoje, calbackAlarmeAmanha)
                    SemanaDia.quinta->auxiliarVasioEscalaSegundaseSexta(diasChecagen, horario, feriadosDias, calbackAlarmeHoje, calbackAlarmeAmanha)
                    SemanaDia.sexta->{
                       if( diasChecagen.diasemana!=SemanaDia.sabado&&diasChecagen.diasemana!=SemanaDia.doming)
                        if(checaHorarioParaAgendadmento(horario))
                            calbackAlarmeHoje(horario.hora,horario.minuto)
                    }
                    SemanaDia.sabado->{}
                    SemanaDia.doming->{
                        if(diasChecagen.prosimoDiasemana!=SemanaDia.sabado&&diasChecagen.prosimoDiasemana!=SemanaDia.doming)
                            calbackAlarmeAmanha(horario.hora,horario.minuto)
                    }
                }

            }



        }

    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun modeloFerias(calbackAlarmeHoje:(h:Int, m:Int)->Unit,
                     calbackNoificacaoEmferias:()->Unit ,
                     calbackNoificacaoUltimoDiaDeFerias: () -> Unit,
                     horario: HorioDosAlarmes,
                     feriados:List<Feriados>,
                     ferias: Ferias,modeloEscala: ModeloDeEScala,
                     folgas:List<Int>,
                     diasChecagen: DiasChecagen,
                     opicional:DiasOpcionais,calbackAlarmeAmanha:(h:Int,m:Int)->Unit){
       val localDate =LocalDate.now()
       val localDateFimdasferias =LocalDate.of(ferias.anoFim,ferias.mesFim,ferias.diaFim)
       if(localDateFimdasferias.isEqual(localDate)){
           calbackNoificacaoUltimoDiaDeFerias()
           when(modeloEscala.modelo){

               NomesDeModelosDeEscala.Modelo61.nome->feriasNModelo61(opconal =opicional,
                                                                     folgas = folgas,
                                                                     feriados = feriados,
                                                                     diasChecagen = diasChecagen,
                                                                     horario = horario,
                                                                     calbackAlarmeAmanha = calbackAlarmeAmanha )

               NomesDeModelosDeEscala.Modelo1236.nome->feriasNModelo1236(opconal = opicional,
                                                                         diasChecagen = diasChecagen,
                                                                         horario = horario,
                                                                         folgas = folgas,
                                                                         calbackAlarmeAmanha = calbackAlarmeAmanha)

               NomesDeModelosDeEscala.ModeloSegSex.nome->feriasNModeloSegSex(opconal = opicional,
                                                                             diaChecagem = diasChecagen,
                                                                             horario = horario,
                                                                             feriados=feriados,
                                                                             calbackAlarmeAmanha=calbackAlarmeAmanha)
               else->{}

           }
       } else calbackNoificacaoEmferias()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun feriasNModelo61(opconal: DiasOpcionais,
                                folgas: List<Int>,
                                feriados:List<Feriados>,
                                diasChecagen: DiasChecagen,
                                horario: HorioDosAlarmes,
                                calbackAlarmeAmanha: (h: Int, m: Int) -> Unit){
        val _feriados=feriados.map { it.dia }

        val diadeHoje=LocalDate.now()
        val diaSemana = diadeHoje.dayOfWeek.name
        val prosimoDia=comveteLocalDateDayOfWik(diaSemana)
        when(opconal.opicional){
            OpicionalModelo61.Domingo.opcao->{
             if(prosimoDia!=SemanaDia.doming&&!_feriados.contains(diasChecagen.prosimoDia))
                 if(!folgas.contains(diasChecagen.prosimoDia))
                     calbackAlarmeAmanha(horario.hora,horario.minuto)
            }
            OpicionalModelo61.Segunda.opcao->{
                if(prosimoDia!=SemanaDia.segunda&&!_feriados.contains(diasChecagen.prosimoDia))
                    if(!folgas.contains(diasChecagen.prosimoDia))
                        calbackAlarmeAmanha(horario.hora,horario.minuto)
            }
            OpicionalModelo61.Terca.opcao->{
                if(prosimoDia!=SemanaDia.terca&&!_feriados.contains(diasChecagen.prosimoDia))
                    if(!folgas.contains(diasChecagen.prosimoDia))
                        calbackAlarmeAmanha(horario.hora,horario.minuto)
            }
            OpicionalModelo61.Quarta.opcao->{
                if(prosimoDia!=SemanaDia.quarta&&!_feriados.contains(diasChecagen.prosimoDia))
                    if(!folgas.contains(diasChecagen.prosimoDia))
                        calbackAlarmeAmanha(horario.hora,horario.minuto)
            }
            OpicionalModelo61.Quinta.opcao->{
                if(prosimoDia!=SemanaDia.quinta&&!_feriados.contains(diasChecagen.prosimoDia))
                    if(!folgas.contains(diasChecagen.prosimoDia))
                        calbackAlarmeAmanha(horario.hora,horario.minuto)
            }
            OpicionalModelo61.Sexta.opcao->{
                if(prosimoDia!=SemanaDia.sexta&&!_feriados.contains(diasChecagen.prosimoDia))
                    if(!folgas.contains(diasChecagen.prosimoDia))
                        calbackAlarmeAmanha(horario.hora,horario.minuto)
            }
            OpicionalModelo61.Sabado.opcao->{
                if(prosimoDia!=SemanaDia.sabado&&!_feriados.contains(diasChecagen.prosimoDia))
                    if(!folgas.contains(diasChecagen.prosimoDia))
                        calbackAlarmeAmanha(horario.hora,horario.minuto)
            }
            OpicionalModelo61.Vasio.opcao->{
                if(!_feriados.contains(diasChecagen.prosimoDia))
                    if(!folgas.contains(diasChecagen.prosimoDia))
                        calbackAlarmeAmanha(horario.hora,horario.minuto)
            }
        }


    }




    private fun feriasNModelo1236(opconal: DiasOpcionais,
                                  diasChecagen: DiasChecagen,
                                  horario: HorioDosAlarmes,
                                  folgas: List<Int>,calbackAlarmeAmanha: (h: Int, m: Int) -> Unit){


        when(opconal.opicional){
            OpicionalModelo1236.Par.opcao->{
                if(diasChecagen.dia!=31||diasChecagen.dia!=29)
                    if(!folgas.contains(diasChecagen.dia))
                        calbackAlarmeAmanha(horario.hora,horario.minuto)

            }
            OpicionalModelo1236.Impar.opcao->{
                if(diasChecagen.dia!=31||diasChecagen.dia!=29)
                    if(!folgas.contains(diasChecagen.dia))
                        calbackAlarmeAmanha(horario.hora,horario.minuto)

            }
            OpicionalModelo1236.Vasio.opcao->{

            }
        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun feriasNModeloSegSex(opconal:DiasOpcionais, diaChecagem:DiasChecagen,
                                    horario: HorioDosAlarmes, feriados: List<Feriados>,
                                    calbackAlarmeAmanha: (h: Int, m: Int) -> Unit){
        val _feriados=feriados.map { it.dia }
        val diadeHoje=LocalDate.now()
        val diaSemana = diadeHoje.dayOfWeek.name
        val prosimoDia=comveteLocalDateDayOfWik(diaSemana)

        when(opconal.opicional){
            OpicionalModeloSegSex.Domingos.opcao->{
                if(prosimoDia!=SemanaDia.sabado&& !_feriados.contains(diaChecagem.prosimoDia))
                    calbackAlarmeAmanha(horario.hora,horario.minuto)
            }
            OpicionalModeloSegSex.Sbados.opcao->{
                if(prosimoDia!=SemanaDia.doming&& !_feriados.contains(diaChecagem.prosimoDia))
                    calbackAlarmeAmanha(horario.hora,horario.minuto)
            }
            OpicionalModeloSegSex.Feriados.opcao->{
                if(prosimoDia!=SemanaDia.sabado&&prosimoDia!=SemanaDia.doming)
                    calbackAlarmeAmanha(horario.hora,horario.minuto)

            }
            OpicionalModeloSegSex.Vasios.opcao->{
                if(prosimoDia!=SemanaDia.sabado&&prosimoDia!=SemanaDia.doming && !_feriados.contains(diaChecagem.prosimoDia))
                    calbackAlarmeAmanha(horario.hora,horario.minuto)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prosimoDiaSemana(localDate: LocalDate):String{
        val prosimoDia=localDate.plusDays(1)
        val prosimoDiaSemana=prosimoDia.dayOfWeek.name
        return prosimoDiaSemana
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun auxiliarDomingosEscalaSegundaseSexta(diasChecagen: DiasChecagen,
                                                     horario: HorioDosAlarmes,
                                                     feriadosDias:List<Int>,
                                                     calbackAlarmeHoje:(h:Int,m:Int)->Unit,
                                                     calbackAlarmeAmanha: (h: Int, m: Int) -> Unit){
        if(!feriadosDias.contains(diasChecagen.dia))
            if(checaHorarioParaAgendadmento(horario))
                calbackAlarmeHoje(horario.hora,horario.minuto)
            else if(!feriadosDias.contains(diasChecagen.prosimoDia)&&diasChecagen.prosimoDiasemana!=SemanaDia.sabado)
                calbackAlarmeAmanha(horario.hora,horario.minuto)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun auxiliarSabadosEscalaSegundaseSexta(diasChecagen: DiasChecagen,
                                                     horario: HorioDosAlarmes,
                                                     feriadosDias:List<Int>,
                                                     calbackAlarmeHoje:(h:Int,m:Int)->Unit,
                                                     calbackAlarmeAmanha: (h: Int, m: Int) -> Unit){
        if(!feriadosDias.contains(diasChecagen.dia))
            if(checaHorarioParaAgendadmento(horario))
                calbackAlarmeHoje(horario.hora,horario.minuto)
            else if(!feriadosDias.contains(diasChecagen.prosimoDia)&&diasChecagen.prosimoDiasemana!=SemanaDia.doming)
                calbackAlarmeAmanha(horario.hora,horario.minuto)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun auxiliarFeriadosEscalaSegundaseSexta(diasChecagen: DiasChecagen,
                                                    horario: HorioDosAlarmes,
                                                    feriadosDias:List<Int>,
                                                    calbackAlarmeHoje:(h:Int,m:Int)->Unit,
                                                    calbackAlarmeAmanha: (h: Int, m: Int) -> Unit){
        if(diasChecagen.diasemana!=SemanaDia.sabado&&diasChecagen.diasemana!=SemanaDia.doming)
            if(checaHorarioParaAgendadmento(horario))
                calbackAlarmeHoje(horario.hora,horario.minuto)
            else if(diasChecagen.prosimoDiasemana!=SemanaDia.sabado&&diasChecagen.prosimoDiasemana!=SemanaDia.doming)
                calbackAlarmeAmanha(horario.hora,horario.minuto)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun auxiliarVasioEscalaSegundaseSexta(diasChecagen: DiasChecagen,
                                                     horario: HorioDosAlarmes,
                                                     feriadosDias:List<Int>,
                                                     calbackAlarmeHoje:(h:Int,m:Int)->Unit,
                                                     calbackAlarmeAmanha: (h: Int, m: Int) -> Unit){
        if(diasChecagen.diasemana!=SemanaDia.sabado&&diasChecagen.diasemana!=SemanaDia.doming&&!feriadosDias.contains(diasChecagen.dia))
            if(checaHorarioParaAgendadmento(horario))
                calbackAlarmeHoje(horario.hora,horario.minuto)
            else if(diasChecagen.prosimoDiasemana!=SemanaDia.sabado&&diasChecagen.prosimoDiasemana!=SemanaDia.doming&&!feriadosDias.contains(diasChecagen.prosimoDia))
                calbackAlarmeAmanha(horario.hora,horario.minuto)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun checaHorarioParaAgendadmento(horario:HorioDosAlarmes):Boolean{
       val horarioAtual=LocalTime.now()
       val localTaimer=LocalTime.of(horario.hora,horario.minuto)
       if(horarioAtual.isBefore(localTaimer)){
           return true
       }

     return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun auxiliar61(diasOpcionais: DiasOpcionais, diaIgnorado:SemanaDia,
                           feriados:List<Feriados>,
                           diasChecagen: DiasChecagen,
                           horario: HorioDosAlarmes,
                           calbackAlarmeHoje:(h:Int,m:Int)->Unit,
                           calbackAlarmeAmanha: (h: Int, m: Int) -> Unit){
        if(diasChecagen.diasemana!=diaIgnorado){
           if(checaHorarioParaAgendadmento(horario))
                calbackAlarmeHoje(horario.hora,horario.minuto)
           else if(diasChecagen.prosimoDiasemana!=diaIgnorado)
                   calbackAlarmeAmanha(horario.hora,horario.minuto)
        }else if(diasChecagen.prosimoDiasemana!=diaIgnorado)
                calbackAlarmeAmanha(horario.hora,horario.minuto)



    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun comveteLocalDateDayOfWik(diasemana:String):SemanaDia {
      val s =diasemana.uppercase()

  val comvercao=  when(s){
       "SUNDAY"-> SemanaDia.doming
       "MONDAY"-> SemanaDia.segunda
       "TUESDAY" -> SemanaDia.terca
       "WEDNESDAY" -> SemanaDia.quarta
       "THURSDAY" -> SemanaDia.quinta
       "FRIDAY "-> SemanaDia.sexta
       "SATURDAY" -> SemanaDia.sabado
      else -> {SemanaDia.segunda}
  }
      return comvercao
  }


    @RequiresApi(Build.VERSION_CODES.O)
    fun checarFerias(ferias: Ferias):Boolean{
         Log.i("checando ferias","checando ferias")
        if(ferias.dia==0 && ferias.mes==0 && ferias.ano==0) return false
        Log.i("checando ferias ","usuario selecionou ferias")
        val localDate =LocalDate.now()
        val diaInicioFerias =LocalDate.of(ferias.ano,ferias.mes,ferias.dia)
        val diaFimFerias =LocalDate.of(ferias.anoFim,ferias.mesFim,ferias.diaFim)
        if(localDate.isAfter(diaFimFerias) || localDate.isBefore(diaInicioFerias)) return false
        Log.i("checando ferias","esta de ferias")

       return true

    }

}