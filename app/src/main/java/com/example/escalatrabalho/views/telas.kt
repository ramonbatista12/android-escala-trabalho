package com.example.escalatrabalho.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.escalatrabalho.viewModel.ViewModelTelas
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun telainicial(vm:ViewModelTelas){
      val scop = rememberCoroutineScope()

     Scaffold(modifier=Modifier.fillMaxSize(), bottomBar = {barraSuperior(vm =vm)}) {

     Box(modifier=Modifier.fillMaxSize()){
      when(vm.estadosVm.telas.value) {
          TelaNavegacaoSimples.calendario-> Column(modifier = Modifier.offset(x=10.dp)) {
              Spacer(Modifier.padding(25.dp))
              Text("   Mes de :${vm.nomeMes}")
              Spacer(Modifier.padding(3.dp))
              calendario(m=Modifier,vm=vm)
          }
          TelaNavegacaoSimples.comfig -> config(m=Modifier.align(Alignment.TopCenter).offset(y=60.dp),
              disparaDialogoDatas = {scop.launch {  vm.estadosVm.disparaDatass.value=!vm.estadosVm.disparaDatass.value}},
              disparaDialogoFerias={scop.launch {  vm.estadosVm.disparaDialogoFerias.value=!vm.estadosVm.disparaDatass.value}},vm=vm)
      }
         }
 dialogoDatasFolgas(disparar = vm.estadosVm.disparaDatass.value, acaoFechar = {vm.estadosVm.disparaDatass.value=!vm.estadosVm.disparaDatass.value})
 dialogoDatasFerrias(disparar =vm.estadosVm.disparaDialogoFerias.value, acaoFechar = {vm.estadosVm.disparaDialogoFerias.value=!vm.estadosVm.disparaDialogoFerias.value})
}

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//dialogo de datas de folgas responsavel por permirtir a selecao das folgas
fun  dialogoDatasFolgas(disparar:Boolean,acaoFechar:()->Unit){if(disparar) Surface {
    ModalBottomSheet(onDismissRequest = acaoFechar) {  var estate = rememberDatePickerState()
        Surface {   Box{
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
    NavigationBar(modifier = Modifier.fillMaxWidth().height(85.dp)) {
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