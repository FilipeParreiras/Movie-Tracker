package pt.ulusofona.deisi.cm2223.g21905158.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import pt.ulusofona.deisi.cm2223.g21905158.activities.DetailsGeneralFragment
import pt.ulusofona.deisi.cm2223.g21905158.activities.DetailsVisualizationFragment
import pt.ulusofona.deisi.cm2223.g21905158.models.Visualizacao

class DetailAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val visualizacao: Visualizacao) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2 // number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DetailsGeneralFragment(visualizacao.filme)
            1 -> DetailsVisualizationFragment(visualizacao)
            else -> DetailsGeneralFragment(visualizacao.filme)
        }
    }
}