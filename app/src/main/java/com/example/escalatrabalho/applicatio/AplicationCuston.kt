package com.example.escalatrabalho.applicatio

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.escalatrabalho.roomComfigs.RoomDb

class AplicationCuston: Application()  {
    companion object db{
        lateinit var db: RoomDb
    }
    override fun onCreate() {
        super.onCreate()
        db.db = Room.databaseBuilder(this,RoomDb::class.java,"datas").build()
        Log.e("custonApplication","metodo oncrieite")
    }
}