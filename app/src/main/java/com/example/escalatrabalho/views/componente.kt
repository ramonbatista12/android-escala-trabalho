package com.example.escalatrabalho.views

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
//comfiguracao responsavel por mostra otimer piker que agenda o horio dos alarmes
fun horarioDosAlarmes(stadoTransicao: MutableTransitionState<Boolean>){
    Box(Modifier.fillMaxWidth()) {
    val scope = rememberCoroutineScope()//corotina interna
    Text(//texto clicavel que aciona a animacao que mostra o relogio
        text = "Horario dos alarmes",
        modifier = Modifier.align(Alignment.TopStart)
                           .offset(x = 20.dp).clickable { scope.launch { stadoTransicao.targetState = !stadoTransicao.currentState } }
    )
    IconButton(//icone que aciona a animacao que mostra o relogio
        onClick = { scope.launch {  stadoTransicao.targetState = !stadoTransicao.currentState }},
        modifier = Modifier.align(Alignment.TopEnd)
    ) {
        if (!stadoTransicao.currentState) Icon(
            Icons.Default.KeyboardArrowDown,
            contentDescription = "espamdir"
        )
        else Icon(Icons.Default.KeyboardArrowUp, contentDescription = "fechar")
    }
    androidx.compose.animation.AnimatedVisibility(
        visibleState =stadoTransicao,
        modifier = Modifier.align(Alignment.Center)
    ) { Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.padding(30.dp))
        Surface { timePicker() }
    } }

}
}
@Composable
//responsavel por mostrar as datas de folgas
fun dataDasFolgas(stadoTransicao: MutableTransitionState<Boolean>, diparaDialogoDatas:()->Unit){
    Box(modifier = Modifier.fillMaxWidth()) {
    val scope = rememberCoroutineScope()//corotina interna
    Text(//texto clicavel que aciona a animacao que mostra as datas de folgas
        text = "DataDasFolgas",
        modifier = Modifier.align(Alignment.TopStart)
                           .offset(x = 20.dp)
                           .clickable { scope.launch { stadoTransicao.targetState = !stadoTransicao.currentState } }
    )
    IconButton(// icone que aciona a animacao que mostra as datas de folgas
        onClick = {
            scope.launch {  stadoTransicao.targetState = !stadoTransicao.currentState}

        },
        modifier = Modifier.align(Alignment.TopEnd)
    ) {
        if (!stadoTransicao.currentState) Icon(
            Icons.Default.KeyboardArrowDown,
            contentDescription = "espamdir"
        )
        else Icon(Icons.Default.KeyboardArrowUp, contentDescription = "fechar")
    }
    androidx.compose.animation.AnimatedVisibility(//amimacao que mostra as datas de folgas
        visibleState = stadoTransicao,
        modifier = Modifier.align(Alignment.Center).offset(x=20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.heightIn(300.dp).fillMaxWidth()) {
            Spacer(Modifier.padding(30.dp))
            LazyVerticalGrid (columns = GridCells.FixedSize(170.dp), modifier = Modifier.width(500.dp).height(300.dp), horizontalArrangement = Arrangement.spacedBy(3.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                repeat(10){
                    item{
                        itemDatas()
                    }

                }
                item {
                    //adiciona um btn adicionar ao fianl da lista vi algo parecido no sistem DO TRABALHO
                    IconButton(onClick = {scope.launch { diparaDialogoDatas()} })  { Icon(Icons.Default.AddCircle,"adicionar data") }
                }
            }


        }
    }
}
}
@Composable
//responsavel por mostrar as datas de ferias
//dispara dialogo ferias responsavel por abrir o dialogo de datas de ferias
// ela vem do nivel superior pois o dialogo e esibido no  scaffold pai
fun ferias(stadoTransicao: MutableTransitionState<Boolean>, scope: CoroutineScope, diparaDialogoFerias:()->Unit){
    Box(modifier= Modifier.fillMaxWidth()){
    val scope= rememberCoroutineScope()
    Text(//texto clicavel que aciona a animacao que mostra as datas de ferias
        text = "Ferias", modifier = Modifier.align(Alignment.TopStart)
                                           .offset(20.dp)
                                           .clickable { scope.launch { stadoTransicao.targetState = !stadoTransicao.currentState } })
    IconButton(//icone que aciona a animacao que mostra as datas de ferias
        onClick = { scope.launch { stadoTransicao.targetState = !stadoTransicao.currentState }   },
        modifier = Modifier.align(Alignment.TopEnd)
    ) {
        if (!stadoTransicao.currentState) Icon(
            Icons.Default.KeyboardArrowDown,
            contentDescription = "espamdir"
        )
        else Icon(Icons.Default.KeyboardArrowUp, contentDescription = "fechar")
    }
    androidx.compose.animation.AnimatedVisibility( //amimacao que mostra as datas de ferias
        visibleState = stadoTransicao,
        modifier = Modifier.align(Alignment.Center)
    ){ Column (horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.padding(30.dp))
        var switchs0 by remember { mutableStateOf(false) }

        Box(Modifier.fillMaxWidth()){
            Column(modifier = Modifier.align(Alignment.Center)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = if(!switchs0) "Adicionar ferias" else "Ferias  marcadass" )
                    Spacer(modifier= Modifier.padding(51.dp))
                    Switch(checked = switchs0,
                        onCheckedChange = {
                            scope.launch {
                                if(!switchs0) diparaDialogoFerias()
                                switchs0=!switchs0
                            }
                        })
                }
                if(switchs0) Row(verticalAlignment = Alignment.CenterVertically){
                    Text("priodo:")
                    Text("11/12/2024 a")
                    Text(text = "31/12/2024")
                }


            }
        }
    } }
}
}

