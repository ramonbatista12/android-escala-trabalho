package com.example.escalatrabalho.roomComfigs

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities =[DatasFolgas::class,
                    Feriados::class,
                    Executad0::class,
                    ModeloDeEScala::class,
                    DiasOpcionais::class,
                    HorioDosAlarmes::class,
                    Ferias::class],
                    version = 1)
abstract class RoomDb: RoomDatabase() {
    abstract fun dao():Daos

}

class CalbacInicializacaoBd:RoomDatabase.Callback(){
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        db.execSQL("INSERT INTO Executad0 (ezecutado) VALUES ('f')")
        db.execSQL("INSERT INTO ModeloDeEScala (id,modelo,boolean) VALUES (1,'12/36',0)")
        db.execSQL("INSERT INTO ModeloDeEScala (id,modelo,boolean) VALUES (2,'6/1',0)")
        db.execSQL("INSERT INTO ModeloDeEScala (id,modelo,boolean) VALUES (3,'seg-sext',0)")
        db.execSQL("INSERT INTO HorioDosAlarmes (hora,minuto) VALUES (6,0)")

    }
}