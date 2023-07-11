package pt.ulusofona.deisi.cm2223.g21905158.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21905158.data.CinecartazRepositorio
import pt.ulusofona.deisi.cm2223.g21905158.databinding.FragmentCreateBinding
import pt.ulusofona.deisi.cm2223.g21905158.models.Visualizacao
import pt.ulusofona.deisi.cm2223.g21905158.ui.adapters.CinemaAutocompleteAdapter
import pt.ulusofona.deisi.cm2223.g21905158.ui.adapters.FotosAdapter
import pt.ulusofona.deisi.cm2223.g21905158.ui.adapters.MovieAutocompleteAdapter
import java.io.File
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.util.*


class CreateFragment : Fragment() {
    private lateinit var binding: FragmentCreateBinding
    private lateinit var datepickerDialog: DatePickerDialog

    private lateinit var movieAutocompleteAdapter: MovieAutocompleteAdapter
    private lateinit var cinemaAutocompleteAdapter: CinemaAutocompleteAdapter

    private var imagens = mutableListOf<String>()
    var totalImagens = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val myCalendar = Calendar.getInstance()
        datepickerDialog = DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                binding.dataI.setText("$dayOfMonth/$monthOfYear/$year")
            },
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateBinding.inflate(layoutInflater)
        binding.avaliacaoValor.text = "1"

        // Funcionamento dropdown
        movieAutocompleteAdapter = MovieAutocompleteAdapter(requireContext(), binding.tituloFilme)
        cinemaAutocompleteAdapter = CinemaAutocompleteAdapter(requireContext(), binding.nomeCinema)

        binding.fotos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.fotos.adapter = FotosAdapter(requireContext(), imagens)

        binding.dataI.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    datepickerDialog.show()
                }
            }
            true
        }

        binding.avaliacao.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.avaliacaoValor.text = progress.toString()

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        binding.imageButton.setOnClickListener{
            openImagePicker()
        }

        // Submeter ao clicar no botão
        binding.submeter.setOnClickListener {
            val filme = movieAutocompleteAdapter.selecionado
            var cinema = cinemaAutocompleteAdapter.selecionado
            val titulo = binding.tituloFilme.text
            val nome = binding.nomeCinema.text
            val avaliacao = binding.avaliacao.progress
            var grauIdade = binding.classificacaoEtaria.text.toString()
            var data = Date()
            if (binding.dataI.text.toString().length > 0 && data == Date()){
                data = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(binding.dataI.text.toString())
            }
            val observacoes = binding.observacao.text.toString()

            val tituloInvalido = titulo.isEmpty()
            val nomeInvalido = nome.isEmpty()
            val dataInvalida = data.toString().isEmpty() || data == Date()
            val observacoesInvalidas = observacoes.isEmpty()
            val formularioValido = !(tituloInvalido || nomeInvalido || dataInvalida || observacoesInvalidas)


            if (tituloInvalido) {
                binding.tituloFilmeLayout.error = "Input field required"
                Handler().postDelayed({ binding.tituloFilmeLayout.error = null }, 3000)
            }
            if (nomeInvalido){
                binding.nomeCinemaLayout.error = "Input field required"
                Handler().postDelayed({ binding.nomeCinemaLayout.error = null }, 3000)
            }
            if (dataInvalida){
                binding.data.error = "Input field required"
                Handler().postDelayed({ binding.data.error = null }, 3000)
            }
            if (observacoesInvalidas){
                binding.observacoesLayout.error = "Input field required"
                Handler().postDelayed({ binding.observacoesLayout.error = null }, 3000)
            }
            if(grauIdade == ""){
                grauIdade = "Geral"
            }

            // se texto vazio, lançar erro na caixa de texto
            // se texto preenchido:
            // caso 1 - temos filme, então usar id para obter detalhe
            // caso 2 - não temos filme, usar texto da caixa como título para encontrar filme detalhe
            // caso 2.1 - não encontrámos filme detalhe, lançar erro na caixa de texto
            if (formularioValido) {

                CoroutineScope(Dispatchers.IO).launch {
                    // Se temos filme selecionado, usar o seu titulo, se não, usar o texto da caixa
                    val tituloFilme = filme?.title ?: titulo.toString()

                    CinecartazRepositorio.getInstance().detalheFilme(tituloFilme) { result ->
                        if (result.isSuccess) {
                            val filmeDetalhe = result.getOrNull()

                            // Não temos filme mesmo assim...
                            if (filmeDetalhe == null) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    binding.tituloFilmeLayout.error = "Movie not found"
                                    Handler().postDelayed({ binding.tituloFilmeLayout.error = null }, 3000)
                                }
                            }

                            // Se temos cinema selecionado, usar o seu nome, se não, usar o texto da caixa
                            val nomeCinema = cinema?.nome ?: nome.toString()
                            CinecartazRepositorio.getInstance().obterCinema(nomeCinema) { result ->
                                if (result.isSuccess) {
                                    cinema = result.getOrNull()

                                    // Não temos cinema mesmo assim...
                                    if (cinema == null) {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            binding.nomeCinemaLayout.error = "Cinema not found"
                                            Handler().postDelayed({ binding.nomeCinemaLayout.error = null }, 3000)
                                        }
                                    }

                                    // Sucesso, inserir filme e visualização na BD
                                    if (filmeDetalhe != null && cinema != null) {
                                        CinecartazRepositorio.getInstance().adicionarDetalheFilme(filmeDetalhe) {
                                            CinecartazRepositorio.getInstance().adicionarVisualizacao(
                                                Visualizacao(filmeDetalhe, cinema!!, avaliacao, data, imagens, grauIdade.toString(), observacoes)
                                            ) {}
                                        }

                                        CoroutineScope(Dispatchers.Main).launch {
                                            (requireActivity() as MainActivity).tabMovies()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return binding.root
    }

    fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, totalImagens)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        /*if (requestCode == totalImagens && resultCode == Activity.RESULT_OK && intent != null) {
            val uri: Uri? = intent.data
            if (uri != null) {
                imagens.add(uri.toString())

                // Atualizar fotos no UI
                binding.fotos.adapter!!.notifyDataSetChanged()
            }}*/

        val imagePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_images")
        if (!imagePath.exists()) {
            imagePath.mkdirs()
        }

        val fileName = Date().time.toLong().toString()


        val imageFile = File(imagePath, "$fileName.jpg")

        // Save your image to the imageFile

        // Get the content URI using FileProvider

        // Save your image to the imageFile

        // Get the content URI using FileProvider
        val imageUri = FileProvider.getUriForFile(this.requireContext(), "pt.ulusofona.deisi.cm2223.g21905158", imageFile)

        imagens.add(imageUri.toString())
        binding.fotos.adapter!!.notifyDataSetChanged()
    }

}