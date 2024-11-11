package com.example.escalatrabalho.repositorio
/*
* esse arquivo e responsavel por gerenciar todos os repositorios ele contem uma clsse repositorio principal
* pois ela e quem vai fasilitar o uso dos repositorio
* escolhi motar os repositorios separados pois asim fica mais facil usar no viewmodel
* o repositorio principal e quem gerencia todos os repositorios
* aqui contem os sequintes repositorio
* principal,
* repositorioDatasFolgas
* ,repositorioFeriados,
* repositorioModeloDeTrabalho
* ,repositorioHorariosDosAlarmes
* ,repositorioFerias
* */
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.example.escalatrabalho.classesResultados.Requisicaoweb
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModelo1236
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModeloSegSex

import com.example.escalatrabalho.repositorio.repositoriodeDatas.RepositorioDatas
import com.example.escalatrabalho.repositorio.repositoriodeDatas.SemanaDia
import com.example.escalatrabalho.retrofit.CalendarioApi
import com.example.escalatrabalho.retrofit.CalendarioApiService
import com.example.escalatrabalho.roomComfigs.Daos
import com.example.escalatrabalho.roomComfigs.DatasFolgas
import com.example.escalatrabalho.roomComfigs.DiasOpcionais
import com.example.escalatrabalho.roomComfigs.Executad0
import com.example.escalatrabalho.roomComfigs.Feriados
import com.example.escalatrabalho.roomComfigs.Ferias
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import com.example.escalatrabalho.roomComfigs.RoomDb
import com.example.escalatrabalho.viewModel.modelosParaView.FeriasView
import com.example.escalatrabalho.viewModel.modelosParaView.visulizacaoDatas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//classe repositorioPrincipal responsavel por gerenciar todos os repositorios
class RepositorioPrincipal(val bd: RoomDb,val datasFeriados: CalendarioApi) {
    val escopo = CoroutineScope(Dispatchers.IO)
    // variaveis privadas pois as outras classes nao presisao delas
    private val tabelaFormatacaoDatas = arrayOf("tb", "fg", "cp")                      //tabela representa trabalhado folga compensado
    private val repositorioDatas = RepositorioDatasFolgas(bd.dao())                     //instansia dos repositorio :folgas
    private val repositorioFeriados = RepositorioFeriados(bd.dao())                     //feriados
    private val repositorioModeloDeTrabalho = RepositorioModeloDeTrabalho(bd.dao())     //modelo de trabalho
    private val repositorioHorariosDosAlarmes = RepositorioHorariosDosAlarmes(bd.dao()) // horario dos trabalhod
    private val repositorioFerias = RepositorioFerias(bd.dao())                         //Ferias
    private val repositorioExecutado = RepositorioExecutado(bd.dao())                   // executado
    private val criarDatas = RepositorioDatas()                                         //  instansia da classe que cria as datas
    private val repositorioOpcionais = RepositorioOpcionais(bd.dao())                   //opcionais representam os opicionais exemplo 12/36 podem trabalhar em dias impare ou pares


