package com.example.calculadoradeisr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.calculadoradeisr.databinding.ItemResultadoBinding
//Adaptador para los ítems del ListView
class ResultadosAdapter (private val mContext: Context, private val listaResultados: List<ResultadoISR>)
    : ArrayAdapter<ResultadoISR>(mContext,0,listaResultados) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding  = ItemResultadoBinding.inflate(LayoutInflater.from(mContext),parent,false)

        val resultado = listaResultados[position]

        binding.encabezadoResul.text= resultado.encabezado
        if(resultado.encabezado=="Tasa:"){binding.txtResultado.text= "${resultado.resultado}%" }
        else{binding.txtResultado.text= "$${resultado.resultado}" }
        binding.imgResultado.setImageResource(resultado.imagen)
        return binding.root
    }
}