@Composable
//responsavel por esibir o melo de escala ex 12/36,6/1,seg-sext
fun modeloDeescala(stadoTransicao: MutableTransitionState<Boolean>){
    Box(modifier = Modifier.fillMaxWidth()){
        Text(//texto clicavel que aciona a animacao que mostra o modelo de escala
            text = "Modelo de escala",
            modifier = Modifier.align(Alignment.TopStart)
                               .offset(x=20.dp).clickable { stadoTransicao.targetState = !stadoTransicao.currentState })
        IconButton(//icone que aciona a animacao que mostra o modelo de escala
            onClick = { stadoTransicao.targetState = !stadoTransicao.currentState },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            if (!stadoTransicao.currentState) Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "espamdir"
            )
            else Icon(Icons.Default.KeyboardArrowUp, contentDescription = "fechar")
        }


        androidx.compose.animation.AnimatedVisibility(//animacao que mostra o modelo de escala
            visibleState = stadoTransicao,
            modifier = Modifier.align( Alignment.Center)) {
            var s1 by remember { mutableStateOf(false) }
            var s2 by remember { mutableStateOf(false) }
            var s3 by remember { mutableStateOf(false) }
            Column {
                Spacer(Modifier.padding(30.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("12 / 36")
                    Spacer(Modifier.padding(40.dp))
                    Switch(checked = s1, onCheckedChange = {})
                }
                Row (verticalAlignment = Alignment.CenterVertically){
                    Text(text = "6 / 1")
                    Spacer(Modifier.padding(50.dp))
                    Switch(checked = s2, onCheckedChange = {})
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "seg - sext")
                    Spacer(Modifier.padding(30.dp))
                    Switch(checked = s3, onCheckedChange = {})
                }
            }

        }   }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun timePicker(){
    val state = rememberTimePickerState(0,59)
    TimePicker(state = state,modifier = Modifier.fillMaxWidth().height(400.dp))
}




@Composable
fun itemDatas(){
    Card(modifier = Modifier.width(170.dp).height(40.dp)){
        Box(modifier = Modifier.width(170.dp)){
            Text(text = "20/30/3445", modifier = Modifier.align(Alignment.CenterStart).offset(x=10.dp))
            IconButton(onClick = {}, modifier = Modifier.align(Alignment.TopEnd)) { Icon(Icons.Default.Delete, contentDescription = "apagar data") }


        }
    }
}