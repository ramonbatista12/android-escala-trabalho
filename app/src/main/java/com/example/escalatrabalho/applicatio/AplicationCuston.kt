package com.example.escalatrabalho.applicatio

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.escalatrabalho.retrofit.CalendarioApi
import com.example.escalatrabalho.retrofit.CalendarioApiService
import com.example.escalatrabalho.roomComfigs.CalbacInicializacaoBd
import com.example.escalatrabalho.roomComfigs.RoomDb
import com.example.escalatrabalho.worlk.AgendarAlarmes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AplicationCuston: Application()  {

    companion object db {
         lateinit var context: Context
         val db: RoomDb by lazy {
              Room.databaseBuilder(context,RoomDb::class.java,"datas")
             .build()
         }
        val retrofit = CalendarioApiService.getInstanc()
        val endpoint:CalendarioApi  by lazy {
            retrofit.create(CalendarioApi::class.java)
        }

    }


    override fun onCreate() {
        super.onCreate()

        val applicationScope = CoroutineScope(SupervisorJob())
        db.context = applicationContext
        applicationScope.launch(Dispatchers.IO) {
           db.db.dao().getExecutado()

           applicationScope.launch {
               val work = PeriodicWorkRequest.Builder(AgendarAlarmes::class.java,
                                          600000,
                                                     java.util.concurrent.TimeUnit.MINUTES).build()
               WorkManager.getInstance(applicationContext).enqueue(work)
           }

        }



    }

}






