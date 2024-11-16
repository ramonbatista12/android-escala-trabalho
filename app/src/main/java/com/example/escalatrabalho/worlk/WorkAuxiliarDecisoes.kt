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
    fun modelo1232(diasOpcionais:DiasOpcionais,
                   diasDeFolgas:List<Int>,
                   diasChecagen:DiasChecagen,
                   horario: HorioDosAlarmes,
                   calbackAlarme:()->Unit){

        Log.i("modelo selecionado","modelo 1232")
        when(diasOpcionais.opicional){

            OpicionalModelo1236.Impar.opcao->{
                Log.i("dias ","dia par")
                if(diasChecagen.dia%2==0){
                    if(!diasDeFolgas.contains(diasChecagen.prosimoDia)){
                        Log.i("dias ","dia nao esta na lista")
                        calbackAlarme()
                    }
                }
                else{
                    Log.i("dias ","alarme e para o dia de hoje ")
                    if(checaHorarioParaAgendadmento(horario)){
                        Log.i("dias ","horario do alarme e depois do atual")
                        calbackAlarme()

                    }

                }


            }
            OpicionalModelo1236.Par.opcao->{
                if(diasChecagen.dia%2!=0){
                    if(!diasDeFolgas.contains(diasChecagen.prosimoDia)){
                        calbackAlarme()
                    }
                }
                else{
                    if(checaHorarioParaAgendadmento(horario)){
                        calbackAlarme()

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
                           calbackAlarme:()->Unit){
        val feriadosDias=feriados.map { it.dia }
          when(diasOpcionais.opicional){
              OpicionalModeloSegSex.Domingos.opcao->{
                  if(diasChecagen.diasemana!= SemanaDia.doming){
                      if(checaHorarioParaAgendadmento(horario)){
                          if(!feriadosDias.contains(diasChecagen.prosimoDia))
                              calbackAlarme()

                      }else{
                          when(diasChecagen.prosimoDiasemana){
                              SemanaDia.segunda->{
                             if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                  calbackAlarme()
                              }
                              SemanaDia.terca->{
                              if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                  calbackAlarme()
                              }
                              SemanaDia.quarta->{
                              if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                  calbackAlarme()
                              }
                              SemanaDia.quinta->{
                              if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                  calbackAlarme
                              }
                              SemanaDia.sexta->{
                              if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                  calbackAlarme()
                              }
                              SemanaDia.doming->{
                               if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                  calbackAlarme()
                              }
                              SemanaDia.sabado->{}

                          }
                      }
                  }
              }
              OpicionalModeloSegSex.Sbados.opcao->{
              if(diasChecagen.diasemana!= SemanaDia.sabado){
                  if(checaHorarioParaAgendadmento(horario)){
                      if(!feriadosDias.contains(diasChecagen.prosimoDia))
                         calbackAlarme()

                  }else{
                      when(diasChecagen.prosimoDiasemana){
                          SemanaDia.segunda->{
                          if(!feriadosDias.contains(diasChecagen.prosimoDia))
                              calbackAlarme()
                          }
                          SemanaDia.terca->{
                          if(!feriadosDias.contains(diasChecagen.prosimoDia))
                              calbackAlarme()
                          }
                          SemanaDia.quarta->{
                              if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                  calbackAlarme()
                          }
                          SemanaDia.quinta->{
                              if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                  calbackAlarme
                          }
                          SemanaDia.sexta->{
                              if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                  calbackAlarme()
                          }
                          SemanaDia.doming->{}
                          SemanaDia.sabado->{
                              if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                 calbackAlarme()
                          }

                      }
                  }
              }}
              OpicionalModeloSegSex.Feriados.opcao->{}
              OpicionalModeloSegSex.Vasios.opcao->{
                  if(diasChecagen.diasemana!= SemanaDia.doming&& diasChecagen.diasemana!= SemanaDia.sabado){
                    if(checaHorarioParaAgendadmento(horario)){
                        if(!feriadosDias.contains(diasChecagen.prosimoDia))
                           calbackAlarme()

                    }else{
                      when(diasChecagen.prosimoDiasemana){
                          SemanaDia.segunda->{
                              if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                 calbackAlarme()
                          }
                          SemanaDia.terca->{
                              if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                 calbackAlarme()
                          }
                          SemanaDia.quarta->{
                              if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                 calbackAlarme()
                          }
                          SemanaDia.quinta->{
                              if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                  calbackAlarme()
                          }
                          SemanaDia.sexta->{
                              if(!feriadosDias.contains(diasChecagen.prosimoDia))
                                  calbackAlarme()
                          }
                          SemanaDia.doming->{}
                          SemanaDia.sabado->{}

                      }
                    }
                  }
              }

          }
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

}