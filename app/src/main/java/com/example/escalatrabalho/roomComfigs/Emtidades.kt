package com.example.escalatrabalho.roomComfigs

import androidx.compose.runtime.Composable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//entidades do banco de dados responsavel pelas datas de folgas
@Entity
data class DatasFolgas(@PrimaryKey(autoGenerate = true) val id:Int,
                       @ColumnInfo(name = "data")val data:Int,
                       @ColumnInfo(name="mes") val mes:Int,
                       @ColumnInfo(name="ano") val ano:Int)
//entidades do banco de dados responsavel pelos feriados
@Entity
data class Feriados(@PrimaryKey(autoGenerate = true) val id:Int,
                    @ColumnInfo(name = "dia")val dia:Int,
                    @ColumnInfo(name="mes") val mes:Int,
                    @ColumnInfo(name="ano") val ano:Int,
                    @ColumnInfo(name="discricao") val descricao:String)
//entidades do banco de dados responsavel pelas configuracoes
//entidae responsavel por saber se ja foi esecutado
@Entity
data class Executad0(@PrimaryKey(autoGenerate = true) val id:Int,
                     @ColumnInfo(name = "ezecutado") val s:String)
//entidade responsavel por gerenciar os modelos de Escalas 12/36,6/1,seg-sext
@Entity
data class ModeloDeEScala(@PrimaryKey(autoGenerate = true)val id:Int,
                          @ColumnInfo(name = "modelo") val modelo:String,
                           @ColumnInfo(name = "check") val check:Boolean,)
//entidade responsavel por gerenciar os horarios dos alarmes
@Entity
data class HorioDosAlarmes(@PrimaryKey(autoGenerate = true) val id:Int,
                           @ColumnInfo(name = "hora")val hora:Int,
                           @ColumnInfo(name = "minuto")val minuto:Int)
//entidade responsavel por gerenciar as datas de ferias
@Entity
data class Ferias(@PrimaryKey(autoGenerate = true) val id:Int,
                  @ColumnInfo(name = "ano")val ano:Int,
                  @ColumnInfo(name = "mes")val mes:Int,
                  @ColumnInfo(name = "diaInicio")val dia:Int,
                  @ColumnInfo(name = "diaFim")val diaFim:Int)