              //fluxos que vao ser pasados as view atraves dos view models se ouver
            val fluxoDatasFolgas = repositorioDatas.select(criarDatas.mes, criarDatas.ano)                         //esse e o fluxo das datas de folgas
            val fluxoDasDatas = criarDatas.getDatas()                                                              //datas vindas da classe que cria as datas
            val fluxoModeloDeTrabalho1236 = repositorioModeloDeTrabalho.select("12/36").map {
                if(it==null) ModeloDeEScala(1, "12/36", false)
                else it
            }           //fluxo modelo de trabalho do tipo 1236
            val fluxoModeloDeTrabalho61 = repositorioModeloDeTrabalho.select("6/1").map {
                if(it==null) ModeloDeEScala(2, "6/1", false)
                else it
            }               //fluxo modelo de trabaalho do tipo 6/1
            val fluxoModeloDeTrabalhoSegsext = repositorioModeloDeTrabalho.select("seg-sext").map {
                if(it==null) ModeloDeEScala(3, "seg-sext", false)
                else it
            }     //fluxo modelo de trabalho do tipo seg-sexta
            val fluxoHorariosDosAlarmes = repositorioHorariosDosAlarmes.select()                                   // fluxo dos horarios dos alarmes
            val nomeDomes = criarDatas.getMes()                                                                    // string com o nome do mes
            val numeroMes = criarDatas.mes                                                                         // numero do mes
            val fluxoFeriados =repositorioFeriados.get(criarDatas.mes+1)                                           // fluxo com os feriados do mes
            val fluxoModeloDeEscalaAtivo = repositorioModeloDeTrabalho.fluxoModeloDeEscalaAtivo().map {
                if(it==null) ModeloDeEScala(1, "defaut", false)
                else it
            }        // fluxo com o modelo de trabalho ativo
            val fluxoOpcionais=fluxoModeloDeEscalaAtivo.flatMapLatest {
               flow<DiasOpcionais?>  {
                  while(true){
                      if(it==null){
                    emit(DiasOpcionais(id=0,"",OpicionalModelo1236.Vasio.opcao))
                   } else{
                       val aux = withContext(Dispatchers.IO){
                           repositorioOpcionais.selectDiaOpcional(it.modelo)
                       }
                       if(aux==null) emit(DiasOpcionais(id=0,"",OpicionalModelo1236.Vasio.opcao))
                       else emit(aux)

                   }
                      delay(1000)
                  }
               }
            }                                        //fluxo com os opcionais eles representao as opcoes para um determinado modelo de escala
            val fluxoDeferias = repositorioFerias.select().map {
                if(it==null) FeriasView(0,0,0,0,0,0,0,false)
                else
                FeriasView(id=it!!.id,
                    diaInicio=it!!.dia,
                    diaFim=it!!.diaFim,
                    mesInici = it!!.mes,
                    mesFim = it!!.mesFim,
                    anoInici = it!!.ano,
                    anoFim = it!!.anoFim,
                    check = true)
            }                                                //fluxo das Ferias


             val fluxoDatasTrabalhado = combine(                                                                    // fluxo combinado o repositorio o cria para formatar os dados
                                              fluxoDasDatas,
                                              fluxoDatasFolgas,
                                              fluxoModeloDeEscalaAtivo,
                                              fluxoOpcionais,
                                              fluxoFeriados
                                               ){

                   data, folga, modeloAtivo ,opcional,feriados ->

                    val _folga: List<Int> = folga.map { it.data; }
                    val _feriados: List<Int> = feriados.map { it.dia; }
                    var l = data.map {
                                  when(modeloAtivo!!.modelo) {    //checa a string do modelo ativo
                                        "12/36" -> {
                                             if (_folga.contains(it.dia.toInt())) {
                                                  visulizacaoDatas(it.dia.toInt(),
                                                                   it.mes.toInt(),
                                                                   tabelaFormatacaoDatas[1])
                                                                                   }
                                             else {
                                                 if(opcional!!.opicional == OpicionalModelo1236.Par.opcao)
                                                 {
                                                 if (it.dia.toInt() % 2 == 0)
                                                     visulizacaoDatas(it.dia.toInt(),
                                                                      it.mes.toInt(),
                                                                      tabelaFormatacaoDatas[0]
                                                                       )
                                                 else
                                                     visulizacaoDatas(it.dia.toInt(),
                                                                      it.mes.toInt(),
                                                                      tabelaFormatacaoDatas[2])
                                                  } else if(opcional.opicional == OpicionalModelo1236.Impar.opcao){
                                                     if (it.dia.toInt() % 2 != 0)
                                                         visulizacaoDatas(it.dia.toInt(),
                                                                          it.mes.toInt(),
                                                                          tabelaFormatacaoDatas[0]
                                                         )
                                                     else
                                                         visulizacaoDatas(it.dia.toInt(),
                                                                          it.mes.toInt(),
                                                                          tabelaFormatacaoDatas[2])
                                                  } else visulizacaoDatas(it.dia.toInt(),
                                                                          it.mes.toInt(),
                                                                          tabelaFormatacaoDatas[0])
                                             }
                                                     }

                                        "6/1"-> {

                                               if (_folga.contains(it.dia.toInt())) {
                                                        visulizacaoDatas(it.dia.toInt(),
                                                                         it.mes.toInt(),
                                                                         tabelaFormatacaoDatas[1])
                                                                                     }
                                               else
                                                       visulizacaoDatas(it.dia.toInt(),
                                                                        it.mes.toInt(),
                                                                        tabelaFormatacaoDatas[0])

                                                }

                                        "seg-sext" -> {

                                            if (it.diaSemana == SemanaDia.sabado) {
                                                if (opcional!!.opicional == OpicionalModeloSegSex.Sbados.opcao)
                                                    visulizacaoDatas(
                                                        it.dia.toInt(),
                                                        it.mes.toInt(),
                                                        tabelaFormatacaoDatas[0]
                                                    )
                                                else visulizacaoDatas(
                                                    it.dia.toInt(),
                                                     it.mes.toInt(),
                                                    tabelaFormatacaoDatas[1]
                                                )                              }

                                                else if (it.diaSemana == SemanaDia.doming) {
                                                    if (opcional!!.opicional == OpicionalModeloSegSex.Domingos.opcao) {
                                                        visulizacaoDatas(
                                                            it.dia.toInt(),
                                                            it.mes.toInt(),
                                                            tabelaFormatacaoDatas[0]
                                                        )
                                                    } else visulizacaoDatas( it.dia.toInt(),
                                                                             it.mes.toInt(),
                                                                             tabelaFormatacaoDatas[1]
                                                    )
                                                                                         }
                                                else if(_folga.contains(it.dia.toInt())){
                                                     if (opcional!!.opicional == OpicionalModeloSegSex.Feriados.opcao)
                                                              visulizacaoDatas(it.dia.toInt(),
                                                                               it.mes.toInt(),
                                                                               tabelaFormatacaoDatas[0])
                                                     else visulizacaoDatas(it.dia.toInt(),
                                                                           it.mes.toInt(),
                                                                           tabelaFormatacaoDatas[1])

                                                }
                                             else visulizacaoDatas( it.dia.toInt(),
                                                                    it.mes.toInt(),
                                                                    tabelaFormatacaoDatas[0])





                                        }

                                        else -> visulizacaoDatas(it.dia.toInt(),
                                                                 it.mes.toInt(),
                                                                 tabelaFormatacaoDatas[0])
                                   }

                                        }
        l
                                               }



