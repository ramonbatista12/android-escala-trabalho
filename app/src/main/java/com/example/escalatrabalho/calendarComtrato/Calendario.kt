package com.example.escalatrabalho.calendarComtrato

import android.content.Context
import android.database.Cursor
import android.provider.CalendarContract
import android.util.Log
import androidx.compose.foundation.layout.add

class Calendario {

suspend fun getEventos(c:Context){

  val projecao = arrayOf(
      CalendarContract.Events.TITLE,
      CalendarContract.Events.DTSTART,
      CalendarContract.Events.DTEND,
  )

    val uri = CalendarContract.Events.CONTENT_URI

    val selection = " UPPER( ${CalendarContract.Events.TITLE} )LIKE ?"
    val selectionArgs = arrayOf( "%feriado%")


    val cursor = c.contentResolver.query(
        CalendarContract.Events.CONTENT_URI,
        projecao,
        selection,
        selectionArgs,
        null
    )

    cursor?.use {
        while (it.moveToNext()) {
            val titulo = it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.TITLE))
            val dtStart = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Events.DTSTART))
            val dtEnd = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Events.DTEND))
            // ... extrair outras colunas
            Log.d("calendario"," titulo ${titulo}, dtStart ${dtStart}, dtEnd ${dtEnd}")

        }
    }

}



}

data class aux(val id: Long,val  name: String,val accountName: String)