package pt.ulusofona.deisi.cm2223.g21905158.ui.maps

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import pt.ulusofona.deisi.cm2223.g21905158.R
import pt.ulusofona.deisi.cm2223.g21905158.models.Visualizacao

data class VisualizacaoMarker(
    private val context: Context,
    val visualizacao: Visualizacao
) : ClusterItem {
    private val filme = visualizacao.filme
    private val cinema = visualizacao.cinema

    override fun getPosition(): LatLng {
        return LatLng(cinema.latitude, cinema.longitude)
    }

    override fun getTitle(): String {
        return "${filme.title} @ ${cinema.nome}"
    }

    override fun getSnippet(): String {
        return visualizacao.data.toString()
    }

    override fun getZIndex(): Float {
        return 0f
    }

    fun getColor(): Int {
        return when (visualizacao.avaliacao) {
            in 1..2 -> ContextCompat.getColor(context, R.color.very_bad)
            in 3..4 -> ContextCompat.getColor(context, R.color.bad)
            in 5..6 -> ContextCompat.getColor(context, R.color.medium)
            in 7..8 -> ContextCompat.getColor(context, R.color.good)
            in 9..10 -> ContextCompat.getColor(context, R.color.excellent)
            else -> Color.GRAY
        }
    }

    fun getAvaliacao(): Int {
        return visualizacao.avaliacao
    }
}