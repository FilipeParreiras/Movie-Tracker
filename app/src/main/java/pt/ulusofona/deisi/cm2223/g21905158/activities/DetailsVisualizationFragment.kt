package pt.ulusofona.deisi.cm2223.g21905158.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ulusofona.deisi.cm2223.g21905158.databinding.FragmentDetailsVisualizationBinding
import pt.ulusofona.deisi.cm2223.g21905158.models.Visualizacao
import pt.ulusofona.deisi.cm2223.g21905158.ui.adapters.FotosAdapter

class DetailsVisualizationFragment(val visualizacao: Visualizacao) : Fragment() {
    private lateinit var binding: FragmentDetailsVisualizationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsVisualizationBinding.inflate(layoutInflater)

        binding.nomeCinema.text = visualizacao.cinema.nome
        binding.data.text = visualizacao.getDate()
        binding.avaliacao.progress = visualizacao.avaliacao
        binding.avaliacao.isEnabled = false
        binding.classificacaoEtaria.text = visualizacao.grauIdade
        binding.morada.text = visualizacao.cinema.morada
        binding.codigoPostal.text = visualizacao.cinema.codPostal
        binding.localidade.text = visualizacao.cinema.localidade
        println(visualizacao.cinema.codPostal)

        when(visualizacao.grauIdade){
            "M12" -> binding.classificacaoEtaria.text = "Maiores de 12"
            "M18" -> binding.classificacaoEtaria.text = "Maiores de 18"
        }
        binding.comentarios.text = visualizacao.observacoes
        println(visualizacao.observacoes.toString())

        binding.fotos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        println("HERE")
        println("URI: " + visualizacao.fotos.toString())
        binding.fotos.adapter = FotosAdapter(requireContext(), visualizacao.fotos)

        return binding.root
    }
}