package pt.ulusofona.deisi.cm2223.g21905158.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ulusofona.deisi.cm2223.g21905158.R
import pt.ulusofona.deisi.cm2223.g21905158.models.Visualizacao

class MovieAdapter(private val dataSet: Array<Visualizacao>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var onItemClickListener: ((position: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: ((position: Int) -> Unit)) {
        onItemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView
        val evalTextView: TextView
        val grauSatisfacao: TextView

        init {
            titleTextView = view.findViewById(R.id.title)
            evalTextView = view.findViewById(R.id.eval)
            grauSatisfacao = view.findViewById(R.id.grau_satisfacao)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        //Cria o elemento visual com base no XML
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.movie_item, viewGroup, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        //Atribuição dos valores
        viewHolder.titleTextView.text = dataSet[position].filme.title
        viewHolder.evalTextView.text = dataSet[position].avaliacao.toString()

        if (dataSet[position].avaliacao in 1..2){
            viewHolder.grauSatisfacao.text = "Muito Fraco"
        } else if(dataSet[position].avaliacao in 3..4){
            viewHolder.grauSatisfacao.text = "Fraco"
        } else if(dataSet[position].avaliacao in 5..6){
            viewHolder.grauSatisfacao.text = "Médio"
        } else if(dataSet[position].avaliacao in 7..8){
            viewHolder.grauSatisfacao.text = "Bom"
        } else if(dataSet[position].avaliacao in 9..10){
            viewHolder.grauSatisfacao.text = "Muito Bom"
        }

        //Bind do listener
        viewHolder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }
    override fun getItemCount() = dataSet.size
}