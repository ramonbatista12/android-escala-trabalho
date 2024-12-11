package com.example.escalatrabalho.views

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.escalatrabalho.R
import com.example.escalatrabalho.classesResultados.ResultadosDatasFolgas
import com.example.escalatrabalho.classesResultados.ResultadosSalvarHora
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModelo1236
import com.example.escalatrabalho.repositorio.OpicionaiSealedClassess.OpicionalModeloSegSex
import com.example.escalatrabalho.roomComfigs.DatasFolgas
import com.example.escalatrabalho.roomComfigs.DiasOpcionais
import com.example.escalatrabalho.roomComfigs.HorioDosAlarmes
import com.example.escalatrabalho.roomComfigs.ModeloDeEScala
import com.example.escalatrabalho.ui.theme.AzulClareoAcinzentado
import com.example.escalatrabalho.viewModel.ViewModelTelas
import com.example.escalatrabalho.viewModel.modelosParaView.FeriasView
import com.example.escalatrabalho.viewModel.modelosParaView.HorarioView
import com.example.escalatrabalho.viewModel.modelosParaView.mdcheck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("SuspiciousIndentation")
@Composable
//comfiguracao responsavel por mostra otimer piker que agenda o horio dos alarmes
fun HorarioDosAlarmes(vm: ViewModelTelas, calbackSnackbar: suspend (String) -> Unit = {}, windowSizeClass: WindowSizeClass){
    //modtivo do uso de if ele vai selecionar  a fracao de acordo com o tamanho disponivel da janela
    // eu ainda estou estudando maneiras de faxer melhor  mas por emquanto foi o melhor que comsequi fazer
    //  lembrando que a os ife elses representam a fracao com base no windowSizeClass que e cauculado por currentWindowAdaptiveInfo().windowSizeClass
    // a documetacao diss que ate esse momento o uso de windowsize class e o mais indicado quandose referre a classes que cauculam o tamanho de janelas
   val largura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
             else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 0.4f
             else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 0.3F
             else 1.0f
   val altura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
            else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 0.4f
            else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 0.4f
            else 1.0f
     Box(Modifier.fillMaxWidth(largura )) {
    val scope = rememberCoroutineScope()//corotina interna
    Text(//texto clicavel que aciona a animacao que mostra o relogio
        text = "Horario dos alarmes",
        modifier = Modifier.align(Alignment.TopStart)
                           .offset(x = 20.dp)
                           .clickable { scope.launch {vm.estadosVm.transicaoDatPiker.targetState = !vm.estadosVm.transicaoDatPiker.currentState  } }
    )
    IconButton (//icone que aciona a animacao que mostra o relogio
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

    ) {
       var  disparaTrocaDEHorario by  remember { mutableStateOf(false) }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val horario = vm.fluxoHorariosDosAlarmes.collectAsState(HorarioView(" -- "," -- "))
        Spacer(Modifier.padding(30.dp))
       Box(Modifier.padding(20.dp)
                   .fillMaxWidth(1f)
                   .height(200.dp).clickable { disparaTrocaDEHorario=true }
                   .border(width = 0.4.dp,shape = RoundedCornerShape(20.dp),color = Color.Black)){
        FlowRow (horizontalArrangement = Arrangement.Center,
                 modifier = Modifier.align(Alignment.Center) ) {
              Text(text=horario.value.hora ,
                   fontSize = 70.sp, modifier = Modifier.border(width = 0.4.dp, shape =RoundedCornerShape(20.dp),color = Color.Black ))
              Spacer(Modifier.padding(5.dp))
              Text(text=":",
                   fontSize = 70.sp)
              Spacer(Modifier.padding(5.dp))
              Text(text=horario.value.minuto ,
                  fontSize = 70.sp,modifier = Modifier.border(width = 0.4.dp, shape =RoundedCornerShape(20.dp),color = Color.Black ))
            }
           IconButton(onClick = {disparaTrocaDEHorario=true},
           modifier=Modifier.size(if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 40.dp else 70.dp )
                            .align(Alignment.TopEnd)) {
           Icon(Icons.Default.Edit, contentDescription = "editar horario",modifier=Modifier.size(45.dp))
         }}

       if(disparaTrocaDEHorario)
          ModalBottomSheet (onDismissRequest = { disparaTrocaDEHorario = false}){
              Surface { TimePicker(vm =vm ,calbackSnackbar = calbackSnackbar,windowSizeClass = windowSizeClass) }}
          }
    }

}
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
//comfiguracao responsavel por mostra otimer piker que agenda o horio dos alarmes
fun HorarioDosAlarmesAuturaCompacta(vm: ViewModelTelas, calbackSnackbar: suspend (String) -> Unit = {}, windowSizeClass: WindowSizeClass){
    //modtivo do uso de if ele vai selecionar  a fracao de acordo com o tamanho disponivel da janela
    // eu ainda estou estudando maneiras de fazer melhor  mas por emquanto foi o melhor que comsequi fazer
    //  lembrando que a os ife elses representam a fracao com base no windowSizeClass que e cauculado por currentWindowAdaptiveInfo().windowSizeClass
    // a documetacao diss que ate esse momento o uso de windowsize class e o mais indicado quandose referre a classes que cauculam o tamanho de janelas
    val largura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
    else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 0.4f
    else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 0.5F
    else 1.0f
    val altura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1f
    else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 1.0f
    else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 1F
    else 1.0f
    Box(Modifier.fillMaxWidth(largura ).fillMaxHeight(altura)) {
        val scope = rememberCoroutineScope()//corotina interna
        Text(//texto clicavel que aciona a animacao que mostra o relogio
            text = "Horario dos alarmes",
            modifier = Modifier.align(Alignment.TopStart)
                .offset(x = 20.dp)
                .clickable { scope.launch {vm.estadosVm.transicaoDatPiker.targetState = !vm.estadosVm.transicaoDatPiker.currentState  } }
        )
        IconButton (//icone que aciona a animacao que mostra o relogio
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
        ) {

            var  disparaTrocaDEHorario by  remember { mutableStateOf(false) }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val horario = vm.fluxoHorariosDosAlarmes.collectAsState(HorarioView(" -- "," -- "))
                Spacer(Modifier.padding(10.dp))
                Box(Modifier.padding(20.dp)
                    .fillMaxWidth(1f)
                    .height(200.dp).clickable { disparaTrocaDEHorario=true }
                    .border(width = 0.4.dp,shape = RoundedCornerShape(20.dp),color = Color.Black)){
                    FlowRow (horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.align(Alignment.Center) ) {
                        if(windowSizeClass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) Spacer(Modifier.padding(10.dp))
                        Text(text=horario.value.hora ,
                            fontSize = 70.sp, modifier = Modifier.border(width = 0.4.dp, shape =RoundedCornerShape(20.dp),color = Color.Black ))
                        Spacer(Modifier.padding(5.dp))
                      if(windowSizeClass.windowHeightSizeClass!= WindowHeightSizeClass.COMPACT) Text(text=":",fontSize = 70.sp)


                        Spacer(Modifier.padding(5.dp))
                        Text(text=horario.value.minuto ,
                            fontSize = 70.sp,modifier = Modifier.border(width = 0.4.dp, shape =RoundedCornerShape(20.dp),color = Color.Black ))
                    }
                    IconButton(onClick = {disparaTrocaDEHorario=true},
                        modifier=Modifier.size(if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 40.dp else 70.dp )
                            .align(Alignment.TopEnd)) {
                        Icon(Icons.Default.Edit, contentDescription = "editar horario",modifier=Modifier.size(45.dp))
                    }}

                if(disparaTrocaDEHorario)
                    ModalBottomSheet (onDismissRequest = { disparaTrocaDEHorario = false})
                    {
                        Surface { TimePicker(vm =vm ,calbackSnackbar = calbackSnackbar,windowSizeClass = windowSizeClass) }}
            } }



        }


}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
//responsavel por mostrar as datas de folgas
fun DataDasFolgas(vm:ViewModelTelas, diparaDialogoDatas:()->Unit, windowSizeClass: WindowSizeClass,calbackInteresticial:()->Unit){
    var fluxo = vm.fluxoDatasFolgas.collectAsState(ResultadosDatasFolgas.caregando())
    //modtivo do uso de if ele vai selecionar  a fracao de acordo com o tamanho disponivel da janela
    // eu ainda estou estudando maneiras de faxer melhor  mas por emquanto foi o melhor que comsequi fazer
    //  lembrando que a os ife elses representam a fracao com base no windowSizeClass que e cauculado por currentWindowAdaptiveInfo().windowSizeClass
    // a documetacao diss que ate esse momento o uso de windowsize class e o mais indicado quandose referre a classes que cauculam o tamanho de janelas
    val largura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
              else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 0.5f
              else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 0.3F
              else 1.0f
    val altura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
             else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 0.4f
             else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 0.5F
             else 1.0f
    var escopo= rememberCoroutineScope()
    val progresoo = remember { mutableStateOf(0f) }
    Box(modifier = Modifier.fillMaxWidth(largura)) {
    val scope = rememberCoroutineScope()//corotina interna
    Text(//texto clicavel que aciona a animacao que mostra as datas de folgas
        text = "DataDasFolgas",
        modifier = Modifier.align(Alignment.TopStart)
                           .offset(x = 20.dp)
                           .clickable {
                               calbackInteresticial()
                               scope.launch { vm.estadosVm.transicaoData.targetState = !vm.estadosVm.transicaoData.currentState
                               }
                           }
    )
    IconButton(// icone que aciona a animacao que mostra as datas de folgas
        onClick = {
            calbackInteresticial()
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
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        Column(
               modifier = Modifier.fillMaxWidth()

        ) {
            Spacer(Modifier.padding(30.dp))
            LazyVerticalGrid (columns = GridCells.FixedSize(140.dp),
                              modifier = Modifier.fillMaxWidth()
                                                  .height(200.dp)
                                                  .clip(RoundedCornerShape(20.dp))
                                                  .border(width = 0.4.dp,
                                                          color = Color.Black,
                                                          shape = RoundedCornerShape(20.dp))
                                                  .padding(10.dp)
                                                 ,
                              horizontalArrangement = Arrangement.spacedBy(3.dp),
                              verticalArrangement = Arrangement.spacedBy(3.dp)) {

                    when (val r=fluxo.value){
                        is ResultadosDatasFolgas.ok-> {
                            if(r.lista.isEmpty()) item{ //se a lista estiver vasia define o item com o aviso de vasio
                                Text(text = "lista vazia ",
                                     style = androidx.compose.material3.MaterialTheme.typography.titleLarge)}
                            else items(r.lista) { ItemDatas(it,vm=vm) }
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
@RequiresApi(Build.VERSION_CODES.O)
@Composable
//responsavel por mostrar as datas de folgas
fun DataDasFolgasAlturaCompacta(vm:ViewModelTelas, diparaDialogoDatas:()->Unit, windowSizeClass: WindowSizeClass,calbackInteresticial: () -> Unit){
    var fluxo = vm.fluxoDatasFolgas.collectAsState(ResultadosDatasFolgas.caregando())
    //modtivo do uso de if ele vai selecionar  a fracao de acordo com o tamanho disponivel da janela
    // eu ainda estou estudando maneiras de faxer melhor  mas por emquanto foi o melhor que comsequi fazer
    //  lembrando que a os ife elses representam a fracao com base no windowSizeClass que e cauculado por currentWindowAdaptiveInfo().windowSizeClass
    // a documetacao diss que ate esse momento o uso de windowsize class e o mais indicado quandose referre a classes que cauculam o tamanho de janelas
    val largura = 0.4f
    val altura =  1.0f
    var escopo= rememberCoroutineScope()
    val progresoo = remember { mutableStateOf(0f) }
    Box(modifier = Modifier.fillMaxWidth(largura)) {
        val scope = rememberCoroutineScope()//corotina interna
        Text(//texto clicavel que aciona a animacao que mostra as datas de folgas
            text = "DataDasFolgas",
            modifier = Modifier.align(Alignment.TopStart)
                .offset(x = 20.dp)
                .clickable {
                    calbackInteresticial()
                    scope.launch { vm.estadosVm.transicaoData.targetState = !vm.estadosVm.transicaoData.currentState
                    }
                }
        )
        IconButton(// icone que aciona a animacao que mostra as datas de folgas
            onClick = {
                calbackInteresticial()
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
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()

            ) {
                Spacer(Modifier.padding(30.dp))
                LazyVerticalGrid (columns = GridCells.FixedSize(140.dp),
                    modifier = Modifier.fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(width = 0.4.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(20.dp))
                        .padding(10.dp)
                    ,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)) {

                    when (val r=fluxo.value){
                        is ResultadosDatasFolgas.ok-> {
                            if(r.lista.isEmpty()) item{ //se a lista estiver vasia define o item com o aviso de vasio
                                Text(text = "lista vazia ",
                                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge)}
                            else items(r.lista) { ItemDatas(it,vm=vm) }
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
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
//responsavel por mostrar as datas de Ferias
//dispara dialogo Ferias responsavel por abrir o dialogo de datas de Ferias
// ela vem do nivel superior pois o dialogo e esibido no  scaffold pai
fun Ferias(vm: ViewModelTelas,stadoTransicao: MutableTransitionState<Boolean>, scope: CoroutineScope, diparaDialogoFerias:()->Unit, windowSizeClass: WindowSizeClass,calbackInteresticial: () -> Unit){
    //modtivo do uso de if ele vai selecionar  a fracao de acordo com o tamanho disponivel da janela
    // eu ainda estou estudando maneiras de faxer melhor  mas por emquanto foi o melhor que comsequi fazer
    //  lembrando que a os ife elses representam a fracao com base no windowSizeClass que e cauculado por currentWindowAdaptiveInfo().windowSizeClass
    // a documetacao diss que ate esse momento o uso de windowsize class e o mais indicado quandose referre a classes que cauculam o tamanho de janelas
    val largura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
    else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 0.5f
    else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 0.3F
    else 1.0f
    val altura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
    else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 0.3f
    else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 0.3F
    else 1.0f
    Box(modifier= Modifier.fillMaxWidth(largura)){
    val scope= rememberCoroutineScope()
    Text(//texto clicavel que aciona a animacao que mostra as datas de Ferias
        text = "Ferias", modifier = Modifier.align(Alignment.TopStart)
                                           .offset(20.dp)
                                           .clickable { calbackInteresticial()
                                               scope.launch { stadoTransicao.targetState = !stadoTransicao.currentState } })
    IconButton(//icone que aciona a animacao que mostra as datas de Ferias
        onClick = { scope.launch {calbackInteresticial()
            stadoTransicao.targetState = !stadoTransicao.currentState }   },
        modifier = Modifier.align(Alignment.TopEnd)
    ) {
        if (!stadoTransicao.currentState) Icon(
            Icons.Default.KeyboardArrowDown,
            contentDescription = "espamdir"
        )
        else Icon(Icons.Default.KeyboardArrowUp, contentDescription = "fechar")
    }
    androidx.compose.animation.AnimatedVisibility( //amimacao que mostra as datas de Ferias
        visibleState = stadoTransicao,
        modifier = Modifier.align(Alignment.Center)
    ){ Column (horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.padding(30.dp))
        val ferias = vm.fluxoFeriass.collectAsState(FeriasView(0, 0, 0, 0,0,0,0,false))



        Box(Modifier.fillMaxWidth()){



                    FlowRow(verticalArrangement = Arrangement.Center,
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.fillMaxWidth()
                                                .align(Alignment.TopCenter)
                                                .clip(RoundedCornerShape(20.dp))
                                                .border(width = 0.4.dp,
                                                        color = Color.Black,
                                                        shape = RoundedCornerShape(20.dp))
                                                .padding(horizontal = 30.dp,
                                                        vertical = 10.dp)
                                                .width(280.dp)) {
                      Column (modifier = Modifier.align(Alignment.CenterVertically)){
                          Column  {
                            Text(text =  "Ferias " )

                            Switch(checked = ferias.value!!.check,
                                onCheckedChange = {
                                    scope.launch {
                                      if(it) vm.estadosVm.disparaDialogoFerias.value=true
                                      else vm.apagarFerias()
                                    }
                                })
                        }}
                        Spacer(Modifier.padding(20.dp))
                        if(ferias.value!!.check) {
                            val visibilit=MutableTransitionState(ferias.value!!.check)
                        AnimatedVisibility(visibleState = visibilit) {
                            Column {
                            Column {
                            Text(text = "Inicio")
                            itemDatasFerias(ferias.value!!.diaInicio,ferias.value!!.mesInici,ferias.value!!.anoInici)
                                  }
                            Spacer(Modifier.padding(9.dp))
                        Column {
                            Text(text = "Fim")
                            itemDatasFerias(ferias.value!!.diaFim,ferias.value!!.mesFim,ferias.value!!.anoFim)
                           }
                                Spacer(Modifier.padding(10.dp))
                        }
                        }
                        }

                    }
                }


            }
        }
    } }
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
//responsavel por mostrar as datas de Ferias
//dispara dialogo Ferias responsavel por abrir o dialogo de datas de Ferias
// ela vem do nivel superior pois o dialogo e esibido no  scaffold pai
fun FeriasAlturaCompacta(vm :ViewModelTelas, stadoTransicao: MutableTransitionState<Boolean>, scope: CoroutineScope, diparaDialogoFerias:()->Unit, windowSizeClass: WindowSizeClass,calbackInteresticial: () -> Unit){
    //modtivo do uso de if ele vai selecionar  a fracao de acordo com o tamanho disponivel da janela
    // eu ainda estou estudando maneiras de faxer melhor  mas por emquanto foi o melhor que comsequi fazer
    //  lembrando que a os ife elses representam a fracao com base no windowSizeClass que e cauculado por currentWindowAdaptiveInfo().windowSizeClass
    // a documetacao diss que ate esse momento o uso de windowsize class e o mais indicado quandose referre a classes que cauculam o tamanho de janelas
    val largura =  0.5f

    Box(modifier= Modifier.fillMaxWidth(largura)){
        val scope= rememberCoroutineScope()
        Text(//texto clicavel que aciona a animacao que mostra as datas de Ferias
            text = "Ferias", modifier = Modifier.align(Alignment.TopStart)
                .offset(20.dp)
                .clickable { calbackInteresticial()
                    scope.launch { stadoTransicao.targetState = !stadoTransicao.currentState } })
        IconButton(//icone que aciona a animacao que mostra as datas de Ferias
            onClick = {calbackInteresticial()
                scope.launch { stadoTransicao.targetState = !stadoTransicao.currentState }   },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            if (!stadoTransicao.currentState) Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "espamdir"
            )
            else Icon(Icons.Default.KeyboardArrowUp, contentDescription = "fechar")
        }
        androidx.compose.animation.AnimatedVisibility( //amimacao que mostra as datas de Ferias
            visibleState = stadoTransicao,
            modifier = Modifier.align(Alignment.Center)
        ){ Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.padding(30.dp))
            val ferias = vm.fluxoFeriass.collectAsState(FeriasView(0, 0, 0, 0,0,0,0,false))
            var checkFerias by remember { mutableStateOf(ferias.value!!.check) }

            Box(Modifier.fillMaxWidth()){



                FlowRow(verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .clip(RoundedCornerShape(20.dp))
                        .border(width = 0.4.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 30.dp,
                            vertical = 10.dp)
                        .width(280.dp)) {
                    Column (modifier = Modifier.align(Alignment.CenterVertically)){
                        Column  {
                            Text(text = "Ferias " )

                            Switch(checked = checkFerias,
                                onCheckedChange = {
                                    calbackInteresticial()
                                    scope.launch {
                                        if(!checkFerias) diparaDialogoFerias()
                                       else vm.apagarFerias()
                                    }
                                })
                        }}
                    Spacer(Modifier.padding(20.dp))
                    if(ferias.value.check) {
                        val visibilit=MutableTransitionState(ferias.value.check)
                        AnimatedVisibility(visibleState = visibilit) {
                            Column {
                                Column {
                                    Text(text = "Inicio")
                                    itemDatasFerias(ferias.value!!.diaInicio,ferias.value!!.mesInici,ferias.value!!.anoInici)
                                }
                                Spacer(Modifier.padding(9.dp))
                                Column {
                                    Text(text = "Fim")
                                    itemDatasFerias(ferias.value!!.diaFim,ferias.value!!.mesFim,ferias.value!!.anoFim)
                                }
                                Spacer(Modifier.padding(10.dp))
                            }
                        }
                    }

                }
            }


        }
        }
    } }


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
//responsavel por esibir o melo de escala ex 12/36,6/1,seg-sext
fun ModeloDeEscala(vm: ViewModelTelas, windowSizeClass: WindowSizeClass,calbackInteresticial: () -> Unit){
    //modtivo do uso de if ele vai selecionar  a fracao de acordo com o tamanho disponivel da janela
    // eu ainda estou estudando maneiras de faxer melhor  mas por emquanto foi o melhor que comsequi fazer
    //  lembrando que a os ife elses representam a fracao com base no windowSizeClass que e cauculado por currentWindowAdaptiveInfo().windowSizeClass
    // a documetacao diss que ate esse momento o uso de windowsize class e o mais indicado quandose referre a classes que cauculam o tamanho de janelas
    val largura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
              else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 0.4f
              else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 0.3F
              else 1.0f
    val altura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
             else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 0.4f
             else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 0.5F
             else 1.0f
    Box(modifier = Modifier.fillMaxWidth(largura)){
        Text(//texto clicavel que aciona a animacao que mostra o modelo de escala
            text = "Modelo de escala",
            modifier = Modifier.align(Alignment.TopStart)
                               .offset(x=20.dp)
                               .clickable { vm.estadosVm.transicaoModeloTrabalho.targetState = !vm.estadosVm.transicaoModeloTrabalho.currentState })
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
            modifier = Modifier.align(Alignment.CenterStart).offset(x=3.dp)) {
            var modelo1236 =vm.estadosModeloTrabalho1236.collectAsState(mdcheck(1,false))
            var modelo61 =vm.estadosModeloDeTrabalho61.collectAsState(mdcheck(2,false))
            var modeloSegSex =vm.estadoModloTrabalhoSegsext.collectAsState(mdcheck(3,false))
            var diasOpcionais = vm.fluxoDiasOpcionais.collectAsState(DiasOpcionais(id=0,"",
                                                                     OpicionalModelo1236.Vasio.opcao))
           Column {
                Spacer(Modifier.padding(30.dp))
               FlowRow (horizontalArrangement = Arrangement.spacedBy(20.dp),
                        modifier=Modifier.clip(RoundedCornerShape(20.dp))
                                          .border(width = 0.4.dp,
                                                  color = Color.Black,
                                                  shape = RoundedCornerShape(20.dp))
                                          .padding(horizontal = 30.dp,
                                                   vertical = 10.dp)
                                          .fillMaxWidth()
                                         /* .width(280.dp)*/) {
                Column(horizontalAlignment = Alignment.CenterHorizontally)  {
                    Text("12 / 36")
                    Switch(checked = if(modelo1236.value.check&&
                                        modelo61.value.check==false&&
                                        modeloSegSex.value.check==false)true else false, onCheckedChange = {
                        calbackInteresticial()
                        Log.e("switch","${modelo1236.value.check} em onchange")
                        vm.inserirModeloDeTrabalho(ModeloDeEScala(modelo1236.value.id,"12/36",it))
                    },modifier = Modifier.animateContentSize())
                }
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Text(text = "6 / 1")
                    Switch(checked = if(modelo61.value.check&&
                                       modelo1236.value.check==false&&
                                       modeloSegSex.value.check==false) true else false ,
                        onCheckedChange = {
                            calbackInteresticial()
                        vm.inserirModeloDeTrabalho(ModeloDeEScala(modelo61.value.id,"6/1",it))
                                           },
                        modifier = Modifier.animateContentSize())
                }
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Text(text = "seg - sext")
                    Switch(checked = if(modeloSegSex.value.check&&
                                        modelo1236.value.check==false&&
                                        modelo61.value.check==false) true else false,
                          onCheckedChange = {
                              calbackInteresticial()
                        vm.inserirModeloDeTrabalho(ModeloDeEScala(modeloSegSex.value.id,"seg-sext",it))
                                            },
                        modifier = Modifier.animateContentSize())
                }
               if(modelo1236.value.check){

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "dia-impar")

                     Switch(checked =
                     if (diasOpcionais.value!!.opicional == OpicionalModelo1236.Impar.opcao)
                            true else false, onCheckedChange = {
                             calbackInteresticial()
                            if(it) vm.inserirOpcionalModelo("12/36",OpicionalModelo1236.Impar.opcao)
                            else vm.inserirOpcionalModelo("12/36",OpicionalModelo1236.Vasio.opcao)
                     })
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "dia-par")
                    Switch(checked = if(diasOpcionais.value!!.opicional==OpicionalModelo1236.Par.opcao)true else false
                        , onCheckedChange = {
                         calbackInteresticial()
                        if(it) vm.inserirOpcionalModelo("12/36",OpicionalModelo1236.Par.opcao)
                        else  vm.inserirOpcionalModelo("12/36",OpicionalModelo1236.Vasio.opcao)
                        })
                }
                }else if(modeloSegSex.value.check){

                   Column(horizontalAlignment = Alignment.CenterHorizontally) {
                       Text(text = if(windowSizeClass.windowHeightSizeClass!=WindowHeightSizeClass.MEDIUM&&windowSizeClass.windowWidthSizeClass!=WindowWidthSizeClass.EXPANDED)"Sabados" else "Sabds")
                       Switch(checked = if(diasOpcionais.value!!.opicional==OpicionalModeloSegSex.Sbados.opcao) true else false, onCheckedChange = {
                           if(it) vm.inserirOpcionalModelo("seg-sext",OpicionalModeloSegSex.Sbados.opcao)
                           else vm.inserirOpcionalModelo("seg-sext",OpicionalModeloSegSex.Vasios.opcao)
                       })

                   }
                   Column(horizontalAlignment = Alignment.CenterHorizontally) {
                       Text(text = if(windowSizeClass.windowHeightSizeClass!=WindowHeightSizeClass.MEDIUM&&windowSizeClass.windowWidthSizeClass!=WindowWidthSizeClass.EXPANDED) "Domingos" else "Domgs")
                       Switch(checked =if (diasOpcionais.value!!.opicional==OpicionalModeloSegSex.Domingos.opcao) true else false, onCheckedChange = {
                           if(it) vm.inserirOpcionalModelo("seg-sext",OpicionalModeloSegSex.Domingos.opcao)
                           else vm.inserirOpcionalModelo("seg-sext",OpicionalModeloSegSex.Vasios.opcao)
                       })
                   }
                   Column(horizontalAlignment = Alignment.CenterHorizontally) {
                       Text(text = if(windowSizeClass.windowHeightSizeClass!=WindowHeightSizeClass.MEDIUM&&windowSizeClass.windowWidthSizeClass!=WindowWidthSizeClass.EXPANDED)"Feriados" else "Ferds")
                       Switch(checked =if (diasOpcionais.value!!.opicional==OpicionalModeloSegSex.Feriados.opcao) true else false, onCheckedChange = {
                           calbackInteresticial()
                           if(it) vm.inserirOpcionalModelo("seg-sext",OpicionalModeloSegSex.Feriados.opcao)
                           else vm.inserirOpcionalModelo("seg-sext",OpicionalModeloSegSex.Vasios.opcao)
                       })
                   }
               }

               }
            }

        }   }
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
//responsavel por esibir o melo de escala ex 12/36,6/1,seg-sext
fun ModeloDeEscalaAlturaCompacta(vm: ViewModelTelas, windowSizeClass: WindowSizeClass,calbackInteresticial: () -> Unit){
    //modtivo do uso de if ele vai selecionar  a fracao de acordo com o tamanho disponivel da janela
    // eu ainda estou estudando maneiras de faxer melhor  mas por emquanto foi o melhor que comsequi fazer
    //  lembrando que a os ife elses representam a fracao com base no windowSizeClass que e cauculado por currentWindowAdaptiveInfo().windowSizeClass
    // a documetacao diss que ate esse momento o uso de windowsize class e o mais indicado quandose referre a classes que cauculam o tamanho de janelas
    val largura = 0.5f

    Box(modifier = Modifier.fillMaxWidth(largura)){
        Text(//texto clicavel que aciona a animacao que mostra o modelo de escala
            text = "Modelo de escala",
            modifier = Modifier.align(Alignment.TopStart)
                .offset(x=20.dp)
                .clickable { calbackInteresticial()
                    vm.estadosVm.transicaoModeloTrabalho.targetState = !vm.estadosVm.transicaoModeloTrabalho.currentState })
        IconButton(//icone que aciona a animacao que mostra o modelo de escala
            onClick = {calbackInteresticial()
                vm.estadosVm.transicaoModeloTrabalho.targetState = !vm.estadosVm.transicaoModeloTrabalho.currentState },
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
            modifier = Modifier.align(Alignment.CenterStart).offset(x=3.dp,y=25.dp)) {
            var modelo1236 =vm.estadosModeloTrabalho1236.collectAsState(mdcheck(1,false))
            var modelo61 =vm.estadosModeloDeTrabalho61.collectAsState(mdcheck(2,false))
            var modeloSegSex =vm.estadoModloTrabalhoSegsext.collectAsState(mdcheck(3,false))
            var diasOpcionais = vm.fluxoDiasOpcionais.collectAsState(DiasOpcionais(id=0,"",
                OpicionalModelo1236.Vasio.opcao))
            var scrollState=rememberScrollState()
            Column (modifier = Modifier.verticalScroll(scrollState)){
                Spacer(Modifier.padding(30.dp))
                FlowRow (horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier=Modifier.clip(RoundedCornerShape(20.dp))
                        .border(width = 0.4.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 30.dp,
                            vertical = 10.dp)
                        .fillMaxWidth()
                    /* .width(280.dp)*/) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally)  {
                        Text("12 / 36")
                        Switch(checked = if(modelo1236.value.check&&
                            modelo61.value.check==false&&
                            modeloSegSex.value.check==false)true else false, onCheckedChange = {
                            calbackInteresticial()
                            Log.e("switch","${modelo1236.value.check} em onchange")
                            vm.inserirModeloDeTrabalho(ModeloDeEScala(modelo1236.value.id,"12/36",it))
                        },modifier = Modifier.animateContentSize())
                    }
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = "6 / 1")
                        Switch(checked = if(modelo61.value.check&&
                            modelo1236.value.check==false&&
                            modeloSegSex.value.check==false) true else false ,
                            onCheckedChange = {
                                calbackInteresticial()
                                vm.inserirModeloDeTrabalho(ModeloDeEScala(modelo61.value.id,"6/1",it))
                            },
                            modifier = Modifier.animateContentSize())
                    }
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = "seg - sext")
                        Switch(checked = if(modeloSegSex.value.check&&
                            modelo1236.value.check==false&&
                            modelo61.value.check==false) true else false,
                            onCheckedChange = {
                                calbackInteresticial()
                                vm.inserirModeloDeTrabalho(ModeloDeEScala(modeloSegSex.value.id,"seg-sext",it))
                            },
                            modifier = Modifier.animateContentSize())
                    }
                    if(modelo1236.value.check){

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "dia-impar")

                            Switch(checked =
                            if (diasOpcionais.value!!.opicional == OpicionalModelo1236.Impar.opcao)
                                true else false, onCheckedChange = {
                                 calbackInteresticial()
                                if(it) vm.inserirOpcionalModelo("12/36",OpicionalModelo1236.Impar.opcao)
                                else vm.inserirOpcionalModelo("12/36",OpicionalModelo1236.Vasio.opcao)
                            })
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "dia-par")
                            Switch(checked = if(diasOpcionais.value!!.opicional==OpicionalModelo1236.Par.opcao)true else false
                                , onCheckedChange = {
                                    calbackInteresticial()
                                    if(it) vm.inserirOpcionalModelo("12/36",OpicionalModelo1236.Par.opcao)
                                    else  vm.inserirOpcionalModelo("12/36",OpicionalModelo1236.Vasio.opcao)
                                })
                        }
                    }else if(modeloSegSex.value.check){
                        if(windowSizeClass.windowHeightSizeClass!=WindowHeightSizeClass.COMPACT) Spacer(Modifier.padding(0.3.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = if(windowSizeClass.windowHeightSizeClass!=WindowHeightSizeClass.COMPACT)"  sabados" else "sab")
                            Switch(checked = if(diasOpcionais.value!!.opicional==OpicionalModeloSegSex.Sbados.opcao) true else false, onCheckedChange = {
                                if(it) vm.inserirOpcionalModelo("seg-sext",OpicionalModeloSegSex.Sbados.opcao)
                                else vm.inserirOpcionalModelo("seg-sext",OpicionalModeloSegSex.Vasios.opcao)
                            })

                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text =if(windowSizeClass.windowHeightSizeClass!=WindowHeightSizeClass.COMPACT) "Domingos" else "Dom")
                            Switch(checked =if (diasOpcionais.value!!.opicional==OpicionalModeloSegSex.Domingos.opcao) true else false, onCheckedChange = {
                                if(it) vm.inserirOpcionalModelo("seg-sext",OpicionalModeloSegSex.Domingos.opcao)
                                else vm.inserirOpcionalModelo("seg-sext",OpicionalModeloSegSex.Vasios.opcao)
                            })
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = if(windowSizeClass.windowHeightSizeClass!=WindowHeightSizeClass.COMPACT)"Feriados" else "Fer")
                            Switch(checked =if (diasOpcionais.value!!.opicional==OpicionalModeloSegSex.Feriados.opcao) true else false, onCheckedChange = {
                                if(it) vm.inserirOpcionalModelo("seg-sext",OpicionalModeloSegSex.Feriados.opcao)
                                else vm.inserirOpcionalModelo("seg-sext",OpicionalModeloSegSex.Vasios.opcao)
                            })
                        }
                    }

                }
            }

        }   }
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
//minha implementacao de timer piker
fun TimePicker(vm: ViewModelTelas, calbackSnackbar: suspend (String) -> Unit = {}, windowSizeClass: WindowSizeClass) {

    var state =  rememberTimePickerState(7, 0,true)
    val scope = rememberCoroutineScope()
    val largura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
              else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 1.0f
              else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 1.0F
                   else 1.0f
    val altura = 1.0f
    val layout = if( windowSizeClass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) TimePickerLayoutType.Horizontal
                 else TimePickerLayoutType.Vertical
    Column(modifier = Modifier.clip(RoundedCornerShape(20.dp))
                               ) {
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {

            TextButton (onClick = {
                vm.inserirHorariosDosAlarmes( HorioDosAlarmes(0,state.hour,state.minute),calbakSnackbar = calbackSnackbar)
            }, modifier = Modifier .animateContentSize().width(190.dp))
            {
                Icon(painterResource(R.drawable.baseline_add_alarm_24),null,modifier = Modifier)
                when ( vm.estadosVm.salvandoHorariosResultados.value ) {
                  ResultadosSalvarHora.clicavel -> {
                        Text(text = "Editar e Salvar Horário",maxLines = 1)
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
        TimePicker(state = state, modifier = Modifier.fillMaxWidth(largura).fillMaxHeight(altura), layoutType = layout)

    }

    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//minha implementacao de timer piker
fun TimePickerAlturaCompacta(vm: ViewModelTelas, calbackSnackbar: suspend (String) -> Unit = {}, windowSizeClass: WindowSizeClass) {

    val state = rememberTimePickerState(7, 0,is24Hour = true)
    val scope = rememberCoroutineScope()
    val largura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
              else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 1.0f
              else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 1.0F
                   else 1.0f
    val altura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 0.7f
             else if (windowSizeClass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) 0.6f
                  else 1.0f
    val layout = if( windowSizeClass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) TimePickerLayoutType.Horizontal
    else TimePickerLayoutType.Vertical
    Column(modifier = Modifier.clip(RoundedCornerShape(20.dp))
        .border(width = 0.4.dp,
            color = Color.Black,
            shape = RoundedCornerShape(20.dp))) {
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {

            TextButton (onClick = {
                vm.inserirHorariosDosAlarmes( HorioDosAlarmes(0,state.hour,state.minute),calbakSnackbar = calbackSnackbar)
            }, modifier = Modifier .animateContentSize().width(190.dp))
            {
                Icon(painterResource(R.drawable.baseline_add_alarm_24),null,modifier = Modifier)

                when ( vm.estadosVm.salvandoHorariosResultados.value ) {
                    ResultadosSalvarHora.clicavel -> {
                        Text(text = "Editar e Salvar Horário",maxLines = 1)
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
        TimePicker(state = state, modifier = Modifier.fillMaxWidth(largura), layoutType = layout)

    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
//minha implementacao de timer piker
fun TimePickerAlturaCompactLarguraEspandida(vm: ViewModelTelas, calbackSnackbar: suspend (String) -> Unit = {}, windowSizeClass: WindowSizeClass) {
    val state = rememberTimePickerState(0, 59)
    val scope = rememberCoroutineScope()
    val largura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1.0f
    else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 1.0f
    else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED) 0.5F
    else 1.0f
    val altura =  if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 0.7f
    else if (windowSizeClass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) 0.6f
    else 1.0f
    val layout = if( windowSizeClass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) TimePickerLayoutType.Horizontal
    else TimePickerLayoutType.Vertical
    Column(modifier = Modifier.clip(RoundedCornerShape(20.dp))
        .border(width = 0.4.dp,
            color = Color.Black,
            shape = RoundedCornerShape(20.dp))) {
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {

            TextButton (onClick = {
                vm.inserirHorariosDosAlarmes( HorioDosAlarmes(0,state.hour,state.minute),calbakSnackbar = calbackSnackbar)
            }, modifier = Modifier .animateContentSize().width(190.dp))
            {
                 Icon(painterResource(R.drawable.baseline_add_alarm_24),null,modifier = Modifier)
                when ( vm.estadosVm.salvandoHorariosResultados.value ) {
                    ResultadosSalvarHora.clicavel -> {
                        Text(text = "Editar e Salvar Horário",maxLines = 1)
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
        TimePicker(state = state, modifier = Modifier.fillMaxWidth(largura), layoutType = layout)

    }

}



@Composable
fun ItemDatas(item:DatasFolgas, vm:ViewModelTelas){
    Card(modifier = Modifier.width(140.dp).height(40.dp)){
        Box(modifier = Modifier.width(140.dp)){
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
@Composable
fun itemDatasFerias( dia:Int,mes:Int,ano:Int){


    Card(modifier = Modifier.width(140.dp).height(40.dp)){
        Box(modifier = Modifier.width(140.dp)){
            Text(text = "${if(dia<10) "0" + dia else dia}/${if(mes<10) "0" + mes else mes}/${ano}", modifier = Modifier.align(Alignment.CenterStart).offset(x=10.dp))
            IconButton(onClick = {/*vm.deletarDatasFolgas(item)*/}, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(Icons.Default.Edit, contentDescription = "apagar data") }


        }
    }
}