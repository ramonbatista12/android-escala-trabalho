package com.example.escalatrabalho

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.escalatrabalho.serviceAlarme.BroadcastReceiverMensagems
import com.example.escalatrabalho.serviceAlarme.BroadcastsService
import com.example.escalatrabalho.ui.theme.EscalaTrabalhoTheme
import com.example.escalatrabalho.viewModel.ViewmodelActivitAlarme

class ActivityAlarrme : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EscalaTrabalhoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                  val vm = viewModel<ViewmodelActivitAlarme>()
                  val estadoHora=vm._hora.collectAsState("00:00")
                   Box(modifier = Modifier.padding(innerPadding)
                                             .background(color = androidx.compose.ui.graphics.Color.DarkGray)
                                             .fillMaxSize()
                      ){
                          Box(modifier = Modifier.width(400.dp)
                                                 .height(400.dp)
                                                 .align(alignment = Alignment.Center)
                              ){
                                Column (horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier=Modifier.align(alignment = Alignment.TopCenter)) {
                                    Text(text = " ALARME",
                                        color=Color.White,
                                        fontSize = 80.sp)
                                    Text(text = estadoHora.value,
                                         fontSize = 80.sp,
                                         textAlign = TextAlign.Center,
                                         color = Color.White )
                                    Row(horizontalArrangement = Arrangement.Center,modifier=Modifier.fillMaxWidth()) {
                                        OutlinedButton(onClick = {
                                            val intent= Intent(this@ActivityAlarrme
                                                ,BroadcastsService::class.java).also {
                                                it.action=BroadcastReceiverMensagems.Soneca.mensagems
                                                sendBroadcast(it)
                                                                                      }
                                                finish()
                                                                   },

                                        ) {
                                            Text(text = "Soneca")

                                        }
                                        Spacer(modifier = Modifier.padding(10.dp))

                                        OutlinedButton (onClick = {
                                            Intent(this@ActivityAlarrme,
                                                   BroadcastsService::class.java).also {
                                                it.action=BroadcastReceiverMensagems.Para.mensagems
                                                sendBroadcast(it)
                                                                                        }
                                            finish()
                                                                    }



                                        ) {

                                            Text(text = "Parar",
                                                color=Color.White)
                                        }
                                    }

                                }
                                }






                       }



                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    EscalaTrabalhoTheme {
        Greeting2("Android")
    }
}