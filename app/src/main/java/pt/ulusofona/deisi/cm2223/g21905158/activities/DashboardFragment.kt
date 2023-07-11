package pt.ulusofona.deisi.cm2223.g21905158.activities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21905158.data.CinecartazRepositorio
import pt.ulusofona.deisi.cm2223.g21905158.databinding.FragmentDashboardBinding
import pt.ulusofona.deisi.cm2223.g21905158.models.Visualizacao
import pt.ulusofona.deisi.cm2223.g21905158.ui.adapters.MovieAdapter

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        CoroutineScope(Dispatchers.IO).launch {
            CinecartazRepositorio.getInstance().listarUltimasVisualizacoes { result ->
                if (result.isSuccess) {
                    val visualizacoes = result.getOrNull()!!.toTypedArray()

                    CoroutineScope(Dispatchers.Main).launch {
                        val adapter = MovieAdapter(visualizacoes)
                        binding.listaFilmes.setHasFixedSize(true)
                        binding.listaFilmes.layoutManager = LinearLayoutManager(binding.root.context)
                        binding.listaFilmes.adapter = adapter
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            CinecartazRepositorio.getInstance().calculaMetricas { result ->
                println(result)
                if (result.isSuccess){
                    val metricas = result.getOrNull()!!

                    println(metricas)

                    CoroutineScope(Dispatchers.Main).launch {
                        binding.totalFilmes.text = metricas.totalFilmes.toString()
                        binding.totalCinemas.text = metricas.totalCinemas.toString()
                        binding.cinemaMaisVisitado.text = metricas.cinemaMaisVisitado
                    }
                }
            }
        }

        /*binding.totalCinemas.text = Gestor.totalCinemas().toString()
        binding.generoMaisVisto.text = Gestor.generoMaisVisto()
        binding.cinemaMaisVisitado.text = Gestor.cinemaMaisVisto()
        */


        return binding.root
    }
}