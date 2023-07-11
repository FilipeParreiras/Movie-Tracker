package pt.ulusofona.deisi.cm2223.g21905158.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pt.ulusofona.deisi.cm2223.g21905158.R
import pt.ulusofona.deisi.cm2223.g21905158.databinding.FragmentDetailsGeneralBinding
import pt.ulusofona.deisi.cm2223.g21905158.models.Filme
import pt.ulusofona.deisi.cm2223.g21905158.models.FilmeDetalhe


class DetailsGeneralFragment(val movie: FilmeDetalhe) : Fragment() {
    private lateinit var binding: FragmentDetailsGeneralBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsGeneralBinding.inflate(layoutInflater)

        binding.imdbAvaliacoesTotal.text = movie.imdbVotes.toString()
        binding.progressBar.progress = movie.imdbRating?.toInt() ?: 0
        binding.movieDescription.text = movie.plot


        binding.seeMoreButton.setOnClickListener {
            if (binding.seeMoreButton.text == getString(R.string.ver_mais)) {
                binding.movieDescription.maxLines = Int.MAX_VALUE
                binding.seeMoreButton.text = getString(R.string.ver_menos)
            } else {
                binding.movieDescription.maxLines = 3
                binding.seeMoreButton.text = getString(R.string.ver_mais)
            }
        }



        return binding.root
    }




}