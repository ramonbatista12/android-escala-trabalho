package com.example.escalatrabalho

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
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
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
      enableEdgeToEdge()//estava estudando as dicas do google e vi essa dica ele permite customisar a cor de status bar e navigation bar do sistema android
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EscalaTrabalhoTheme {
                val vmMain = viewModel<MainViewModel>(factory = Fabricarvmain().fabricar(AplicationCuston.db.db,AplicationCuston.endpoint))
                val scope = rememberCoroutineScope()
                val comsedidas = vmMain._estadoPermicaoNotificacao.collectAsState(false)
                val requisicaoNotificacao =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                        if (it) vmMain.mudarEstadoNotificacao(it)
                    }
                val requisicaoPermissaoEspecial = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){
                    if (it) vmMain.mudarEstadoPermicaoEspecial(it)
                }


                val classeDetela = currentWindowAdaptiveInfo().windowSizeClass
                val listaPermicoes = mutableListOf<String>()
                val stadoDatasColetadas = vmMain._datasColetadas.collectAsState(false)
                val permicaoEspecial = vmMain._permissaoEspecial.collectAsState(false)

                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                ) vmMain.mudarEstadoNotificacao(false)

                else vmMain.mudarEstadoNotificacao(true)

                if (ContextCompat.checkSelfPermission(this,
                                                     android.Manifest.permission.SCHEDULE_EXACT_ALARM)
                                                     !=android.content.pm.PackageManager.PERMISSION_GRANTED){
                    Log.i("Use exact alarm ", "permicao negada")
                    vmMain.mudarEstadoPermicaoEspecial(true)
                }

                else{
                    Log.i("Use exact alarm ", "permicao aceita")
                    vmMain.mudarEstadoPermicaoEspecial( true)
                }

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

                if (!stadoDatasColetadas.value) {
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
                val estadoEspecial=vmMain.dialogPermissaoEspecial.value
                if(estadoEspecial){
                    AlertDialog(onDismissRequest = { vmMain.dialog.value = false },
                               confirmButton = {
                                   Button(onClick = {
                                       scope.launch {
                                     launch { startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM)) }
                                     launch { vmMain.dialogPermissaoEspecial.value=false }
                                                    }


                                                     })
                                   {
                                       Text("Ok permirtir")
                                   }
                                                   },
                               title = { Text("Permicao Para Alarme") },
                               text = { Text("e nessesaria a permissão para  criar alarmes precisos sem ela o app nao pode executar sua funcao principal") })



                }

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