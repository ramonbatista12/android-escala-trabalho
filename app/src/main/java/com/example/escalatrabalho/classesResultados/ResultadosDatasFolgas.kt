package com.example.escalatrabalho.classesResultados

import com.example.escalatrabalho.roomComfigs.DatasFolgas

sealed class ResultadosDatasFolgas {
    class caregando()
    class erro()
    class ok(val lista: List<DatasFolgas>)
}

sealed class ResultadosModeloTrabalh{
    class salvando()
    class visualizando()


}