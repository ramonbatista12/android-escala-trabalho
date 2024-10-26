package com.example.escalatrabalho

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import androidx.work.WorkManager
import com.example.escalatrabalho.applicatio.AplicationCuston
import com.example.escalatrabalho.roomComfigs.RoomDb
import com.example.escalatrabalho.ui.theme.EscalaTrabalhoTheme
import com.example.escalatrabalho.viewModel.Fabricar
import com.example.escalatrabalho.viewModel.ViewModelTelas
import com.example.escalatrabalho.views.telainicial


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
      enableEdgeToEdge()//estava estudando as dicas do google e vi essa dica ele permite customisar a cor de status bar e navigation bar do sistema android
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EscalaTrabalhoTheme {


               telainicial(vm = viewModel(
                   factory = Fabricar().fabricar(AplicationCuston.db.db,
                    WorkManager.getInstance(applicationContext))))
            }
        }
    }

 fun ComponentActivity.enableEdgeToEdge(//aqui eu manipulo a cor de status bar e navigation bar do sistema android deixei ambos transparentes
     statusBar:SystemBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT,android.graphics.Color.TRANSPARENT)
     ,navigationBar:SystemBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT,android.graphics.Color.TRANSPARENT)) {}


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