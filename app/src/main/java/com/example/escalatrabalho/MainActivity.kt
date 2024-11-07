package com.example.escalatrabalho

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.WorkManager
import com.example.escalatrabalho.applicatio.AplicationCuston
import com.example.escalatrabalho.repositorio.RepositorioPrincipal
import com.example.escalatrabalho.ui.theme.EscalaTrabalhoTheme
import com.example.escalatrabalho.viewModel.Fabricar
import com.example.escalatrabalho.viewModel.Fabricarvmain
import com.example.escalatrabalho.viewModel.MainViewModel
import com.example.escalatrabalho.views.telainicial


class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
      enableEdgeToEdge()//estava estudando as dicas do google e vi essa dica ele permite customisar a cor de status bar e navigation bar do sistema android
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EscalaTrabalhoTheme {
                val vmMain = viewModel<MainViewModel>(factory = Fabricarvmain().fabricar(AplicationCuston.db.db,AplicationCuston.endpoint))
                val comsedidas = vmMain._estadoPermicao.collectAsState(false)
                val requisicaoNotificacao =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                        if (it) vmMain.mudarEstado(it)
                    }


                val classeDetela = currentWindowAdaptiveInfo().windowSizeClass
                val listaPermicoes = mutableListOf<String>()


                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                )
                    vmMain.mudarEstado(false)
                else vmMain.mudarEstado(true)

               LaunchedEffect(Unit) {
                   vmMain.checarFeriados()
               }
                telainicial(
                    vm = viewModel(
                        factory = Fabricar().fabricar(
                            AplicationCuston.db.db, AplicationCuston.endpoint,
                            WorkManager.getInstance(applicationContext)
                        )
                    ), classeDetela
                )
                val state = vmMain.dialog
                if (state.value) {
                    AlertDialog(onDismissRequest = { vmMain.dialog.value = false },
                        confirmButton = {
                            Button(onClick = {
                                requisicaoNotificacao.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                            })
                            { Text("Ok permirtir") }
                        },

                        title = { Text("Permissões") },
                        text = { Text("e nessesaria a permissão para notificações do celular") })
                }
                val stadoDatasColetadas = vmMain._datasColetadas.collectAsState(false)
                if (!stadoDatasColetadas.value)
                    AlertDialog(onDismissRequest = { vmMain.dialog.value = false },
                        confirmButton = {
                            Button(onClick = {
                                vmMain.carregarFeriados()
                            })
                            { Text("caregar feriados") }
                        },

                        title = { Text("Ferriados") },
                        text = { Text("Posso caregar os feriados da internet e nessesesario o uso da rede") })


                     }
        }
    }
}



 fun ComponentActivity.enableEdgeToEdge(//aqui eu manipulo a cor de status bar e navigation bar do sistema android deixei ambos transparentes
     statusBar:SystemBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT,android.graphics.Color.TRANSPARENT)
     ,navigationBar:SystemBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT,android.graphics.Color.TRANSPARENT)) {}





    @Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EscalaTrabalhoTheme {
        Greeting("Android")
    }
}