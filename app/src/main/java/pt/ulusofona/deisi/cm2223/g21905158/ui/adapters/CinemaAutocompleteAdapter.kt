package pt.ulusofona.deisi.cm2223.g21905158.ui.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import android.widget.Filterable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21905158.data.CinecartazRepositorio
import pt.ulusofona.deisi.cm2223.g21905158.models.Cinema


class CinemaAutocompleteAdapter(context: Context?, private val autoCompleteTextView: AutoCompleteTextView) :
    ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1), Filterable {
    private var resultado: List<Cinema> = listOf()
    var selecionado: Cinema? = null

    private var searchJob: Job? = null
    private val searchChannel = Channel<String>()

    init {
        autoCompleteTextView.setAdapter(this)
        setupTextWatcher()
        setupItemClickListener()
    }

    private fun setupItemClickListener() {
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selecionado = resultado[position]
        }
    }

    @OptIn(FlowPreview::class)
    private fun setupTextWatcher() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // O texto foi modificado, apagar seleção
                selecionado = null
                s?.let {
                    val searchQuery = it.toString()
                    CoroutineScope(Dispatchers.Main).launch {
                        searchChannel.send(searchQuery)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        // Attach the textWatcher to the AutoCompleteTextView
        autoCompleteTextView.addTextChangedListener(textWatcher)

        // Listen to search queries from the channel and perform filtering
        CoroutineScope(Dispatchers.Main).launch {
            searchChannel
                .receiveAsFlow()
                .debounce(300)
                .collect { query ->
                    filter.filter(query)
                }
        }
    }

    override fun getCount(): Int {
        return resultado.size
    }

    override fun getItem(index: Int): String {
        return resultado[index].nome
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                // Cancelar pedidos anteriores
                searchJob?.cancel()

                // Criar novo pedido à API
                searchJob = CoroutineScope(Dispatchers.Default).launch {
                    constraint?.let { searchQuery ->
                        // Fazer a pesquisa
                        CinecartazRepositorio.getInstance().pesquisarCinemas(searchQuery.toString()) { result ->
                            // Processar resultado na thread Main
                            CoroutineScope(Dispatchers.Main).launch {
                                if (result.isSuccess) {
                                    resultado = result.getOrNull()!!
                                    filterResults.values = resultado
                                    filterResults.count = resultado.size
                                    notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
        }
    }
}