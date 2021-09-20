package com.example.calculadoradeisr

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadoradeisr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       //Creacción del Spinner que muestra los diferentes periordos
        val arrayPeriodos=arrayOf("Diario","Semanal","Decenal","Quincenal","Mensual","anual")//Array de información
        val adaptador1 = ArrayAdapter(this, R.layout.simple_spinner_item,arrayPeriodos)//Creación de adaptador
        adaptador1.setDropDownViewResource(R.layout.simple_spinner_item)
        binding.sprPeriodo.adapter=adaptador1//Asignación de adaptador

        //Asignación de eveno click para el botón de Calcular
        binding.button.setOnClickListener{
            //Se verifica que se haya asignado un valor
            if(binding.txtIngresos.text.isEmpty()){
                val toast:Toast =Toast.makeText(applicationContext, "Ingrese su salario neto", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER , 0, 100);
                toast.show();
            //Se verifica que el valor no sea negativo
            }else if(binding.txtIngresos.text.toString().toDouble() < 0){
                val toast:Toast =Toast.makeText(applicationContext, "Ingrese un salario positivo", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER , 0, 100);
                toast.show();
            } else{
                //Si el valor es valido se crea el objeto Calculos con el valor y el período del Spinner
                val calculos=Calculos(binding.sprPeriodo.getSelectedItem().toString(),binding.txtIngresos.text.toString().toDouble())
                //Se envía el objeto al ResultadoActivity mediante un intent
                val intent = Intent(this, ResultadoActivity::class.java)
                intent.putExtra("calculo",calculos)
                startActivity(intent)
            }
        }
    }
}