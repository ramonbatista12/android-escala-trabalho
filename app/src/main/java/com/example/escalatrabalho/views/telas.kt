package com.example.escalatrabalho.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.escalatrabalho.R
import com.example.escalatrabalho.enums.TelaNavegacaoSimples
import com.example.escalatrabalho.enums.TelaNavegacaoSinplesAlturaCompacta
import com.example.escalatrabalho.roomComfigs.DatasFolgas
import com.example.escalatrabalho.viewModel.ViewModelTelas
import com.example.escalatrabalho.viewModel.modelosParaView.FeriasView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedBoxWithConstraintsScope")
@Composable
fun telainicial(vm:ViewModelTelas,windowSizeClass: WindowSizeClass){
      val scop = rememberCoroutineScope()

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
                                    if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT)
                                                barraSuperior(vm =vm)
                            },
              snackbarHost = { SnackbarHost(hostState = vm.hostState) },)


         {          //modtivo do uso de if ele vai selecionar o layout de acordo com o tamanho disponivel da janela
                                   // eu ainda estou estudando maneiras de faxer melhor  mas por emquanto foi o melhor que comsequi fazer
                                   //  lembrando que a os ife elses representam a fracao com base no windowSizeClass que e cauculado por currentWindowAdaptiveInfo().windowSizeClass
                                   // a documetacao diss que ate esse momento o uso de windowsize class e o mais indicado quandose referre a classes que cauculam o tamanho de janelas
                                   // apesar de ter melhorado a repsonsividade ainda tem casos em que o layout nao se ajusta bem
                                   if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT)
                                                 // maioria dos smartphones
                                                 larguraCompacta(vm = vm, scop = scop, windowSizeClass = windowSizeClass, m = Modifier)
                             else if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED)
                                                //maioria dos pc no modo paizagem
                                                larguraExpandida(vm = vm, scop = scop, windowSizeClass = windowSizeClass)
                             else if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM){
                                  if (windowSizeClass.windowHeightSizeClass==WindowHeightSizeClass.COMPACT) {
                                               //maioria dos celulares no modo paizagem
                                                larguraMediaAlturaCompacta(vm = vm, scop = scop, windowSizeClass = windowSizeClass)
                                                Log.e("texte ","largura media altura compacta")
                                  }
                                  else{      //maioria dos tablets no modo retrato
                                               larguraMedia(vm = vm, scop = scop, windowSizeClass = windowSizeClass)
                                       }
                             }
         }


 dialogoDatasFolgas( acaoFechar = {vm.estadosVm.disparaDatass.value=!vm.estadosVm.disparaDatass.value},vm = vm)
 dialogoDatasFerrias(vm = vm, acaoFechar = {vm.estadosVm.disparaDialogoFerias.value=!vm.estadosVm.disparaDialogoFerias.value})
}

