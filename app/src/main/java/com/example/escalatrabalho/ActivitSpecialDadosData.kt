package com.example.escalatrabalho

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.escalatrabalho.ui.theme.EscalaTrabalhoTheme

class ActivitSpecialDadosData : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EscalaTrabalhoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaDadosDatas(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TelaDadosDatas(name: String, modifier: Modifier = Modifier) {
   Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
      Box(modifier = Modifier.padding(innerPadding).fillMaxSize()){

       Column(modifier = Modifier.align(Alignment.Center)) {

          Row { Icon(Icons.Default.DateRange, contentDescription = "data dehoje")
                Text(text = "14/12/2024",modifier = Modifier.width(200.dp), fontSize = 20.sp) }
           Row {
                Icon(painterResource(R.drawable.baseline_access_time_24), contentDescription = "Horario atual")
                Text("10:00",modifier = Modifier.width(200.dp), fontSize = 20.sp)}

           Row {

                Text("Dia de folga !!!",modifier = Modifier.width(200.dp), fontSize = 20.sp)}
                Text("voce esta de Ferias !!!",modifier = Modifier.width(200.dp), fontSize = 20.sp)
                Text("Horario dos Alarmes : 10:00",modifier = Modifier.width(200.dp), fontSize = 20.sp)
                Text("Hoje e Feriado",modifier = Modifier.width(200.dp), fontSize = 20.sp)
       }





      }




   }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    EscalaTrabalhoTheme {
        TelaDadosDatas("Android")
    }
}