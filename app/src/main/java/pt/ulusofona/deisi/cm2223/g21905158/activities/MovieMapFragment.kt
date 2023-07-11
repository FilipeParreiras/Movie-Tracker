package pt.ulusofona.deisi.cm2223.g21905158.activities

import android.content.Intent
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21905158.R
import pt.ulusofona.deisi.cm2223.g21905158.data.CinecartazRepositorio
import pt.ulusofona.deisi.cm2223.g21905158.databinding.FragmentMovieMapBinding
import pt.ulusofona.deisi.cm2223.g21905158.ui.maps.VisualizacaoClusterRenderer
import pt.ulusofona.deisi.cm2223.g21905158.ui.maps.VisualizacaoMarker

class MovieMapFragment : Fragment() {
    private val repositorio = CinecartazRepositorio.getInstance()

    private lateinit var binding: FragmentMovieMapBinding
    private lateinit var clusterManager: ClusterManager<VisualizacaoMarker>

    private fun calcularPontoMedio(localizacoes: List<LatLng>): LatLng? {
        if (localizacoes.isEmpty()) return null

        var minLat = Double.MAX_VALUE
        var maxLat = -Double.MAX_VALUE
        var minLng = Double.MAX_VALUE
        var maxLng = -Double.MAX_VALUE

        for (localizacao in localizacoes) {
            minLat = minOf(minLat, localizacao.latitude)
            maxLat = maxOf(maxLat, localizacao.latitude)
            minLng = minOf(minLng, localizacao.longitude)
            maxLng = maxOf(maxLng, localizacao.longitude)
        }

        val avgLat = (minLat + maxLat) / 2
        val avgLng = (minLng + maxLng) / 2

        return LatLng(avgLat, avgLng)
    }



    private val callback = OnMapReadyCallback { googleMap ->
        clusterManager = ClusterManager<VisualizacaoMarker>(requireContext(), googleMap)
        clusterManager.renderer = VisualizacaoClusterRenderer(requireContext(), googleMap, clusterManager)
        clusterManager.renderer.setOnClusterClickListener { cluster ->
            val visualizacaoMarker = cluster.items.last()!!

            /*val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("visualizacao", visualizacaoMarker.visualizacao)
            startActivity(intent)*/

            Toast.makeText(context, visualizacaoMarker.visualizacao.cinema.nome , Toast.LENGTH_SHORT).show()

            true
        }

        googleMap.setOnCameraIdleListener(clusterManager)

        try {
            // Load the custom map style JSON file
            val styleJson = resources.openRawResource(R.raw.map_style).bufferedReader().use { it.readText() }

            // Apply the custom map style to the Google Map
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))

        } catch (e: Exception) {
            // Handle exception
        }

        CoroutineScope(Dispatchers.IO).launch {
            repositorio.listarVisualizacoes { result ->
                if (result.isSuccess) {
                    val visualizacoes = result.getOrNull()!!

                    val pontoMedio = calcularPontoMedio(visualizacoes.map { v -> LatLng(v.cinema.latitude, v.cinema.longitude) })

                    clusterManager.addItems(visualizacoes.map { v -> VisualizacaoMarker(requireContext(), v) })

                    CoroutineScope(Dispatchers.Main).launch {
                        clusterManager.cluster()

                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(pontoMedio!!, 12f)
                        googleMap.moveCamera(cameraUpdate)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieMapBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}