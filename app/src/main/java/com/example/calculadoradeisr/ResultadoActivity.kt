package com.example.calculadoradeisr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calculadoradeisr.databinding.ActivityResultadoBinding

class ResultadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityResultadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Se recibe el objeto enviado desde el MainActivity
        val aCalcular=intent.getSerializableExtra("calculo") as Calculos
        binding.txtResultado2.text="Resultado en un período ${aCalcular.periodo.lowercase()} "
        //Variable que almacena la tabla correspondiente al período
        val tablaParticular:TablaParticular= llenarTabla(aCalcular.periodo.lowercase())
        //Mediante funciones se determinan todos los cálculos para el ISR
        //Ingreso Total
        val ingresoTot=ResultadoISR("Ingreso Tota:", aCalcular.sueldo, R.drawable.sueldo)
        //Límite Inferior
        val limiteInfe:ResultadoISR=encontrarLimiteInfe(ingresoTot.resultado,tablaParticular)
        //Base
        val base:ResultadoISR=encontrarBase(ingresoTot.resultado,limiteInfe.resultado)
        //Tasa
        val tasa:ResultadoISR=encontrarTasa(tablaParticular,limiteInfe.resultado)
        //Impuesto Marginal
        val inpuestoMargi:ResultadoISR=encontrarInpuestoMargi(base.resultado,tasa.resultado)
        //Cuota Fija
        val cuotaFija:ResultadoISR=encontrarCuotaFija(tablaParticular,limiteInfe.resultado)
        //Impuesto a Retener
        val impuestoRenta:ResultadoISR=encontrarImpuestoRenta(inpuestoMargi.resultado,cuotaFija.resultado)
        //Una vez que se tienen todos los resultados se crea un objeto ResultadoISR para mostrarse en una ListView
        val listaResultados= listOf<ResultadoISR>(ingresoTot,limiteInfe,base,tasa,inpuestoMargi,cuotaFija,impuestoRenta)
        val adaptador=ResultadosAdapter(this,listaResultados)
        binding.lista.adapter=adaptador//Se asigna el adaptador para el ListView
        binding.tuISREs.text="${impuestoRenta.resultado}"
    }
    //Función que determina la tabla a usar según el período escogido
    fun llenarTabla(periodo:String):TablaParticular{
        //Arreglos para cada columna de la tabla
        val tabla=TablaISR()
        var limInfe=arrayOf<Double>()
        var limSup=arrayOf<Double>()
        var cuataFija=arrayOf<Double>()
        var tasa=arrayOf<Double>()
        when(periodo){
            "diario" -> {
                limInfe=tabla.limInfeDia.copyOf()
                limSup=tabla.limSupDia.copyOf()
                cuataFija=tabla.cuataFijaDia.copyOf()
                tasa=tabla.tasaDia.copyOf()
            }
            "semanal" -> {
                limInfe=tabla.limInfeSem.copyOf()
                limSup=tabla.limSupSem.copyOf()
                cuataFija=tabla.cuataFijaSem.copyOf()
                tasa=tabla.tasaSem.copyOf()
             }
            "decenal" -> {
                limInfe=tabla.limInfeDec.copyOf()
                limSup=tabla.limSupDec.copyOf()
                cuataFija=tabla.cuataFijaDec.copyOf()
                tasa=tabla.tasaDec.copyOf()
             }
            "quincenal" -> {
                limInfe=tabla.limInfeQui.copyOf()
                limSup=tabla.limSupQui.copyOf()
                cuataFija=tabla.cuataFijaQui.copyOf()
                tasa=tabla.tasaQui.copyOf()
             }
            "mensual" -> {
                limInfe=tabla.limInfeMen.copyOf()
                limSup=tabla.limSupMen.copyOf()
                cuataFija=tabla.cuataFijaMen.copyOf()
                tasa=tabla.tasaMen.copyOf()
            }
            "anual" -> {
                limInfe=tabla.limInfeAnu.copyOf()
                limSup=tabla.limSupAnu.copyOf()
                cuataFija=tabla.cuataFijaAnu.copyOf()
                tasa=tabla.tasaAnu.copyOf()
            }
        }
        //Los arreglos (columnas) se almacenan en un objeto TablaParticular y se retorna
        val tablaParticular=TablaParticular(limInfe,limSup,cuataFija,tasa)
        return tablaParticular
    }
    //Función que calcula el Límite Inferior
    fun encontrarLimiteInfe(sueldo:Double,tablaParticular: TablaParticular):ResultadoISR{
        var resultado:Double=0.0
        for (i in 0..9) {
            if(sueldo >= tablaParticular.limInfe[i] && sueldo <= tablaParticular.limSup[i]){
                resultado=tablaParticular.limInfe[i]
            }
        }
        val limiteInfe=ResultadoISR("Límite Inferior:",resultado,R.drawable.menos)
        return limiteInfe
    }
    //Función que calcula el Base
    fun  encontrarBase(sueldo:Double,limiteInfe:Double):ResultadoISR{
        var resultado:Double=0.0
        resultado=sueldo-limiteInfe
        val rounded = String.format("%.2f", resultado)
        val base=ResultadoISR("Base:",rounded.toDouble(),R.drawable.igual)
        return base
    }
    //Función que calcula la Tasa
    fun encontrarTasa(tablaParticular: TablaParticular,limiteInfe:Double):ResultadoISR{
        var resultado:Double=0.0
        for (i in 0..9) {
            if(limiteInfe == tablaParticular.limInfe[i]){
                resultado=tablaParticular.tasa[i]
            }
        }
        val tasa=ResultadoISR("Tasa:",resultado,R.drawable.por)
        return tasa
    }
    //Función que calcula el Impuesto Marginal
    fun encontrarInpuestoMargi(base:Double,tasa:Double):ResultadoISR{
        var resultado:Double=0.0
        resultado=(base*tasa)/100
        val rounded = String.format("%.2f", resultado)
        val inpuestoMargi=ResultadoISR("Impuesto Marginal:",rounded.toDouble(),R.drawable.igual)
        return inpuestoMargi
    }
    //Función que calcula la Cuota Fija
    fun encontrarCuotaFija(tablaParticular: TablaParticular,limiteInfe:Double):ResultadoISR{
        var resultado:Double=0.0
        for (i in 0..9) {
            if(limiteInfe == tablaParticular.limInfe[i]){
                resultado=tablaParticular.cuataFija[i]
            }
        }
        val cuotaFija=ResultadoISR("Cuota Fija:",resultado,R.drawable.mas)
        return cuotaFija
    }
    //Función que calcula el Impuesto a Retener
    fun encontrarImpuestoRenta(inpuestoMargi:Double,cuotaFija:Double):ResultadoISR{
        var resultado:Double=0.0
        resultado=inpuestoMargi+cuotaFija
        val rounded = String.format("%.2f", resultado)
        val impuestoRenta=ResultadoISR("Impuesto a Retener:",rounded.toDouble(),R.drawable.igual)
        return impuestoRenta
    }
}