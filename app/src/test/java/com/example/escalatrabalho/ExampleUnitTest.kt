package com.example.escalatrabalho

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModeloSegSex
import com.example.escalatrabalho.repositorio.RepositorioPrincipal
import com.example.escalatrabalho.retrofit.CalendarioApi
import com.example.escalatrabalho.retrofit.Feriados
import com.example.escalatrabalho.roomComfigs.Daos
import com.example.escalatrabalho.roomComfigs.DatasFolgas
import com.example.escalatrabalho.roomComfigs.DiasOpcionais
import com.example.escalatrabalho.roomComfigs.Executad0
import com.example.escalatrabalho.roomComfigs.Ferias
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import com.example.escalatrabalho.roomComfigs.RoomDb
import com.example.escalatrabalho.viewModel.MainViewModel
import com.example.escalatrabalho.worlk.AgendarAlarmes
import com.example.escalatrabalho.worlk.WorkAuxiliarDecisoes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

class TesteAgendarAlarme{
    @Test
    fun agendarAlarme(): Unit {
        val WorkAuxiliarDecisoes=WorkAuxiliarDecisoes()

     val a=   WorkAuxiliarDecisoes.checaHorarioParaAgendadmento(HorioDosAlarmes(0,0,0))
     val b =WorkAuxiliarDecisoes.checaHorarioParaAgendadmento(HorioDosAlarmes(1,20,0))
     val localT=LocalTime.now()
     System.out.println("${localT.hour}:${localT.minute},${a},${b}")

    }
}

class MockRom:RoomDb() {
    override fun dao(): Daos {
        return object : Daos {
            override fun getDatasFolgas(ano: Int, mes: Int): Flow<List<DatasFolgas>> {
                return flowOf(listOf<DatasFolgas>())
            }

            override suspend fun insertFolgas(vararg datasFolgas: DatasFolgas) {

            }

            override suspend fun delete(datasFolgas: DatasFolgas) {

            }

            override suspend fun delete(feriados: com.example.escalatrabalho.roomComfigs.Feriados) {

            }

            override suspend fun delete(executad0: Executad0) {

            }

            override suspend fun delete(modeloDeEScala: ModeloDeEScala) {

            }

            override suspend fun delete(diasOpcionais: DiasOpcionais) {

            }

            override suspend fun delete(horioDosAlarmes: HorioDosAlarmes) {

            }

            override suspend fun delete(ferias: Ferias) {

            }

            override suspend fun update(datasFolgas: DatasFolgas) {

            }

            override suspend fun update(feriados: com.example.escalatrabalho.roomComfigs.Feriados) {

            }

            override suspend fun update(executad0: Executad0) {

            }

            override suspend fun update(modeloDeEScala: ModeloDeEScala) {

            }

            override suspend fun update(diasOpcionais: DiasOpcionais) {

            }

            override suspend fun update(ferias: Ferias) {

            }

            override fun getFeriados(mes: Int): Flow<List<com.example.escalatrabalho.roomComfigs.Feriados>> {
                return flowOf(emptyList())
            }

            override fun getFeriados(
                mes: Int,
                dia: Int
            ): Flow<com.example.escalatrabalho.roomComfigs.Feriados?> {
                return flowOf(null)
            }

            override fun comtaferiados(): Int {
                return 1
            }

            override suspend fun insertFeriados(vararg feriados: com.example.escalatrabalho.roomComfigs.Feriados) {

            }

            override fun getExecutado(): Flow<List<Executad0>> {
                return flowOf(listOf())
            }

            override suspend fun countExecutado(): Int {
                return 0
            }

            override suspend fun insertExecutado(vararg executad0: Executad0) {

            }

            override fun getModeloDeEScala(): Flow<List<ModeloDeEScala>> {
                return flowOf(listOf<ModeloDeEScala>())
            }

            override suspend fun count(): Int {
                return 0
            }

            override fun selectModeloDeEscalaAtivo(): Flow<ModeloDeEScala?> {
                return flowOf(null)
            }

            override fun getModeloDeEscalaObjeto(): ModeloDeEScala? {
                return null
            }

            override fun selectFeriascheck(colune: String): Flow<ModeloDeEScala?> {
                return flowOf(null)
            }

            override suspend fun insertModeloDeEScala(vararg modeloDeEScala: ModeloDeEScala) {

            }

            override suspend fun insert(modeloDeEScala: List<ModeloDeEScala>) {

            }

            override suspend fun updateLista(modeloDeEScala: List<ModeloDeEScala>) {

            }

            override fun getDiasOpcionais(modelo: String): Flow<List<DiasOpcionais>> {
                return flowOf(
                    listOf(
                        DiasOpcionais(
                            1,
                            "12/36",
                            OpicionalModeloSegSex.Domingos.opcao
                        )
                    )
                )
            }

            override fun getDiaOpcionaisMes(modelo: String): DiasOpcionais? {
                return null
            }

            override suspend fun countDiasOpcionais(): Int {
                return 0
            }

            override suspend fun insertDiasOpcionais(vararg diasOpcionais: DiasOpcionais) {}


            override suspend fun inserirListaDeOpcionais(diasOpcionais: List<DiasOpcionais>) {

            }

            override fun getHorariosDosAlarmes(): Flow<HorioDosAlarmes?> {
                return flowOf(null)
            }

            override suspend fun getHorariosDosAlarmesObjeto(): HorioDosAlarmes? {
                return null
            }

            override fun getHorariosPrimeiroDosAlarmes(): HorioDosAlarmes {
                return HorioDosAlarmes(0, 0, 0)
            }

            override fun countHorarios(): Int {
                return 0
            }

            override suspend fun insertHorariosDosAlarmes(vararg horioDosAlarmes: HorioDosAlarmes) {

            }

            override suspend fun updateAlarme(horioDosAlarmes: HorioDosAlarmes) {}


            override fun getFerias(): Flow<Ferias?> {
                return flowOf(null)
            }

            override fun getFeriasObjeto(): Ferias? {
                return null
            }

            override fun countFerias(): Int {
                return 0
            }

            override suspend fun insertFerias(vararg ferias: Ferias) {

            }

            override fun deleteFerias() {

            }

        }
    }

    override fun clearAllTables() {

    }

    override fun createInvalidationTracker(): InvalidationTracker {
        return object : InvalidationTracker(this@MockRom) {

        }
    }

    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        TODO("Not yet implemented")
    }

}



class MockCalendarioApi():CalendarioApi{
    override suspend fun getDatas(anos: Int): List<Feriados> {
        return  listOf<Feriados>(Feriados("12/12","Natal","Feriado"))
    }


}
class RepositorioMock: RepositorioPrincipal(MockRom(), MockCalendarioApi()) {

}

class TesxtemudancaestadoBaeria(){
    @Test
    fun mudarestado(){
        val vm = MainViewModel(RepositorioPrincipal(MockRom(), MockCalendarioApi()))
         vm.mudarEstadoBateria(true)
        val a = vm._bateriaOptimizacao
        val b = vm.dialogBateria
        assertEquals(a,true)
        assertEquals(b,false)

    }

}