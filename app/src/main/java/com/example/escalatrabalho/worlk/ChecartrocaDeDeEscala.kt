package com.example.escalatrabalho.worlk

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.escalatrabalho.applicatio.AplicationCuston
import com.example.escalatrabalho.enums.IdsDeModelosDeEscala
import com.example.escalatrabalho.enums.NomesDeModelosDeEscala
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModelo1236
import com.example.escalatrabalho.repositorio.RepositorioPrincipal
import com.example.escalatrabalho.roomComfigs.DiasOpcionais
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import com.example.escalatrabalho.views.ModeloDeEscala
import kotlinx.coroutines.Job
import java.time.LocalDate

class ChecartrocaDeDeEscala(appContext: Context,
                            params: WorkerParameters
): CoroutineWorker(appContext, params) {
    val job= Job()
    val scop= kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main + job)
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val repositorioPrincipal = RepositorioPrincipal(AplicationCuston.db, AplicationCuston.endpoint)
        val modeloAtivo=repositorioPrincipal.getmodeloObjeto()?:ModeloDeEScala(0,"",false)
        val opcional=repositorioPrincipal.getOpcionaisObjeto(NomesDeModelosDeEscala.Modelo1236.nome)?:DiasOpcionais(0,
                                                                                                                    NomesDeModelosDeEscala.Modelo1236.nome,
                                                                                                                    OpicionalModelo1236.Vasio.opcao)
        when(modeloAtivo.modelo){
            NomesDeModelosDeEscala.Modelo61.nome->{}
            NomesDeModelosDeEscala.ModeloSegSex.nome->{}
            NomesDeModelosDeEscala.Modelo1236.nome->{
                val hoje= LocalDate.now()
                val onten=hoje.minusDays(1)
                if(hoje.dayOfMonth==1 && onten.dayOfMonth==31){
                  val opcao= when(modeloAtivo.modelo){
                      OpicionalModelo1236.Par.opcao-> OpicionalModelo1236.Impar
                      OpicionalModelo1236.Impar.opcao-> OpicionalModelo1236.Par
                      else->OpicionalModelo1236.Vasio
                  }
                    repositorioPrincipal.inserirOpcionais(
                       DiasOpcionais(id=IdsDeModelosDeEscala.IdModelo1236.id,modelo=NomesDeModelosDeEscala.Modelo1236.nome,opicional=opcao.opcao)
                   )


                }


            }
            else->{}
        }



        return Result.success()
    }
}