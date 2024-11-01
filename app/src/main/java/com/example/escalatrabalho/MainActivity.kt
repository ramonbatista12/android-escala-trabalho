package com.example.escalatrabalho

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.work.WorkManager
import com.example.escalatrabalho.applicatio.AplicationCuston
import com.example.escalatrabalho.ui.theme.EscalaTrabalhoTheme
import com.example.escalatrabalho.viewModel.Fabricar
import com.example.escalatrabalho.views.telainicial


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
      enableEdgeToEdge()//estava estudando as dicas do google e vi essa dica ele permite customisar a cor de status bar e navigation bar do sistema android
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EscalaTrabalhoTheme {
                val classeDetela = currentWindowAdaptiveInfo().windowSizeClass

                    telainicial(
                        vm = viewModel(
                            factory = Fabricar().fabricar(
                                AplicationCuston.db.db, AplicationCuston.endpoint,
                                WorkManager.getInstance(applicationContext)
                            )
                        ),classeDetela
                    )
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