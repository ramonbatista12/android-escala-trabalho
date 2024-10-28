package com.example.escalatrabalho.applicatio

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.escalatrabalho.roomComfigs.CalbacInicializacaoBd
import com.example.escalatrabalho.roomComfigs.RoomDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AplicationCuston: Application()  {
    companion object db{
        lateinit var db: RoomDb

    }
    override fun onCreate() {
        super.onCreate()
        db.db = Room.databaseBuilder(this,RoomDb::class.java,"datas")
            .build()
        val applicationScope = CoroutineScope(SupervisorJob())
        applicationScope.launch(Dispatchers.IO) {
           db.db.dao().getExecutado()

        }



    }
}