        suspend fun checarRepositorioFeriados(): Int{
           return repositorioFeriados.contaFeriados()
                                                    }
        suspend fun caregarDatasFeriados():Requisicaoweb{
            try{
                val l = datasFeriados.getDatas(2025)
                l.map {
                    val split = it.date.split("-")
                    val ano = split[0].toInt()
                    val mes = split[1].toInt()
                    val dia = split[2].toInt()
                    repositorioFeriados.insert(Feriados(id=0,
                                                        dia = dia,
                                                        mes=mes,
                                                        ano=ano,
                                                        descricao =  it.name))
                       }
                }
            catch (e:Exception){
                return Requisicaoweb.erro(e.message!!)
                               }
            return Requisicaoweb.ok

                                                          }

        //inserir e apagar datas de folgas
        suspend fun inserirDatasdefolgas(datasFolgas: DatasFolgas) {
            repositorioDatas.insert(datasFolgas)
                                                                   }

        suspend fun apargarDataDasFolgas(datasFolgas: DatasFolgas) {
            repositorioDatas.delete(datasFolgas)
                                                                   }

        //inserir horario dos alarmes
        suspend fun inserirHorariosDosAlarmes(horariosDosAlarmes: HorioDosAlarmes) {
            val aux = repositorioHorariosDosAlarmes.count()
            if (aux == 0)
                repositorioHorariosDosAlarmes.insert(horariosDosAlarmes)
            else {
                val obj = repositorioHorariosDosAlarmes.getPrimeiro()
                repositorioHorariosDosAlarmes.update(HorioDosAlarmes(id = obj.id,
                                                                    hora = horariosDosAlarmes.hora,
                                                                    minuto = horariosDosAlarmes.minuto))
                 }

                                                                                    }

        suspend fun apagarFerias(ferias: FeriasView){
            repositorioFerias.delete(Ferias(id=ferias.id,
                                            ano = ferias.anoInici,
                                            anoFim = ferias.anoFim,
                                            mes = ferias.mesInici,
                                            mesFim = ferias.mesFim,
                                            dia = ferias.diaInicio,
                                            diaFim = ferias.diaFim))
        }
        suspend fun apagarFerias(){
            repositorioFerias.delete()
        }

