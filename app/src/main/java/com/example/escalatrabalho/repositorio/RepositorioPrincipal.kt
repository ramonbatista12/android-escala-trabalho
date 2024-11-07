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
import android.util.Log
import com.example.escalatrabalho.classesResultados.Requisicaoweb

import com.example.escalatrabalho.repositorio.repositoriodeDatas.RepositorioDatas
import com.example.escalatrabalho.repositorio.repositoriodeDatas.SemanaDia
import com.example.escalatrabalho.retrofit.CalendarioApi
import com.example.escalatrabalho.retrofit.CalendarioApiService
import com.example.escalatrabalho.roomComfigs.Daos
import com.example.escalatrabalho.roomComfigs.DatasFolgas
import com.example.escalatrabalho.roomComfigs.Executad0
import com.example.escalatrabalho.roomComfigs.Feriados
import com.example.escalatrabalho.roomComfigs.Ferias
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import com.example.escalatrabalho.roomComfigs.RoomDb
import com.example.escalatrabalho.viewModel.modelosParaView.visulizacaoDatas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


//classe repositorioPrincipal responsavel por gerenciar todos os repositorios
class RepositorioPrincipal(val bd: RoomDb,val datasFeriados: CalendarioApi) {
    val escopo = CoroutineScope(Dispatchers.IO)
    // variaveis privadas pois as outras classes nao presisao delas
    private val tabelaFormatacaoDatas = arrayOf("tab", "folg", "cp")                    //tabela representa trabalhado folga compensado
    private val repositorioDatas = RepositorioDatasFolgas(bd.dao())                     //instansia dos repositorio :folgas
    private val repositorioFeriados = RepositorioFeriados(bd.dao())                     //feriados
    private val repositorioModeloDeTrabalho = RepositorioModeloDeTrabalho(bd.dao())     //modelo de trabalho
    private val repositorioHorariosDosAlarmes = RepositorioHorariosDosAlarmes(bd.dao()) // horario dos trabalhod
    private val repositorioFerias = RepositorioFerias(bd.dao())                         //ferias
    private val repositorioExecutado = RepositorioExecutado(bd.dao())                   // executado
    private val criarDatas = RepositorioDatas()                                         //  instansia da classe que cria as datas



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
            val fluxoFeriados =repositorioFeriados.get(criarDatas.mes+1)                                           // fluxo com os feriados do mes
            val fluxoModeloDeEscalaAtivo = repositorioModeloDeTrabalho.fluxoModeloDeEscalaAtivo().map {
                if(it==null) ModeloDeEScala(1, "defaut", false)
                else it
            }        // fluxo com o modelo de trabalho ativo
            val fluxoDatasTrabalhado = combine(                                                                    // fluxo combinado o repositorio o cria para formatar os dados
                                              fluxoDasDatas,
                                              fluxoDatasFolgas,
                                              fluxoModeloDeEscalaAtivo,
                                               ){

                   data, folga, modeloAtivo  ->

                    val _folga: List<Int> = folga.map { it.data; }
                    var l = data.map {
                                  when(modeloAtivo!!.modelo) {    //checa a string do modelo ativo
                                        "12/36" -> {
                                             if (_folga.contains(it.dia.toInt())) {
                                                  visulizacaoDatas(it.dia.toInt(),
                                                                   tabelaFormatacaoDatas[1])
                                                                                   }
                                             else {
                                                 if (it.dia.toInt() % 2 == 0)
                                                     visulizacaoDatas(it.dia.toInt(),
                                                                      tabelaFormatacaoDatas[2]
                                                                       )
                                                 else
                                                     visulizacaoDatas(it.dia.toInt(),
                                                                      tabelaFormatacaoDatas[0])
                                                  }
                                                     }

                                        "6/1"-> {

                                               if (_folga.contains(it.dia.toInt())) {
                                                        visulizacaoDatas(it.dia.toInt(),
                                                                         tabelaFormatacaoDatas[1])
                                                                                     }
                                               else
                                                       visulizacaoDatas(it.dia.toInt(),
                                                                        tabelaFormatacaoDatas[0])

                                                }

                                        "seg-sext" -> {
                                               if (it.diaSemana == SemanaDia.sabado || it.diaSemana == SemanaDia.doming)
                                                       visulizacaoDatas(it.dia.toInt(),
                                                                        tabelaFormatacaoDatas[1])
                                               else visulizacaoDatas(it.dia.toInt(),
                                                                     tabelaFormatacaoDatas[0])
                                                        }

                                        else -> visulizacaoDatas(it.dia.toInt(),
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




        //inserir modelo de trabalho se vasio se nao atualizar
        suspend fun inserirModeloDeTrabalho(modeloDeTrabalho: ModeloDeEScala) {
            var count =repositorioModeloDeTrabalho.count()//melhor maneira que achei para saber se alista ta vasia
            if (count == 0) { //quando vasia eu crio e insiro os valores do check
                var l = listOf(
                    ModeloDeEScala(1, "12/36", false),
                    ModeloDeEScala(2, "6/1", false),
                    ModeloDeEScala(3, "seg-sext", false))
                    repositorioModeloDeTrabalho.insert(l)
                            }

            else
                repositorioModeloDeTrabalho.update(modeloDeTrabalho) //se a lista tiver algun registro eu atualizo o valor do check

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
            fun select():Flow<List<Ferias>> =dao.getFerias()
    suspend fun insert(ferias: Ferias)=dao.insertFerias(ferias)
    suspend fun update(ferias: Ferias)=dao.update(ferias)
    suspend fun delete(ferias: Ferias)=dao.delete(ferias)
                                               }

//classe RepositorioExecutado responsavel por gerenciar a tabela Executado
class RepositorioExecutado(private val dao: Daos){
            fun select():Flow<List<Executad0>> =dao.getExecutado()
    suspend fun insert(executad0: Executad0)=dao.insertExecutado(executad0)
    suspend fun update(executad0: Executad0)=dao.update(executad0)
    suspend fun delete(executad0: Executad0)=dao.delete(executad0)
                                                  }