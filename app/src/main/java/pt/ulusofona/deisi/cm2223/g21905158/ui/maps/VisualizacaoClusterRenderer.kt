package pt.ulusofona.deisi.cm2223.g21905158.ui.maps

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import pt.ulusofona.deisi.cm2223.g21905158.R

class VisualizacaoClusterRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<VisualizacaoMarker>
) : DefaultClusterRenderer<VisualizacaoMarker>(context, map, clusterManager) {

    override fun shouldRenderAsCluster(cluster: Cluster<VisualizacaoMarker>): Boolean {
        return cluster.size >= 1
    }

    override fun getDescriptorForCluster(cluster: Cluster<VisualizacaoMarker>): BitmapDescriptor {
        val visualizacaoMarker = cluster.items.last()

        val marker = ContextCompat.getDrawable(context, R.drawable.baseline_marker_24)
        marker!!.setTint(visualizacaoMarker.getColor())

        val iconGenerator = IconGenerator(context)
        iconGenerator.setBackground(marker)

        return BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())
    }
}