@Composable
 fun larguraCompacta(vm:ViewModelTelas,scop:CoroutineScope,windowSizeClass: WindowSizeClass,m:Modifier) {
     LaunchedEffect(Unit) {
         Log.e("texte ","largura compacta")
     }
    Box(modifier = Modifier.fillMaxSize())
    {
                     when (vm.estadosVm.telas.value) {
                                      TelaNavegacaoSimples.calendario ->
                                                       Column(
                                                           horizontalAlignment = Alignment.CenterHorizontally,
                                                           modifier = Modifier.fillMaxSize()
                                                               .align(Alignment.TopCenter)
                                                               .matchParentSize()
                                                       ) {
                                                           Spacer(Modifier.padding(25.dp))
                                                           Text("   ${vm.nomeMes}")
                                                           Spacer(Modifier.padding(3.dp))
                                                           calendario(m = Modifier, vm = vm, windowSizeClass)
                                                       }

                                     TelaNavegacaoSimples.comfig ->
                                                       config(m = Modifier.align(Alignment.TopCenter)
                                                           .matchParentSize()
                                                           .padding(horizontal = 2.dp)/*.offset(y=60.dp)*/,
                                                           disparaDialogoDatas = {
                                                               scop.launch {
                                                                   vm.estadosVm.disparaDatass.value = !vm.estadosVm.disparaDatass.value
                                                               }
                                                           },
                                                           disparaDialogoFerias = {
                                                               scop.launch {
                                                                   vm.estadosVm.disparaDialogoFerias.value =
                                                                       !vm.estadosVm.disparaDatass.value
                                                               }
                                                           },
                                                           calbackSnackbar = { it ->
                                                               scop.launch {
                                                                  vm.hostState.showSnackbar(
                                                                       message = it,
                                                                       duration = SnackbarDuration.Short
                                                                   )
                                                               }
                                                           }, vm = vm, windowSizeClass)

                     }
    }
}
@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun larguraExpandida(vm:ViewModelTelas,scop:CoroutineScope,windowSizeClass: WindowSizeClass){
    PermanentNavigationDrawer(drawerContent = {
                                       if(windowSizeClass.windowHeightSizeClass==WindowHeightSizeClass.COMPACT)
                                           Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                                               IconButton(onClick = {
                                                   scop.launch {
                                                       vm.estadosVm.telasAlturaCompacta.value = TelaNavegacaoSinplesAlturaCompacta.relogio
                                                   }
                                               }){
                                                   //responsavel por navegar para tela de configuracao
                                                   Icon(
                                                       painterResource(R.drawable.baseline_access_time_24),
                                                       contentDescription = "relogio",
                                                       modifier = Modifier.height(30.dp)
                                                   )
                                               }
                                               IconButton(onClick = {
                                                   scop.launch {
                                                       vm.estadosVm.telasAlturaCompacta.value = TelaNavegacaoSinplesAlturaCompacta.selecoes
                                                   }
                                               }){
                                                   //responsavel por navegar para tela de configuracao
                                                   Icon(
                                                       painterResource(R.drawable.baseline_checklist_24),
                                                       contentDescription = "selecoes",
                                                       modifier = Modifier.height(30.dp)
                                                   )
                                               }
                                               IconButton(onClick = {
                                                   scop.launch {
                                                       vm.estadosVm.telasAlturaCompacta.value = TelaNavegacaoSinplesAlturaCompacta.datasFolgas
                                                   }
                                               }){
                                                   //responsavel por navegar para tela de configuracao
                                                   Icon(
                                                       painterResource(R.drawable.baseline_airline_seat_individual_suite_24),
                                                       contentDescription = "selecoes",
                                                       modifier = Modifier.height(30.dp)
                                                   )
                                               }
                                           }
                                              },
                               modifier = Modifier.fillMaxSize()){
    BoxWithConstraints  (modifier=Modifier.fillMaxSize()){

        LaunchedEffect(Unit) {
            vm.estadosVm.telasAlturaCompacta.value= TelaNavegacaoSinplesAlturaCompacta.relogio}
            FlowRow {
                                    calendario(m=Modifier,vm=vm,windowSizeClass)
                                    Spacer(modifier=Modifier.padding(8.dp))
                                   if (windowSizeClass.windowHeightSizeClass==WindowHeightSizeClass.COMPACT){
                                       Box(modifier = Modifier.fillMaxWidth(0.6f), contentAlignment = Alignment.Center){ painelExpandidoAlturaCompacta(vm,scop,windowSizeClass)}
                                   }
                                   else{
                                          FlowColumn {
                                              HorarioDosAlarmes(vm,calbackSnackbar = { it->vm.hostState.showSnackbar(it)},windowSizeClass)
                                              ModeloDeEscala(vm = vm,
                                              windowSizeClass = windowSizeClass)}
                                           Spacer(modifier=Modifier.padding(8.dp))
                                           FlowColumn {
                                               Ferias(vm=vm,stadoTransicao = vm.estadosVm.transicaoFerias,
                                                   scope = scop,
                                                   diparaDialogoFerias = {
                                                       vm.estadosVm.disparaDialogoFerias.value=!vm.estadosVm.disparaDialogoFerias.value
                                                   },
                                                   windowSizeClass )

                                               DataDasFolgas(vm = vm,
                                                   diparaDialogoDatas = {
                                                       scop.launch {  vm.estadosVm.disparaDatass.value=!vm.estadosVm.disparaDatass.value}
                                                   } ,
                                                   windowSizeClass)
                                           }

                                       }
                                   }

                     }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun painelExpandidoAlturaCompacta(vm:ViewModelTelas,scop:CoroutineScope,windowSizeClass: WindowSizeClass){
    FlowRow {

        when (val tela = vm.estadosVm.telasAlturaCompacta.value) {
            TelaNavegacaoSinplesAlturaCompacta.calendario ->{}


            TelaNavegacaoSinplesAlturaCompacta.relogio ->
                TimePickerAlturaCompacta(vm =vm ,calbackSnackbar = {vm.hostState.showSnackbar(it)},windowSizeClass)

            TelaNavegacaoSinplesAlturaCompacta.selecoes->{
                Spacer(Modifier.padding(3.dp))
                FeriasAlturaCompacta(vm=vm,
                    stadoTransicao = vm.estadosVm.transicaoFerias,
                    scope = scop,
                    diparaDialogoFerias = {
                        vm.estadosVm.disparaDialogoFerias.value =
                            !vm.estadosVm.disparaDialogoFerias.value
                    },
                    windowSizeClass
                )
                Spacer(Modifier.padding(10.dp))
                ModeloDeEscalaAlturaCompacta(
                    vm = vm,
                    windowSizeClass = windowSizeClass
                )
                Spacer(Modifier.padding(3.dp))

            }
            TelaNavegacaoSinplesAlturaCompacta.datasFolgas->
                DataDasFolgasAlturaCompacta(
                    vm = vm,
                    diparaDialogoDatas = {
                        scop.launch {
                            vm.estadosVm.disparaDatass.value =
                                !vm.estadosVm.disparaDatass.value
                        }
                    },
                    windowSizeClass
                )
        }

    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalLayoutApi::class)
@Composable
 fun larguraMedia(vm:ViewModelTelas,scop:CoroutineScope,windowSizeClass: WindowSizeClass){
    BoxWithConstraints(modifier=Modifier.fillMaxSize().scrollable(state = rememberScrollState(0), orientation = Orientation.Vertical)){

        Column {
                                  LaunchedEffect(Unit) {
                                      Log.e("texte ","lagura media")
                                  }
                                  calendario(m=Modifier,vm=vm,windowSizeClass)
                                  FlowRow(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                                 Column {
                                                     HorarioDosAlarmes(vm,calbackSnackbar = { it->},windowSizeClass)
                                                     ModeloDeEscala(vm = vm,
                                                     windowSizeClass = windowSizeClass)
                                                 }

                                                   Spacer(modifier=Modifier.padding(8.dp))
                                                   FlowColumn  {
                                                                Ferias(vm=vm,
                                                                    stadoTransicao = vm.estadosVm.transicaoFerias,
                                                                    scope = scop,
                                                                    diparaDialogoFerias = {
                                                                        vm.estadosVm.disparaDialogoFerias.value=!vm.estadosVm.disparaDialogoFerias.value
                                                                    },
                                                                    windowSizeClass )
                                                                Spacer(modifier=Modifier.padding(3.dp))


                                                                DataDasFolgas(vm = vm,
                                                                    diparaDialogoDatas = {
                                                                        scop.launch {  vm.estadosVm.disparaDatass.value=!vm.estadosVm.disparaDatass.value}
                                                                    } ,
                                                                    windowSizeClass)
                                                            }
                                  }
                     }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
 fun larguraMediaAlturaCompacta(vm:ViewModelTelas,scop:CoroutineScope,windowSizeClass: WindowSizeClass){
    Box (modifier=Modifier.fillMaxSize()){

                   val drawerState = rememberDrawerState(DrawerValue.Open)
                   PermanentNavigationDrawer (
                       drawerContent = {
                                          // view model.estadosVm.telas.value=TelaNavegacaoSimples.calendario recebe um objeto do tipo do enun ou calendario ou configuracao
                                          // que representa as posiveis navegacoe simples tive essa ideia pois a vi em um code lab
                                          Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                                                                IconButton(onClick = {
                                                                                       scop.launch {
                                                                                           vm.estadosVm.telasAlturaCompacta.value = TelaNavegacaoSinplesAlturaCompacta.calendario
                                                                                       }
                                                                                       }) {
                                                                                        Icon(
                                                                                            Icons.Default.DateRange,
                                                                                            contentDescription = "calendario",
                                                                                            modifier = Modifier.height(30.dp)
                                                                                        )
                                                                                           }


                                                                IconButton(onClick = {
                                                                    scop.launch {
                                                                        vm.estadosVm.telasAlturaCompacta.value = TelaNavegacaoSinplesAlturaCompacta.relogio
                                                                    }
                                                                                      }){
                                                                                           //responsavel por navegar para tela de configuracao
                                                                                          Icon(
                                                                                              painterResource(R.drawable.baseline_access_time_24),
                                                                                              contentDescription = "relogio",
                                                                                              modifier = Modifier.height(30.dp)
                                                                                          )
                                                                                         }
                                                                IconButton(onClick = {
                                                                               scop.launch {
                                                                                   vm.estadosVm.telasAlturaCompacta.value = TelaNavegacaoSinplesAlturaCompacta.selecoes
                                                                               }
                                                                }){
                                                                    //responsavel por navegar para tela de configuracao
                                                                                Icon(
                                                                                    painterResource(R.drawable.baseline_checklist_24),
                                                                                    contentDescription = "selecoes",
                                                                                    modifier = Modifier.height(30.dp)
                                                                                )
                                                                }
                                                               IconButton(onClick = {
                                                                                    scop.launch {
                                                                                        vm.estadosVm.telasAlturaCompacta.value = TelaNavegacaoSinplesAlturaCompacta.datasFolgas
                                                                                                 }
                                                                                    }){
                                                                                    //responsavel por navegar para tela de configuracao
                                                                                    Icon(
                                                                                        painterResource(R.drawable.baseline_airline_seat_individual_suite_24),
                                                                                        contentDescription = "selecoes",
                                                                                        modifier = Modifier.height(30.dp)
                                                                                    )
                                                                                    }
                                          }
                       }
                   ) {
                                  FlowRow {

                                      when (val tela = vm.estadosVm.telasAlturaCompacta.value) {
                                          TelaNavegacaoSinplesAlturaCompacta.calendario ->
                                              calendarioSmallSmall(
                                              Modifier,
                                              vm,
                                              windowSizeClass
                                                                  )

                                          TelaNavegacaoSinplesAlturaCompacta.relogio ->
                                                TimePickerAlturaCompacta(vm =vm ,calbackSnackbar = { it->vm.hostState.showSnackbar(it)},windowSizeClass)

                                          TelaNavegacaoSinplesAlturaCompacta.selecoes->{
                                                Spacer(Modifier.padding(3.dp))
                                                 FeriasAlturaCompacta(
                                                        vm = vm,
                                                        stadoTransicao = vm.estadosVm.transicaoFerias,
                                                        scope = scop,
                                                        diparaDialogoFerias = {
                                                            vm.estadosVm.disparaDialogoFerias.value =
                                                                !vm.estadosVm.disparaDialogoFerias.value
                                                        },
                                                        windowSizeClass
                                                        )
                                              Spacer(Modifier.padding(3.dp))
                                                 ModeloDeEscalaAlturaCompacta(
                                                               vm = vm,
                                                               windowSizeClass = windowSizeClass
                                                                )
                                              Spacer(Modifier.padding(3.dp))

                                          }
                                          TelaNavegacaoSinplesAlturaCompacta.datasFolgas->
                                                  DataDasFolgasAlturaCompacta(
                                                                            vm = vm,
                                                                            diparaDialogoDatas = {
                                                                                scop.launch {
                                                                                    vm.estadosVm.disparaDatass.value =
                                                                                        !vm.estadosVm.disparaDatass.value
                                                                                }
                                                                            },
                                                                            windowSizeClass
                                                                            )
                                      }

                                  }
                       }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
//dialogo de datas de folgas responsavel por permirtir a selecao das folgas
fun dialogoDatasFolgas(acaoFechar:()->Unit,vm:ViewModelTelas){
    if(vm.estadosVm.disparaDatass.value) Surface {
    ModalBottomSheet(onDismissRequest = acaoFechar) {
        var estate = rememberDatePickerState()

        Surface {   Box{

             TextButton (onClick ={
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
                 Text("Salvar Data")

            }


            DatePicker(state = estate, showModeToggle = true)





        }

        }}


}
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
//dialogo de datas de Ferias responsavel por permirtir a selecao das Ferias
fun dialogoDatasFerrias(vm: ViewModelTelas,acaoFechar:()->Unit){
    if(vm.estadosVm.disparaDialogoFerias.value) Surface {
        ModalBottomSheet(onDismissRequest = acaoFechar) {  var estate = rememberDateRangePickerState()
            TextButton(onClick ={
                var calendar=Calendar.getInstance()
                calendar.timeInMillis=estate.selectedStartDateMillis!!
                val diaInicio=calendar.get(Calendar.DAY_OF_MONTH)
                val mesInicio=calendar.get(Calendar.MONTH)
                val anoInicio=calendar.get(Calendar.YEAR)
                val masimoDia=calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                calendar= Calendar.getInstance()
                calendar.timeInMillis=estate.selectedEndDateMillis!!
                val diaFim=calendar.get(Calendar.DAY_OF_MONTH)
                val mesFim=calendar.get(Calendar.MONTH)
                val anoFim=calendar.get(Calendar.YEAR)
                val masimoDiaFim=calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                vm.inserirFerias(FeriasView(id=0,
                                            diaInicio= if ((diaInicio+1)>masimoDia) 1  else diaInicio,
                                            diaFim =  diaFim,
                                            mesFim = mesFim+1,
                                            mesInici = mesInicio+1,
                                            anoFim = anoFim,
                                            anoInici = anoInicio,
                                            check = true ))
            }) {
                Icon(painterResource(R.drawable.baseline_save_24),"salvar icone")
                Text("Salvar Intervalo Das Ferias")
            }
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
    BottomAppBar (modifier = Modifier.fillMaxWidth()
                                     .height(50.dp)
                                     .background(color = Color.Transparent)
                                     .clip(RoundedCornerShape(30.dp))
                                     .border(0.1.dp,color = Color.Transparent, shape = RoundedCornerShape(30.dp))
        ) {
         // view model.estadosVm.telas.value=TelaNavegacaoSimples.calendario recebe um objeto do tipo do enun ou calendario ou configuracao
        // que representa as posiveis navegacoe simples tive essa ideia pois a vi em um code lab
         NavigationBarItem(//responsavel por navegar  para tela de calendario
             onClick = {escopo.launch { vm.estadosVm.telas.value= TelaNavegacaoSimples.calendario }},
             selected = false,
             modifier = Modifier.fillMaxWidth(),
             label = {  },
             icon = { Icon(Icons.Default.DateRange,contentDescription = "calendario", modifier = Modifier.height(30.dp)) },
         )

        NavigationBarItem(//responsavel por navegar para tela de configuracao
            onClick = {escopo.launch { vm.estadosVm.telas.value= TelaNavegacaoSimples.comfig }},
            selected = false,
            modifier = Modifier.fillMaxWidth(),
            label = {  },
            icon = {Icon(Icons.Default.Settings,contentDescription = "configuracao",modifier=Modifier.height(30.dp))}
        )

    }
}
@Composable
fun dialogo61(){
    AlertDialog(onDismissRequest = { /*TODO*/ },
        confirmButton = { /*TODO*/ },
        title = { /*TODO*/ },
        text = { /*TODO*/ })
}
@Composable
fun dialogo1236(){}
fun dialogoSegunaAsextas(){}
@Composable
@Preview
fun telaInicial(){
    Surface {
      //  telainicial(ViewModelTelasContext as())
    }
}