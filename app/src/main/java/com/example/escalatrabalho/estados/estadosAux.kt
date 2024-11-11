package com.example.escalatrabalho.estados

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.mutableStateOf
import com.example.escalatrabalho.classesResultados.ResultadosSalvarDatasFolgas
import com.example.escalatrabalho.classesResultados.ResultadosSalvarHora

import com.example.escalatrabalho.enums.TelaNavegacaoSimples

class EstadosAuxVm(){//classe criada para manter os estados do viewmodel
var disparaDialogoFerias = mutableStateOf(false)//estado dialog datas de Ferias
    var disparaDatass= mutableStateOf(false)//estado dialog datas fiogas
    var transicaoDatPiker =
        MutableTransitionState(true)//estado transicao animacao Datapiker selecao hr
    var telas=
        mutableStateOf(TelaNavegacaoSimples.calendario)//estado das telas da navegacao simples
    var transicaoData=
        MutableTransitionState(false)//estado da transicao anomacao que mostra as datas das folgas
    var transicaoModeloTrabalho= MutableTransitionState(false)//estado transicao Modelo de trabalho
    var transicaoFerias= MutableTransitionState(false) //estado transicao anomacao mostra a parte modelo de trabalho
    var salvandoHorario= mutableStateOf(false)//estado de salvando horario
    var salvandoHorariosResultados= mutableStateOf(ResultadosSalvarHora.clicavel)//estado de salvando horarios
    var resultadosSalvarDatas=
        mutableStateOf(ResultadosSalvarDatasFolgas.clicavel)//estado de salvando datas de folgasSalvarDatasFolgas)//estado de salvando datas de folgas
}
