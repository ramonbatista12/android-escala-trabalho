package com.example.escalatrabalho.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.escalatrabalho.R
import com.example.escalatrabalho.classesResultados.ResultadosSalvarDatasFolgas
import com.example.escalatrabalho.roomComfigs.DatasFolgas
import com.example.escalatrabalho.viewModel.ViewModelTelas
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedBoxWithConstraintsScope")
@Composable
fun telainicial(vm:ViewModelTelas,windowSizeClass: WindowSizeClass){
      val scop = rememberCoroutineScope()
      val hostSnabar =remember{ SnackbarHostState() }
     //seguindo as dicas do google que estudei adicionei os seguites modificadores de nivel superio eles tem tabem referentes de baixo nivel
    // baixo nivel no jetpack compose nao dis respeito a manipulacao direta a memoria ou componentes fisicos de baixo nivel de abstracao
    // mas sim a componentes basicos do jet peck compose como Layout Graficslayer pois o jet peck compose tabem tem um nivel de abistracao
    // componentes basicos explo basictext layout grafcSlayer componentes de medio nivle como box etc
    // e de alto nivel que nos nao presisamos criar tudo do zerro ou juntando partes basicas como o Text
     Scaffold(modifier=Modifier.fillMaxSize()
                               .safeContentPadding()
                               .safeDrawingPadding()
                               .safeGesturesPadding(),
              bottomBar = {
                if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT)  barraSuperior(vm =vm)
                          },
              snackbarHost = { SnackbarHost(hostState = hostSnabar) },) {

     Box (modifier=Modifier.fillMaxSize()){
         //modtivo do uso de if ele vai selecionar o layout de acordo com o tamanho disponivel da janela
         // eu ainda estou estudando maneiras de faxer melhor  mas por emquanto foi o melhor que comsequi fazer
         //  lembrando que a os ife elses representam a fracao com base no windowSizeClass que e cauculado por currentWindowAdaptiveInfo().windowSizeClass
         // a documetacao diss que ate esse momento o uso de windowsize class e o mais indicado quandose referre a classes que cauculam o tamanho de janelas
         // apesar de ter melhorado a repsonsividade ainda tem casos em que o layout nao se ajusta bem
         if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT)
                       // maioria dos smartphones
                                             when(vm.estadosVm.telas.value) {
                                             TelaNavegacaoSimples.calendario->
                                                 Column(horizontalAlignment = Alignment.CenterHorizontally,
                                                        modifier = Modifier.fillMaxSize()
                                                         .align(Alignment.TopCenter)
                                                         .matchParentSize()) {
                                                 Spacer(Modifier.padding(25.dp))
                                                 Text("   ${vm.nomeMes}")
                                                 Spacer(Modifier.padding(3.dp))
                                                 calendario(m=Modifier,vm=vm,windowSizeClass)
                                             }
                                             TelaNavegacaoSimples.comfig -> config(m=Modifier.align(Alignment.TopCenter)
                                                                                              .matchParentSize()
                                                                                              .padding(horizontal = 2.dp)/*.offset(y=60.dp)*/,
                                                 disparaDialogoDatas = {scop.launch {  vm.estadosVm.disparaDatass.value=!vm.estadosVm.disparaDatass.value}},
                                                 disparaDialogoFerias={scop.launch {  vm.estadosVm.disparaDialogoFerias.value=!vm.estadosVm.disparaDatass.value}},
                                                 calbackSnackbar = {it->
                                                    scop.launch { hostSnabar.showSnackbar(message = it,duration = SnackbarDuration.Short)}
                                                                   }
                                                 ,vm=vm,windowSizeClass)

                                             }
          else if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED)
                                  //maioria dos pcs e tablets no modo paisagem
                                           FlowRow {
                                              calendario(m=Modifier,vm=vm,windowSizeClass)
                                                        Spacer(modifier=Modifier.padding(8.dp))
                                                        horarioDosAlarmes(vm,calbackSnackbar = {it->},windowSizeClass)
                                                        Spacer(modifier=Modifier.padding(8.dp))
                                                         Column {
                                                              ferias(stadoTransicao = vm.estadosVm.transicaoFerias,
                                                                    scope = scop,
                                                                    diparaDialogoFerias = {
                                                                        vm.estadosVm.disparaDialogoFerias.value=!vm.estadosVm.disparaDialogoFerias.value
                                                                                           },
                                                                    windowSizeClass )
                                                              modeloDeescala(vm = vm,
                                                                             windowSizeClass = windowSizeClass)
                                                              dataDasFolgas(vm = vm,
                                                                           diparaDialogoDatas = {
                                                                               scop.launch {  vm.estadosVm.disparaDatass.value=!vm.estadosVm.disparaDatass.value}
                                                                                                } ,
                                                                           windowSizeClass)
                                                         }

                                             }
         else if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM)
                                //maioria dos tablets no modo retrato
                                             Column {
                                                 calendario(m=Modifier,vm=vm,windowSizeClass)
                                                 FlowRow(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                                     horarioDosAlarmes(vm,calbackSnackbar = {it->},windowSizeClass)
                                                     Spacer(modifier=Modifier.padding(8.dp))
                                                     Column  {
                                                         ferias(stadoTransicao = vm.estadosVm.transicaoFerias,
                                                             scope = scop,
                                                             diparaDialogoFerias = {
                                                                 vm.estadosVm.disparaDialogoFerias.value=!vm.estadosVm.disparaDialogoFerias.value
                                                             },
                                                             windowSizeClass )
                                                         Spacer(modifier=Modifier.padding(3.dp))
                                                         modeloDeescala(vm = vm,
                                                             windowSizeClass = windowSizeClass)
                                                         Spacer(modifier=Modifier.padding(3.dp))
                                                         dataDasFolgas(vm = vm,
                                                             diparaDialogoDatas = {
                                                                 scop.launch {  vm.estadosVm.disparaDatass.value=!vm.estadosVm.disparaDatass.value}
                                                             } ,
                                                             windowSizeClass)
                                                             }
                                                         }
                                             }
         }
 dialogoDatasFolgas(disparar = vm.estadosVm.disparaDatass.value, acaoFechar = {vm.estadosVm.disparaDatass.value=!vm.estadosVm.disparaDatass.value},vm = vm)
 dialogoDatasFerrias(disparar =vm.estadosVm.disparaDialogoFerias.value, acaoFechar = {vm.estadosVm.disparaDialogoFerias.value=!vm.estadosVm.disparaDialogoFerias.value})
}

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//dialogo de datas de folgas responsavel por permirtir a selecao das folgas
fun  dialogoDatasFolgas(disparar:Boolean,acaoFechar:()->Unit,vm:ViewModelTelas){
    if(disparar) Surface {
    ModalBottomSheet(onDismissRequest = acaoFechar) {
        var estate = rememberDatePickerState()

        Surface {   Box{

             IconButton(onClick ={
                 //eu uso a classe calendar para pegar a data selecionada e inserir no banco de dados
                 // pois esiste a nessesitade de receber um nuimerro e comverter ele em uma date

                 val cal = Calendar.getInstance()
                 cal.timeInMillis=estate.selectedDateMillis!!
                 vm.inserirDatasFolgas(
                     DatasFolgas(0,
                         cal.get(Calendar.DAY_OF_MONTH)?:11,
                         cal.get(Calendar.MONTH)?:1,
                         cal.get(Calendar.YEAR)?:2024
                     ))
             }, modifier = Modifier.align(Alignment.TopEnd) ) {
                 Icon(painterResource(R.drawable.baseline_save_24),"salvar")//aparesera quando for clicavel novamente
            }


            DatePicker(state = estate, showModeToggle = true)





        }

        }}


}
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
//dialogo de datas de ferias responsavel por permirtir a selecao das ferias
fun  dialogoDatasFerrias(disparar:Boolean,acaoFechar:()->Unit){
    if(disparar) Surface {
        ModalBottomSheet(onDismissRequest = acaoFechar) {  var estate = rememberDateRangePickerState()
            Surface {   Box{
                DateRangePicker(state = estate, showModeToggle = true)
            }

            }
        }

    }
}
@Composable
//barra de navegacao responsavel por navegar entre as telas
fun barraSuperior(vm:ViewModelTelas){
   var escopo = rememberCoroutineScope()//corotina interna
    NavigationBar(modifier = Modifier.fillMaxWidth()
                                     .height(50.dp)
                                     .background(color = Color.Transparent)
                                     .clip(RoundedCornerShape(30.dp))
                                     .border(0.1.dp,color = Color.Transparent, shape = RoundedCornerShape(30.dp))
        ) {
         // view model.estadosVm.telas.value=TelaNavegacaoSimples.calendario recebe um objeto do tipo do enun ou calendario ou configuracao
        // que representa as posiveis navegacoe simples tive essa ideia pois a vi em um code lab
         NavigationBarItem(//responsavel por navegar  para tela de calendario
             onClick = {escopo.launch { vm.estadosVm.telas.value=TelaNavegacaoSimples.calendario }},
             selected = false,
             modifier = Modifier.fillMaxWidth(),
             label = {  },
             icon = { Icon(Icons.Default.DateRange,contentDescription = "calendario", modifier = Modifier.height(30.dp)) },
         )

        NavigationBarItem(//responsavel por navegar para tela de configuracao
            onClick = {escopo.launch { vm.estadosVm.telas.value=TelaNavegacaoSimples.comfig }},
            selected = false,
            modifier = Modifier.fillMaxWidth(),
            label = {  },
            icon = {Icon(Icons.Default.Settings,contentDescription = "configuracao",modifier=Modifier.height(30.dp))}
        )

    }
}

@Composable
@Preview
fun telaInicial(){
    Surface {
      //  telainicial(ViewModelTelasContext as())
    }
}