       suspend fun inserirFerias(ferias: FeriasView){
            repositorioFerias.insert(Ferias(id=0,
                                            ano = ferias.anoInici,
                                            anoFim = ferias.anoFim,
                                            mes =ferias.mesInici,
                                            mesFim = ferias.mesFim,
                                            dia = ferias.diaInicio,
                                            diaFim = ferias.diaFim))
       }



        //inserir modelo de trabalho se vasio se nao atualizar
        @SuppressLint("SuspiciousIndentation")
        suspend fun inserirModeloDeTrabalho(modeloDeTrabalho: ModeloDeEScala) {
            var count =repositorioModeloDeTrabalho.count()//melhor maneira que achei para saber se alista ta vasia
            if (count == 0) { //quando vasia eu crio e insiro os valores do check
                var l = listOf(
                    ModeloDeEScala(1, "12/36", false),
                    ModeloDeEScala(2, "6/1", false),
                    ModeloDeEScala(3, "seg-sext", false))
                    repositorioModeloDeTrabalho.insert(l)
                            }


                 when{
                     modeloDeTrabalho.modelo =="12/36" && modeloDeTrabalho.check->{
                         var l = listOf(
                             ModeloDeEScala(1, "12/36", true),
                             ModeloDeEScala(2, "6/1", false),
                             ModeloDeEScala(3, "seg-sext", false))
                         repositorioModeloDeTrabalho.updatelista(l)
                     }
                     modeloDeTrabalho.modelo=="6/1" && modeloDeTrabalho.check->{
                         var l = listOf(
                             ModeloDeEScala(1, "12/36", false),
                             ModeloDeEScala(2, "6/1", true),
                             ModeloDeEScala(3, "seg-sext", false))
                         repositorioModeloDeTrabalho.updatelista(l)
                     }
                     modeloDeTrabalho.modelo=="seg-sext" && modeloDeTrabalho.check->{
                         var l = listOf(
                             ModeloDeEScala(1, "12/36", false),
                             ModeloDeEScala(2, "6/1", false),
                             ModeloDeEScala(3, "seg-sext", true))
                         repositorioModeloDeTrabalho.updatelista(l)
                     }
                     else ->{repositorioModeloDeTrabalho.update(modeloDeTrabalho)}

                 }


                 //se a lista tiver algun registro eu atualizo o valor do check

                                                                                 }

        suspend fun inserirOpcionais(diasOpcionais: DiasOpcionais){
            val contagem =repositorioOpcionais.contaDiasOpcionais()
            if(contagem==0){//se vasio eu insiro
                val lista = arrayListOf<DiasOpcionais>(
                    DiasOpcionais(id=1,"12/36",OpicionalModelo1236.Vasio.opcao),
                    DiasOpcionais(id=2,"6/1",OpicionalModelo1236.Vasio.opcao),
                    DiasOpcionais(id=3,"seg-sext",OpicionalModelo1236.Vasio.opcao)
                )
                repositorioOpcionais.inserirLista(lista)

                            }

                repositorioOpcionais.update(diasOpcionais)

        }

}//fim da classe



//classe RepositorioDatasFolgas responsavel por gerenciar a tabela DatasFolgas
class RepositorioDatasFolgas(private val dao: Daos){
            fun select(mes:Int,ano:Int):Flow<List<DatasFolgas>> =dao.getDatasFolgas(ano,mes)
    suspend fun insert(datasFolgas: DatasFolgas)=dao.insertFolgas(datasFolgas)
    suspend fun delete(datasFolgas: DatasFolgas)=dao.delete(datasFolgas)
    suspend fun update(datasFolgas: DatasFolgas)=dao.update(datasFolgas)
                                                   }

//classe RepositorioFeriados responsavel por gerenciar a tabela Feriados
class RepositorioFeriados(private val dao: Daos){
             fun get(mes:Int):Flow<List<Feriados>> =dao.getFeriados(mes)
    suspend fun get(mes:Int,dia:Int):Flow<Feriados?> =dao.getFeriados(mes,dia)
     suspend fun insert(feriados: Feriados)=dao.insertFeriados(feriados)
     suspend fun update(feriados: Feriados)=dao.update(feriados)
     suspend fun delete(feriados: Feriados)=dao.delete(feriados)
     suspend fun contaFeriados()=dao.comtaferiados()
                                                }

