package com.example.escalatrabalho.views


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.escalatrabalho.repositoriodeDatas.Datas
import com.example.escalatrabalho.classesResultados.Resultados
import com.example.escalatrabalho.viewModel.ViewModelTelas

@Composable
fun calendario(m:Modifier,vm:ViewModelTelas){
    var estado=vm.fluxo.collectAsState(Resultados.caregando())//estado do fluxo que mite uma clsse sealed do Tipo Resultados representando a montagem das datas
    var colunasDesc = remember { mutableStateOf(listOf("dom","seg","ter","qua","quin","sex","sab")) } //usei esse list para criar o cabesalho dias da semana
    LazyVerticalGrid(columns = GridCells.Fixed(7),modifier=m.fillMaxHeight().width(380.dp)) {
        for (i in colunasDesc.value){
           item{Text(text ="  "+i)}
        }
        when(val rs =estado.value) {
        is Resultados.Ok->   items(items = rs.l) { itemCalendario(it) }
         is Resultados.caregando->{
             items(30){
                 CircularProgressIndicator(modifier = Modifier.width(50.dp).height(80.dp))
             }
         }
        is Resultados.erro->{}
        }

    }



}

@Composable
fun itemCalendario(data: Datas){
    Box(modifier = Modifier.width(50.dp).height(80.dp)){
          Text(text = data.dia, color = Color.Black,modifier = Modifier.align(Alignment.TopCenter).offset(y=5.dp))
        Column(modifier=Modifier.align(Alignment.BottomCenter).offset(y=-5.dp)) { Text(text = "Tab")  }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun config(m:Modifier,disparaDialogoDatas: ()->Unit,disparaDialogoFerias:()->Unit,vm: ViewModelTelas){
    var scrollState = rememberScrollState(0)
Column(modifier = m.fillMaxSize().verticalScroll(state=scrollState)) {
    Spacer(Modifier.padding(10.dp))


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