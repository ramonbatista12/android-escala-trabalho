package com.example.escalatrabalho.roomComfigs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Daos {
    //referem a tabela folgas
    @Query("SELECT * FROM DatasFolgas where ano=:ano and mes=:mes" )
    fun getDatasFolgas(ano:Int, mes:Int): Flow<List<DatasFolgas>>
    @Insert
    suspend fun insertFolgas(vararg datasFolgas: DatasFolgas)
    @Delete
    suspend fun delete(datasFolgas: DatasFolgas)
    @Update
    suspend fun update(datasFolgas: DatasFolgas)

    //referem a tabela feriados
    @Query("SELECT * FROM Feriados where mes=:mes")
    fun getFeriados(mes:Int): Flow<List<Feriados>>
    @Query("SELECT Count(*) FROM Feriados")
    fun comtaferiados(): Int
    @Insert
    suspend fun insertFeriados(vararg feriados: Feriados)
    @Delete
    suspend fun delete(feriados: Feriados)
    @Update
    suspend fun update(feriados: Feriados)

    //comfiguracoes

    //tabela executando
    @Query("SELECT * FROM executad0")
    fun getExecutado(): Flow<List<Executad0>>
    @Insert
    suspend fun insertExecutado(vararg executad0: Executad0)
    @Delete
    suspend fun delete(executad0: Executad0)
    @Update
    suspend fun update(executad0: Executad0)

    //tabela modelo de trabalho
    @Query("SELECT * FROM ModeloDeEScala")
    fun getModeloDeEScala(): Flow<List<ModeloDeEScala>>
    @Query("SELECT COUNT (*) FROM ModeloDeEScala")
    suspend fun count(): Int
    @Query("SELECT  * FROM ModeloDeEScala where modelo=:colune")
    fun selectFeriascheck(colune:String): Flow<List<ModeloDeEScala>>
    @Insert
    suspend fun insertModeloDeEScala(vararg modeloDeEScala: ModeloDeEScala)
    @Insert
    suspend fun insert(modeloDeEScala: List< ModeloDeEScala>)
    @Delete
    suspend fun delete(modeloDeEScala: ModeloDeEScala)
    @Update
    suspend fun update(modeloDeEScala: ModeloDeEScala)
    @Update
    suspend fun update(modeloDeEScala: List<ModeloDeEScala>)

    //horario dos alarmes
    @Query("select * from HorioDosAlarmes")
    fun getHorariosDosAlarmes(): Flow<List<HorioDosAlarmes>>
    @Query("select * from HorioDosAlarmes limit 1")
    fun getHorariosPrimeiroDosAlarmes(): HorioDosAlarmes
    @Query("select COUNT (*) from HorioDosAlarmes ")
    fun countHorarios():Int
    @Insert
    suspend fun insertHorariosDosAlarmes(vararg horioDosAlarmes: HorioDosAlarmes)
    @Delete
    suspend fun delete(horioDosAlarmes: HorioDosAlarmes)
    @Update
    suspend fun updateAlarme(horioDosAlarmes: HorioDosAlarmes)

    //ferias
    @Query("select * from Ferias")
    fun getFerias(): Flow<List<Ferias>>
    @Insert
    suspend fun insertFerias(vararg ferias: Ferias)
    @Delete
    suspend fun delete(ferias: Ferias)
    @Update
    suspend fun update(ferias: Ferias)













}