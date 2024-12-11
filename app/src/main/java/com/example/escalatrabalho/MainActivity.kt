package com.example.escalatrabalho

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
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
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.ads.mediation.AbstractAdViewAdapter
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
     val tag = "MainActivity"
     var interesticial: InterstitialAd? =null



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
      enableEdgeToEdge()//estava estudando as dicas do google e vi essa dica ele permite customisar a cor de status bar e navigation bar do sistema android
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

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



                var classeDetela = currentWindowAdaptiveInfo().windowSizeClass
                var listaPermicoes = mutableListOf<String>()
                val stadoDatasColetadas = vmMain._datasColetadas.collectAsState(false)
                var permicaoEspecial = vmMain._permissaoEspecial.collectAsState(false)

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
                    ), classeDetela,{
                        scope.launch {
                            caregarAdmob()
                            disparar(vmMain)
                        }
                    }
                )

                val stateSnak= remember { SnackbarHostState() }

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
                    AlertDialog(onDismissRequest = { vmMain.cancelarColetaDeFeriados() },
                        confirmButton = {
                            Button(onClick = {
                                vmMain.carregarFeriados(callbackSackBarFalha = {
                                          scope.launch {  stateSnak.showSnackbar("falha ao carregar os feriados")}
                                },
                                callbackSackBarSucesso = {
                                    scope.launch {  stateSnak.showSnackbar("feriados carregados com sucesso")}
                                       }
                                )})
                            { Text("caregar feriados") }
                        },

                        title = { Text("Ferriados") },
                        text = { Text("Posso caregar os feriados da internet? e nessesesario o uso da rede") })

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
                val snackbarHost= SnackbarHost(stateSnak, modifier = Modifier)


                     }
        }
    }
    suspend fun  caregarAdmob(){
        val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
        val calback = object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.i(tag,"erro ao caaregar admob")
                interesticial=null
            }
            override fun onAdLoaded(p0: InterstitialAd) {
                super.onAdLoaded(p0)
                Log.i(tag,"admob carregado")
                interesticial=p0
                interesticial?.fullScreenContentCallback=object : com.google.android.gms.ads.FullScreenContentCallback(){
                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        super.onAdFailedToShowFullScreenContent(p0)
                        Log.i(tag,"erro  ao abrir admob ${p0.message}")
                    }

                    override fun onAdClicked() {

                        super.onAdClicked()
                        Log.i(tag,"admob clicado")
                }

                    override fun onAdImpression() {
                        super.onAdImpression()
                    }
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        Log.i(tag,"admob fechado")
                     interesticial=null
                    }
                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()

                        Log.i(tag,"admob aberto")
                    }
            }
        }}
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712",adRequest,calback
            )
    }
    fun ComponentActivity.enableEdgeToEdge(//aqui eu manipulo a cor de status bar e navigation bar do sistema android deixei ambos transparentes
        statusBar:SystemBarStyle = SystemBarStyle.auto(android.graphics.Color.BLACK,android.graphics.Color.TRANSPARENT)
        ,navigationBar:SystemBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT,android.graphics.Color.TRANSPARENT)) {



    }

    suspend fun disparar(vm: MainViewModel){
       if(interesticial!=null){
          if(vm.contador.value%5==0||vm.contador.value==0)
           interesticial?.show(this)
          vm.incrementaContador()
       }else vm.incrementaContador()
    }
}









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