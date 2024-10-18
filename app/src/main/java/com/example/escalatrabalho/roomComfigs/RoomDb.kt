package com.example.escalatrabalho.roomComfigs

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities =[DatasFolgas::class,
                    Feriados::class,
                    Executad0::class,
                    ModeloDeEScala::class,
                    HorioDosAlarmes::class,
                    Ferias::class],
                    version = 1)
abstract class RoomDb: RoomDatabase() {
    abstract fun dao():Daos

}