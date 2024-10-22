package com.example.escalatrabalho.roomComfigs.repositorio

import com.example.escalatrabalho.roomComfigs.DatasFolgas
import com.example.escalatrabalho.roomComfigs.Executad0
import com.example.escalatrabalho.roomComfigs.Feriados
import com.example.escalatrabalho.roomComfigs.Ferias
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import com.example.escalatrabalho.roomComfigs.RoomDb
import kotlinx.coroutines.flow.Flow


//classe repositorioPrincipal responsavel por gerenciar todos os repositorios
class RepositorioPrincipal(val bd: RoomDb) {
    val repositorioDatas =RepositorioDatasFolgas(bd)
    val repositorioFeriados =RepositorioFeriados(bd)
    val repositorioModeloDeTrabalho =RepositorioModeloDeTrabalho(bd)
    val repositorioHorariosDosAlarmes =RepositorioHorariosDosAlarmes(bd)
    val repositorioFerias =RepositorioFerias(bd)

}

//classe RepositorioDatasFolgas responsavel por gerenciar a tabela DatasFolgas
class RepositorioDatasFolgas(val bd: RoomDb){
    fun select(mes:Int,ano:Int):Flow<List<DatasFolgas>> =bd.dao().getDatasFolgas(ano,mes)
    suspend fun insert(datasFolgas: DatasFolgas)=bd.dao().insertFolgas(datasFolgas)
    suspend fun delete(datasFolgas: DatasFolgas)=bd.dao().delete(datasFolgas)
    suspend fun update(datasFolgas: DatasFolgas)=bd.dao().update(datasFolgas)
}

//classe RepositorioFeriados responsavel por gerenciar a tabela Feriados
class RepositorioFeriados(val bd: RoomDb){
     fun get(mes:Int):Flow<List<Feriados>> =bd.dao().getFeriados(mes)
     suspend fun insert(feriados: Feriados)=bd.dao().insertFeriados(feriados)
     suspend fun update(feriados: Feriados)=bd.dao().update(feriados)
     suspend fun delete(feriados: Feriados)=bd.dao().delete(feriados)
}

//classe RepositorioModeloDeTrabalho responsavel por gerenciar a tabela ModeloDeEScala
class RepositorioModeloDeTrabalho(val bd: RoomDb){
    fun select():Flow<List<ModeloDeEScala>> =bd.dao().getModeloDeEScala()
    fun select(colune:String):Flow<List<ModeloDeEScala>> =bd.dao().selectFeriascheck(colune)
    suspend fun count():Int =bd.dao().count()
    suspend fun insert(modeloDeEScala: ModeloDeEScala)=bd.dao().insertModeloDeEScala(modeloDeEScala)
    suspend fun insert(modeloDeEScala: List<ModeloDeEScala>)=bd.dao().insert(modeloDeEScala)
    suspend fun update(modeloDeEScala: ModeloDeEScala)=bd.dao().update(modeloDeEScala)
    suspend fun delete(modeloDeEScala: ModeloDeEScala)=bd.dao().delete(modeloDeEScala)
}

//classe RepositorioHorariosDosAlarmes responsavel por gerenciar a tabela HorioDosAlarmes
class RepositorioHorariosDosAlarmes(val bd: RoomDb){
    fun select():Flow<List<HorioDosAlarmes>> =bd.dao().getHorariosDosAlarmes()
    suspend fun insert(horioDosAlarmes: HorioDosAlarmes)=bd.dao().insertHorariosDosAlarmes(horioDosAlarmes)
    suspend fun update(horioDosAlarmes: HorioDosAlarmes)=bd.dao().update(horioDosAlarmes)
    suspend fun delete(horioDosAlarmes: HorioDosAlarmes)=bd.dao().delete(horioDosAlarmes)
}

//classe RepositorioFerias responsavel por gerenciar a tabela Ferias
class RepositorioFerias(val bd: RoomDb){
    fun select():Flow<List<Ferias>> =bd.dao().getFerias()
    suspend fun insert(ferias: Ferias)=bd.dao().insertFerias(ferias)
    suspend fun update(ferias: Ferias)=bd.dao().update(ferias)
    suspend fun delete(ferias: Ferias)=bd.dao().delete(ferias)
}

//classe RepositorioExecutado responsavel por gerenciar a tabela Executado
class RepositorioExecutado(val bd: RoomDb){
    fun select():Flow<List<Executad0>> =bd.dao().getExecutado()
    suspend fun insert(executad0: Executad0)=bd.dao().insertExecutado(executad0)
    suspend fun update(executad0: Executad0)=bd.dao().update(executad0)
    suspend fun delete(executad0: Executad0)=bd.dao().delete(executad0)


}