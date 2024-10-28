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
import com.example.escalatrabalho.repositorio.repositoriodeDatas.RepositorioDatas
import com.example.escalatrabalho.repositorio.repositoriodeDatas.SemanaDia
import com.example.escalatrabalho.roomComfigs.Daos
import com.example.escalatrabalho.roomComfigs.DatasFolgas
import com.example.escalatrabalho.roomComfigs.Executad0
import com.example.escalatrabalho.roomComfigs.Feriados
import com.example.escalatrabalho.roomComfigs.Ferias
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import com.example.escalatrabalho.roomComfigs.RoomDb
import com.example.escalatrabalho.viewModel.modelosParaView.visulizacaoDatas
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


//classe repositorioPrincipal responsavel por gerenciar todos os repositorios
class RepositorioPrincipal(val bd: RoomDb) {
   private val tabelaFormatacaoDatas = arrayOf("tab","folg","cp")
   private val repositorioDatas = RepositorioDatasFolgas(bd.dao())
   private val repositorioFeriados = RepositorioFeriados(bd.dao())
   private val repositorioModeloDeTrabalho = RepositorioModeloDeTrabalho(bd.dao())
   private val repositorioHorariosDosAlarmes = RepositorioHorariosDosAlarmes(bd.dao())
   private val repositorioFerias = RepositorioFerias(bd.dao())
   private val repositorioExecutado = RepositorioExecutado(bd.dao())
   val criarDatas = RepositorioDatas()
   val fluxoDatasFolgas = repositorioDatas.select(criarDatas.mes,criarDatas.ano)
   val fluxoDasDatas = criarDatas.getDatas()
   val fluxoModeloDeTrabalho1236 = repositorioModeloDeTrabalho.select("12/36")
       .map {
       if(it.isEmpty()) ModeloDeEScala(1,"12/36",false)
        else it.get(0)
            }
   val fluxoModeloDeTrabalho61 = repositorioModeloDeTrabalho.select("6/1")
       .map {
       if(it.isEmpty()) ModeloDeEScala(2,"6/1",false)
       else it.get(0)
             }
   val fluxoModeloDeTrabalhoSegsext = repositorioModeloDeTrabalho.select("seg-sext")
       .map {
           if(it.isEmpty()) ModeloDeEScala(3,"seg-sext",false)
           else it.get(0)
       }
   val fluxoHorariosDosAlarmes = repositorioHorariosDosAlarmes.select()
   val nomeDomes = criarDatas.getMes()

   val fluxoDatasTrabalhado= combine(
       fluxoDasDatas,
       fluxoDatasFolgas,
       fluxoModeloDeTrabalho1236,
       fluxoModeloDeTrabalho61,
       fluxoModeloDeTrabalhoSegsext ){data,folga,check1236,check61,checkSegsext->
         val _folga:List<Int> =folga.map {

           it.data;
          }
          var l =data.map {

                  when {
                      // Bloco de código para (true, false, false)
                      check1236.check && !check61.check && !checkSegsext.check -> {
                      if(_folga.contains(it.dia.toInt())){
                      visulizacaoDatas(it.dia.toInt(),tabelaFormatacaoDatas[1])
                           }
                      else {
                       if(it.dia.toInt()%2==0)  visulizacaoDatas(it.dia.toInt(),tabelaFormatacaoDatas[2])
                       else visulizacaoDatas(it.dia.toInt(),tabelaFormatacaoDatas[0])
                           }
               }
                //bloco para false,true,false
               !check1236.check && check61.check && !checkSegsext.check -> {

                   if(_folga.contains(it.dia.toInt())){
                       visulizacaoDatas(it.dia.toInt(),tabelaFormatacaoDatas[1])
                   }
                   else {

                       visulizacaoDatas(it.dia.toInt(),tabelaFormatacaoDatas[0])
                   }
               }
               // Bloco de código para (false, false, true)
               !check1236.check && !check61.check && checkSegsext.check -> {
                if(it.diaSemana== SemanaDia.sabado||it.diaSemana== SemanaDia.doming)
                                          visulizacaoDatas(it.dia.toInt(),tabelaFormatacaoDatas[1])
                else visulizacaoDatas(it.dia.toInt(),tabelaFormatacaoDatas[0])
                    }
               //bloco defaut
               else-> visulizacaoDatas(it.dia.toInt(),tabelaFormatacaoDatas[0])
           }

       }
       l
   }
    //inserir e apagar datas de folgas
    suspend fun inserirDatasdefolgas(datasFolgas: DatasFolgas){
        repositorioDatas.insert(datasFolgas)
    }
    suspend fun apargarDataDasFolgas(datasFolgas: DatasFolgas){
        repositorioDatas.delete(datasFolgas)
    }

    //inserir horario dos alarmes
    suspend fun inserirHorariosDosAlarmes(horariosDosAlarmes: HorioDosAlarmes){
        val aux =repositorioHorariosDosAlarmes.count()
        if(aux == 0){
            repositorioHorariosDosAlarmes.insert(horariosDosAlarmes)
        }
        else{
            val obj =repositorioHorariosDosAlarmes.getPrimeiro()
            repositorioHorariosDosAlarmes.update(HorioDosAlarmes(obj.id,horariosDosAlarmes.hora,horariosDosAlarmes.minuto))
        }

    }

    //inserir modelo de trabalho se vasio se nao atualizar
    suspend fun inserirModeloDeTrabalho(modeloDeTrabalho: ModeloDeEScala){
        var count = repositorioModeloDeTrabalho.count()//melhor maneira que achei para saber se alista ta vasia
        if(count==0){ //quando vasia eu crio e insiro os valores do check
            var l =listOf(ModeloDeEScala(1,"12/36",false),ModeloDeEScala(2,"6/1",false),ModeloDeEScala(3,"seg-sext",false))
            repositorioModeloDeTrabalho.insert(l)
        }
        else{//se a lista tiver algun registro eu atualizo o valor do check
            repositorioModeloDeTrabalho.update(modeloDeTrabalho)
        }
    }
}


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
}

//classe RepositorioModeloDeTrabalho responsavel por gerenciar a tabela ModeloDeEScala
class RepositorioModeloDeTrabalho(private val dao: Daos){
    fun select():Flow<List<ModeloDeEScala>> =dao.getModeloDeEScala()
    fun select(colune:String):Flow<List<ModeloDeEScala>> =dao.selectFeriascheck(colune)
    suspend fun count():Int =dao.count()
    suspend fun insert(modeloDeEScala: ModeloDeEScala)=dao.insertModeloDeEScala(modeloDeEScala)
    suspend fun insert(modeloDeEScala: List<ModeloDeEScala>)=dao.insert(modeloDeEScala)
    suspend fun update(modeloDeEScala: ModeloDeEScala)=dao.update(modeloDeEScala)
    suspend fun delete(modeloDeEScala: ModeloDeEScala)=dao.delete(modeloDeEScala)
}

//classe RepositorioHorariosDosAlarmes responsavel por gerenciar a tabela HorioDosAlarmes
class RepositorioHorariosDosAlarmes(private val dao: Daos){
    fun select():Flow<List<HorioDosAlarmes>> =dao.getHorariosDosAlarmes()
    suspend fun getPrimeiro():HorioDosAlarmes= dao.getHorariosPrimeiroDosAlarmes()
    fun count():Int =dao.countHorarios()
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