//classe RepositorioModeloDeTrabalho responsavel por gerenciar a tabela ModeloDeEScala
class RepositorioModeloDeTrabalho(private val dao: Daos){
            fun select():Flow<List<ModeloDeEScala>> =dao.getModeloDeEScala()
            fun select(colune:String):Flow<ModeloDeEScala?> =dao.selectFeriascheck(colune)
            fun fluxoModeloDeEscalaAtivo():Flow<ModeloDeEScala?> =dao.selectModeloDeEscalaAtivo()
    suspend fun count():Int =dao.count()
    suspend fun insert(modeloDeEScala: ModeloDeEScala)=dao.insertModeloDeEScala(modeloDeEScala)
    suspend fun insert(modeloDeEScala: List<ModeloDeEScala>)=dao.insert(modeloDeEScala)
    suspend fun update(modeloDeEScala: ModeloDeEScala)=dao.update(modeloDeEScala)
    suspend fun updatelista(modeloDeEScala: List<ModeloDeEScala>)=dao.updateLista(modeloDeEScala)
    suspend fun delete(modeloDeEScala: ModeloDeEScala)=dao.delete(modeloDeEScala)
                                                          }

//classe RepositorioHorariosDosAlarmes responsavel por gerenciar a tabela HorioDosAlarmes
class RepositorioHorariosDosAlarmes(private val dao: Daos){
            fun select():Flow<List<HorioDosAlarmes>> =dao.getHorariosDosAlarmes()
            fun count():Int =dao.countHorarios()
    suspend fun getPrimeiro():HorioDosAlarmes= dao.getHorariosPrimeiroDosAlarmes()
    suspend fun insert(horioDosAlarmes: HorioDosAlarmes)=dao.insertHorariosDosAlarmes(horioDosAlarmes)
    suspend fun update(horioDosAlarmes: HorioDosAlarmes)=dao.updateAlarme(horioDosAlarmes)
    suspend fun delete(horioDosAlarmes: HorioDosAlarmes)=dao.delete(horioDosAlarmes)
                                                           }

//classe RepositorioFerias responsavel por gerenciar a tabela Ferias
class RepositorioFerias(private val dao: Daos){
            fun select():Flow<Ferias?> =dao.getFerias()
    suspend fun insert(ferias: Ferias)=dao.insertFerias(ferias)
    suspend fun update(ferias: Ferias)=dao.update(ferias)
    suspend fun delete(ferias: Ferias)=dao.delete(ferias)
    suspend fun delete()=dao.deleteFerias()
                                               }

//classe RepositorioExecutado responsavel por gerenciar a tabela Executado
class RepositorioExecutado(private val dao: Daos){
            fun select():Flow<List<Executad0>> =dao.getExecutado()
    suspend fun insert(executad0: Executad0)=dao.insertExecutado(executad0)
    suspend fun update(executad0: Executad0)=dao.update(executad0)
    suspend fun delete(executad0: Executad0)=dao.delete(executad0)
                                                  }

//clase Repositorio opcionais responsavel pelas opcoes dos modelos de escala
class RepositorioOpcionais(private val dao: Daos){
          fun select(modelo:String):Flow<List<DiasOpcionais>> =dao.getDiasOpcionais(modelo)
          fun selectDiaOpcional(modelo:String):DiasOpcionais? =dao.getDiaOpcionaisMes(modelo)
  suspend fun contaDiasOpcionais():Int =dao.countDiasOpcionais()
  suspend fun insert(diasOpcionais: DiasOpcionais)=dao.insertDiasOpcionais(diasOpcionais)
  suspend fun inserirLista(diasOpcionais: List<DiasOpcionais>)=dao.inserirListaDeOpcionais(diasOpcionais)
  suspend fun update(diasOpcionais: DiasOpcionais)=dao.update(diasOpcionais)
  suspend fun delete(diasOpcionais: DiasOpcionais)=dao.delete(diasOpcionais)
}