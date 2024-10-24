package com.example.escalatrabalho.views

import android.content.ClipData.Item
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.escalatrabalho.R
import com.example.escalatrabalho.classesResultados.ResultadosDatasFolgas
import com.example.escalatrabalho.roomComfigs.DatasFolgas
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import com.example.escalatrabalho.viewModel.ViewModelTelas
import com.example.escalatrabalho.viewModel.mdcheck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
//comfiguracao responsavel por mostra otimer piker que agenda o horio dos alarmes
fun horarioDosAlarmes(vm: ViewModelTelas,calbackSnackbar: suspend (String) -> Unit = {}){
    Box(Modifier.fillMaxWidth()) {
    val scope = rememberCoroutineScope()//corotina interna
    Text(//texto clicavel que aciona a animacao que mostra o relogio
        text = "Horario dos alarmes",
        modifier = Modifier.align(Alignment.TopStart)
                           .offset(x = 20.dp).clickable { scope.launch {vm.estadosVm.transicaoDatPiker.targetState = !vm.estadosVm.transicaoDatPiker.currentState  } }
    )
    TextButton (//icone que aciona a animacao que mostra o relogio
        onClick = { scope.launch {  vm.estadosVm.transicaoDatPiker.targetState = !vm.estadosVm.transicaoDatPiker.currentState}},
        modifier = Modifier.align(Alignment.TopEnd).animateContentSize()
    ) {
        if (!vm.estadosVm.transicaoDatPiker.currentState) Icon(
            Icons.Default.KeyboardArrowDown,
            contentDescription = "espamdir"
        )
        else Icon(Icons.Default.KeyboardArrowUp, contentDescription = "fechar")
    }
    androidx.compose.animation.AnimatedVisibility(
        visibleState =vm.estadosVm.transicaoDatPiker,
        modifier = Modifier.align(Alignment.Center)
    ) { Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.padding(30.dp))
        Surface { timePicker(vm =vm ,calbackSnackbar = calbackSnackbar) }
    } }

}
}
@Composable
//responsavel por mostrar as datas de folgas
fun dataDasFolgas(vm:ViewModelTelas, diparaDialogoDatas:()->Unit){
    var fluxo = vm.fluxoDatasFolgas.collectAsState(ResultadosDatasFolgas.caregando())
    var escopo= rememberCoroutineScope()
    val progresoo = remember { mutableStateOf(0f) }
    Box(modifier = Modifier.fillMaxWidth()) {
    val scope = rememberCoroutineScope()//corotina interna
    Text(//texto clicavel que aciona a animacao que mostra as datas de folgas
        text = "DataDasFolgas",
        modifier = Modifier.align(Alignment.TopStart)
                           .offset(x = 20.dp)
                           .clickable {
                               scope.launch { vm.estadosVm.transicaoData.targetState = !vm.estadosVm.transicaoData.currentState
                               }
                           }
    )
    IconButton(// icone que aciona a animacao que mostra as datas de folgas
        onClick = {
            scope.launch {  vm.estadosVm.transicaoData.targetState = !vm.estadosVm.transicaoData.currentState }

        },
        modifier = Modifier.align(Alignment.TopEnd)
    ) {
        if (!vm.estadosVm.transicaoData.currentState) Icon(
            Icons.Default.KeyboardArrowDown,
            contentDescription = "espamdir"
        )
        else Icon(Icons.Default.KeyboardArrowUp, contentDescription = "fechar")
    }
    androidx.compose.animation.AnimatedVisibility(//amimacao que mostra as datas de folgas
        visibleState = vm.estadosVm.transicaoData,
        modifier = Modifier.align(Alignment.Center).offset(x=20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Spacer(Modifier.padding(30.dp))
            LazyVerticalGrid (columns = GridCells.FixedSize(170.dp),
                              modifier = Modifier.width(500.dp)
                                                  .height(300.dp),
                              horizontalArrangement = Arrangement.spacedBy(3.dp),
                              verticalArrangement = Arrangement.spacedBy(3.dp)) {

                    when (val r=fluxo.value){
                        is ResultadosDatasFolgas.ok-> {
                            if(r.lista.isEmpty()) item{ //se a lista estiver vasia define o item com o aviso de vasio
                                Text(text = "lista vazia ",
                                     style = androidx.compose.material3.MaterialTheme.typography.titleLarge)}
                            else items(r.lista) { itemDatas(it,vm=vm) }
                        }
                        is ResultadosDatasFolgas.erro->{
                            item{
                                 Text(text = "Erro ao carregar as datas",
                                     color = Color.Red,
                                     style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                                     modifier = Modifier.align(Alignment.CenterHorizontally)

                                 )

                            }
                        }
                        is ResultadosDatasFolgas.caregando->{
                            item{
                            CircularProgressIndicator(
                                modifier = Modifier.fillMaxWidth()

                            )
                            }
                        }
                    }
                //adiciona um btn adicionar ao fianl da lista vi algo parecido no sistem DO TRABALHO
             item {     IconButton (onClick = {scope.launch { diparaDialogoDatas()} })  {
                    Icon(Icons.Default.AddCircle,"adicionar data",modifier = Modifier.size(50.dp))
             }
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
fun modeloDeescala(vm: ViewModelTelas){
    Box(modifier = Modifier.fillMaxWidth()){
        Text(//texto clicavel que aciona a animacao que mostra o modelo de escala
            text = "Modelo de escala",
            modifier = Modifier.align(Alignment.TopStart)
                               .offset(x=20.dp).clickable { vm.estadosVm.transicaoModeloTrabalho.targetState = !vm.estadosVm.transicaoModeloTrabalho.currentState })
        IconButton(//icone que aciona a animacao que mostra o modelo de escala
            onClick = { vm.estadosVm.transicaoModeloTrabalho.targetState = !vm.estadosVm.transicaoModeloTrabalho.currentState },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            if (!vm.estadosVm.transicaoModeloTrabalho.currentState) Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "espamdir"
            )
            else Icon(Icons.Default.KeyboardArrowUp, contentDescription = "fechar")
        }


        androidx.compose.animation.AnimatedVisibility(//animacao que mostra o modelo de escala
            visibleState = vm.estadosVm.transicaoModeloTrabalho,
            modifier = Modifier.align( Alignment.Center)) {
            var s1 =vm.estadosModeloTrabalho1236.collectAsState(mdcheck(1,false))
            var s2 =vm.estadosModeloDeTrabalho61.collectAsState(mdcheck(2,false))
            var s3 =vm.estadoModloTrabalhoSegsext.collectAsState(mdcheck(3,false))
            Column {
                Spacer(Modifier.padding(30.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("12 / 36")
                    Spacer(Modifier.padding(40.dp))
                    Switch(checked = s1.value.check, onCheckedChange = {
                        Log.e("switch","${s1.value.check} em onchange")
                        vm.inserirModeloDeTrabalho(ModeloDeEScala(s1.value.id,"12/36",it))
                    })
                }
                Row (verticalAlignment = Alignment.CenterVertically){
                    Text(text = "6 / 1")
                    Spacer(Modifier.padding(50.dp))
                    Switch(checked = s2.value.check , onCheckedChange = {
                        vm.inserirModeloDeTrabalho(ModeloDeEScala(s2.value.id,"6/1",it))
                    })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "seg - sext")
                    Spacer(Modifier.padding(30.dp))
                    Switch(checked = s3.value.check, onCheckedChange = {
                        vm.inserirModeloDeTrabalho(ModeloDeEScala(s3.value.id,"seg-sext",it))
                    })
                }
            }

        }   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun timePicker(vm: ViewModelTelas,calbackSnackbar: suspend (String) -> Unit = {}) {
    val state = rememberTimePickerState(0, 59)
    val scope = rememberCoroutineScope()
    Column {
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {

            TextButton (onClick = {
                vm.inserirHorariosDosAlarmes( HorioDosAlarmes(0,state.hour,state.minute),calbakSnackbar = calbackSnackbar)
            }, modifier = Modifier .animateContentSize().width(190.dp))
            {
                when ( vm.estadosVm.salvandoHorariosResultados.value ) {
                  ResultadosSalvarHora.clicavel -> {
                        Text(text = "Alterar e Salvar Horio",maxLines = 1)
                    }

                  ResultadosSalvarHora.comcluido -> {
                      Text(text = "Salvo")
                      Spacer(Modifier.padding(3.dp))
                      Icon(Icons.Default.Check,null,modifier = Modifier)
                    }



                   ResultadosSalvarHora.Salvo -> {
                       Text(text = "Salvo")
                       Spacer(Modifier.padding(3.dp))
                       Icon(Icons.Default.Check,null,modifier = Modifier)
                    }
                    ResultadosSalvarHora.salvando -> {
                        Text(text = "Salvando Horario",maxLines = 1)
                        Spacer(Modifier.padding(3.dp))
                        CircularProgressIndicator(modifier = Modifier.size(30.dp))
                    }

                }
               }
                Spacer(Modifier.padding(10.dp))
            }

            Spacer(Modifier.padding(10.dp))
            TimePicker(state = state, modifier = Modifier.fillMaxWidth().height(400.dp))
    }

    }






@Composable
fun itemDatas(item:DatasFolgas,vm:ViewModelTelas){
    Card(modifier = Modifier.width(170.dp).height(40.dp)){
        Box(modifier = Modifier.width(170.dp)){
            Text(text = "${
                if(item.data<10) "0" + item.data.toString()
               else item.data
            }/${
                if(item.mes<10) "0" + item.mes.toString()
               else
                item.mes
            }/${
                item.ano
            }", modifier = Modifier.align(Alignment.CenterStart).offset(x=10.dp))
            IconButton(onClick = {vm.deletarDatasFolgas(item)}, modifier = Modifier.align(Alignment.TopEnd)) { Icon(Icons.Default.Delete, contentDescription = "apagar data") }


        }
    }
}