package pt.ulusofona.deisi.cm2223.g21905158.models

import android.net.Uri
import java.text.SimpleDateFormat
import java.util.*

class Visualizacao(
    var filme: FilmeDetalhe,
    var cinema: Cinema,
    var avaliacao: Int,
    var data: Date,
    var fotos: List<String>,
    var grauIdade: String,
    var observacoes: String?
) : java.io.Serializable{

    fun getDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(data)
    }
}