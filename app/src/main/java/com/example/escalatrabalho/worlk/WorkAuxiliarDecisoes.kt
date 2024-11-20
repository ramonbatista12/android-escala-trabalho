package com.example.escalatrabalho.worlk

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModelo1236
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModelo61
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModeloSegSex
import com.example.escalatrabalho.repositorio.repositoriodeDatas.DiasChecagen
import com.example.escalatrabalho.repositorio.repositoriodeDatas.SemanaDia
import com.example.escalatrabalho.roomComfigs.DiasOpcionais
import com.example.escalatrabalho.roomComfigs.Feriados
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalTime

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
    fun modelo61(diasOpcionais:DiasOpcionais){
        when(diasOpcionais.opicional){
            OpicionalModelo61.Domingo.opcao->{}
            OpicionalModelo61.Segunda.opcao->{}
            OpicionalModelo61.Terca.opcao->{}
            OpicionalModelo61.Quarta.opcao->{}
            OpicionalModelo61.Quinta.opcao->{}
            OpicionalModelo61.Sexta.opcao->{}
            OpicionalModelo61.Vasio.opcao->{}

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
    fun checaHorarioParaAgendadmento(horario:HorioDosAlarmes):Boolean{
       val horarioAtual=LocalTime.now()
       val localTaimer=LocalTime.of(horario.hora,horario.minuto)
       if(horarioAtual.isBefore(localTaimer)){
           return true
       }

     return false
    }

}