package com.example.escalatrabalho.views


import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.escalatrabalho.repositorio.repositoriodeDatas.Datas
import com.example.escalatrabalho.classesResultados.Resultados
import com.example.escalatrabalho.viewModel.ViewModelTelas
import com.example.escalatrabalho.viewModel.modelosParaView.visulizacaoDatas
import kotlinx.coroutines.flow.FlowCollector

@Composable
fun calendario(m:Modifier, vm:ViewModelTelas){
    var estado=vm.fluxoViewCalendario.collectAsState()//estado do fluxo que mite uma clsse sealed do Tipo Resultados representando a montagem das datas
    val scop = rememberCoroutineScope()


    var colunasDesc = remember { mutableStateOf(listOf("dom","seg","ter","qua","quin","sex","sab")) } //usei esse list para criar o cabesalho dias da semana
    LazyVerticalGrid(columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(1.3.dp),
        verticalArrangement = Arrangement.spacedBy(1.3 .dp),
        modifier= m.fillMaxHeight()
                   .fillMaxWidth()

    ) {
        items(items = colunasDesc.value, span ={ GridItemSpan(1) } )
        {

                Text(it, textAlign = TextAlign.Center,modifier=Modifier.width(80.dp))

        }
        items(items = estado.value){
            itrmcalendario2(it)
        }

    }



}

@Composable
fun itemCalendario(data: Datas){
    Box(modifier = Modifier
        .width(50.dp)
        .height(80.dp)){
          Text(text = data.dia,
              color = Color.Black,
              modifier = Modifier
                  .align(Alignment.TopCenter)
                  .offset(y = 5.dp))
        Column(modifier= Modifier
            .align(Alignment.BottomCenter)
            .offset(y = -5.dp)) {
            Text(text = "Tab")
        }
    }

}
@Composable
fun itrmcalendario2(data:visulizacaoDatas){
    Box(modifier = Modifier
        .width(50.dp)
        .height(80.dp).border(1.3.dp,color = Color.Black)){
        Text(text = data.dia.toString(),
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 5.dp))
        Column(modifier= Modifier
            .align(Alignment.BottomCenter)
            .offset(y = -5.dp)) {
            Text(text = data.trabalhado)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun config(m:Modifier,
           disparaDialogoDatas: ()->Unit,
           disparaDialogoFerias:()->Unit,
           calbackSnackbar:suspend (String) -> Unit ,
           vm: ViewModelTelas){
    var scrollState = rememberScrollState(0)
Column(modifier = m
    .fillMaxSize()
    .verticalScroll(state = scrollState)) {
    val scopo = rememberCoroutineScope()//escopo corotina
    horarioDosAlarmes(vm)//botão horario dos alarmes ao clicar aparesera o alarma
    Spacer(Modifier.padding(3.dp))//espaçamento entre os componentes
    dataDasFolgas(vm,disparaDialogoDatas)//botão data das folgas ao clicar aparesera a data das folgas
    Spacer(Modifier.padding(3.dp))//espaçamento entre os componentes
    ferias(vm.estadosVm.transicaoFerias,scopo,disparaDialogoFerias)//botão ferias ao clicar aparesera as ferias
    Spacer(Modifier.padding(8.dp))//espaçamento entre os componentes
    modeloDeescala(vm)//botão modelo de escala ao clicar aparesera o modelo de escala
}
}







@Composable
@Preview
fun previaCalendario(){
    //Surface {   calendario(Modifier,ViewModelTelas ())}
}

@Composable
@Preview
fun previaConfig(){
    //Surface { config(Modifier,{},{}, vm = ViewModelTelas()) }
}

@Composable
@Preview
fun previaTimer(){
   // Surface { timePicker() }
}