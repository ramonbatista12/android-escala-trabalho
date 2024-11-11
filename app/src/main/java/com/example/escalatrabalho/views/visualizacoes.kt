package com.example.escalatrabalho.views


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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.escalatrabalho.repositorio.repositoriodeDatas.Datas
import com.example.escalatrabalho.viewModel.ViewModelTelas
import com.example.escalatrabalho.viewModel.modelosParaView.visulizacaoDatas
import kotlinx.coroutines.flow.map

@Composable
fun calendario(m:Modifier, vm:ViewModelTelas,windowSizeClass: WindowSizeClass){
    var estado=vm.fluxoViewCalendario.collectAsState()//estado do fluxo que mite uma clsse sealed do Tipo Resultados representando a montagem das datas
    var feriados =vm.fluxoFeriados.map { it.map { it.dia }.toList() }.collectAsState(emptyList())//estado do fluxo que mite uma clsse sealed do Tipo Resultados representando a montagem das datas
    val scop = rememberCoroutineScope()
    //modtivo do uso de if ele vai selecionar  a fracao de acordo com o tamanho disponivel da janela
    // eu ainda estou estudando maneiras de faxer melhor  mas por emquanto foi o melhor que comsequi fazer
    //  lembrando que a os ife elses representam a fracao com base no windowSizeClass que e cauculado por currentWindowAdaptiveInfo().windowSizeClass
    // a documetacao diss que ate esse momento o uso de windowsize class e o mais indicado quandose referre a classes que cauculam o tamanho de janelas
    val largura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
              else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 1.0f
              else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 0.3F
              else 1.0f
    val altura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
             else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 0.5f
             else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 0.8F
             else 1.0f

    var colunasDesc = remember {
        if(windowSizeClass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT)
            mutableStateOf(listOf("dm","sg","tr","qa","qi","sx","sb"))
           else mutableStateOf(listOf("dom","seg","ter","qua","quin","sex","sab"))
    } //usei esse list para criar o cabesalho dias da semana
    LazyVerticalGrid(columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(1.3.dp),
        verticalArrangement = Arrangement.spacedBy(1.3 .dp),
        modifier= m.fillMaxHeight(altura)
                   .fillMaxWidth(largura)

    ) {
        items(items = colunasDesc.value, span ={ GridItemSpan(1) } )
        {

                Text(it, textAlign = TextAlign.Center,modifier=Modifier.width(80.dp))

        }
        items(items = estado.value){

            if(it.mes==vm.numeroMes)
                if(feriados.value.contains(it.dia)) itrmcalendario2(it.copy(trabalhado = "fer\n ${it.trabalhado}"))
                else itrmcalendario2(it)
            else Spacer(Modifier.padding(20.dp))
        }

    }



}
@Composable
fun calendarioSmallSmall(m:Modifier, vm:ViewModelTelas,windowSizeClass: WindowSizeClass){
    var estado=vm.fluxoViewCalendario.collectAsState()//estado do fluxo que mite uma clsse sealed do Tipo Resultados representando a montagem das datas
    var feriados =vm.fluxoFeriados.map { it.map { it.dia }.toList() }.collectAsState(emptyList())//estado do fluxo que mite uma clsse sealed do Tipo Resultados representando a montagem das datas
    val scop = rememberCoroutineScope()
    //modtivo do uso de if ele vai selecionar  a fracao de acordo com o tamanho disponivel da janela
    // eu ainda estou estudando maneiras de faxer melhor  mas por emquanto foi o melhor que comsequi fazer
    //  lembrando que a os ife elses representam a fracao com base no windowSizeClass que e cauculado por currentWindowAdaptiveInfo().windowSizeClass
    // a documetacao diss que ate esse momento o uso de windowsize class e o mais indicado quandose referre a classes que cauculam o tamanho de janelas
    val largura =   1.0f
    val altura = 1.0f

    var colunasDesc = remember {
        if(windowSizeClass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT)
            mutableStateOf(listOf("dm","sg","tr","qa","qi","sx","sb"))
        else mutableStateOf(listOf("dom","seg","ter","qua","quin","sex","sab"))
    } //usei esse list para criar o cabesalho dias da semana
    LazyVerticalGrid(columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(1.3.dp),
        verticalArrangement = Arrangement.spacedBy(1.3 .dp),
        modifier= m.fillMaxHeight(altura)
            .fillMaxWidth(largura)

    ) {
        items(items = colunasDesc.value, span ={ GridItemSpan(1) } )
        {

            Text(it, textAlign = TextAlign.Center,modifier=Modifier.width(80.dp))

        }
        items(items = estado.value){
          if(it.mes==vm.numeroMes)
              if(feriados.value.contains(it.dia)) itrmcalendario2(it.copy(trabalhado = "fer\n ${it.trabalhado}"))
              else itrmcalendario2(it)
          else Spacer(Modifier.padding(20.dp))
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
        .height(80.dp)){
        Text(text = data.dia.toString(),
            color = if(data.trabalhado.contains("fer")) Color.Red else Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 5.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally,modifier= Modifier
            .align(Alignment.BottomCenter)
            .offset(y = -5.dp)) {
            if(data.trabalhado.contains("fer")){
                val split = data.trabalhado.split("\n")
                Text(text = split[0],textAlign = TextAlign.Center)
                Text(text = split[1],textAlign = TextAlign.Center)
            }
            else Text(text = data.trabalhado)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun config(m:Modifier,
           disparaDialogoDatas: ()->Unit,
           disparaDialogoFerias:()->Unit,
           calbackSnackbar:suspend (String) -> Unit ,
           vm: ViewModelTelas,windowSizeClass: WindowSizeClass){
    var scrollState = rememberScrollState(0)

Column(modifier = m
    .fillMaxSize()
    .verticalScroll(state = scrollState)) {
    val scopo = rememberCoroutineScope()//escopo corotina
    HorarioDosAlarmes(vm,calbackSnackbar, windowSizeClass = windowSizeClass)//botão horario dos alarmes ao clicar aparesera o alarma
    Spacer(Modifier.padding(3.dp))//espaçamento entre os componentes
    DataDasFolgas(vm,disparaDialogoDatas,windowSizeClass)//botão data das folgas ao clicar aparesera a data das folgas
    Spacer(Modifier.padding(3.dp))//espaçamento entre os componentes
    Ferias(vm=vm,vm.estadosVm.transicaoFerias,scopo,disparaDialogoFerias,windowSizeClass)//botão Ferias ao clicar aparesera as Ferias
    Spacer(Modifier.padding(8.dp))//espaçamento entre os componentes
    ModeloDeEscala(vm,windowSizeClass)//botão modelo de escala ao clicar aparesera o modelo de escala
    Spacer(Modifier.padding(40.dp))
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
   // Surface { TimePicker() }
}