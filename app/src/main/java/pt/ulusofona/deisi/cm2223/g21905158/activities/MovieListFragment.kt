package pt.ulusofona.deisi.cm2223.g21905158.activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21905158.data.CinecartazRepositorio
import pt.ulusofona.deisi.cm2223.g21905158.databinding.FragmentMovieListBinding
import pt.ulusofona.deisi.cm2223.g21905158.models.Visualizacao
import pt.ulusofona.deisi.cm2223.g21905158.ui.adapters.MovieAdapter

class MovieListFragment : Fragment() {
    private lateinit var binding: FragmentMovieListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(layoutInflater)

        CoroutineScope(Dispatchers.IO).launch {
            CinecartazRepositorio.getInstance().listarVisualizacoes { result ->
                if (result.isSuccess) {
                    val visualizacoes = result.getOrNull()!!
                    CoroutineScope(Dispatchers.Main).launch {
                        val adapter = MovieAdapter(visualizacoes.toTypedArray())
                        binding.movieList.setHasFixedSize(true)
                        binding.movieList.layoutManager = LinearLayoutManager(binding.root.context)
                        binding.movieList.adapter = adapter

                        // Abrir atividade quando clicarmos num item da lista
                        adapter.setOnItemClickListener {
                            val intent = Intent(activity, DetailActivity::class.java)
                            intent.putExtra("visualizacao", visualizacoes[it])
                            startActivity(intent)
                        }
                    }
                }
            }
        }



        return binding.root
    }
}