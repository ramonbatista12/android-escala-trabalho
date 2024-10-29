package com.example.escalatrabalho.retrofit

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

class CalendarioApiService{

    companion object {
        fun getInstanc():Retrofit{
            val contentType = "application/json".toMediaType()
            return Retrofit.Builder()
                .baseUrl("https://brasilapi.com.br/api/feriados/v1/")
                .addConverterFactory(Json.asConverterFactory(contentType))
                .build()
        }
    }


}
interface CalendarioApi{
    @GET("{anos}")
    suspend fun getDatas(@Path("anos") anos:Int):List<Feriados>
}
@Serializable
data class Feriados(
    @SerializedName(value = "date") val date:String,
    @SerializedName(value = "name") val name:String,
    @SerializedName(value = "type") val type:String)