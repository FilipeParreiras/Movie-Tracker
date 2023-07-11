package pt.ulusofona.deisi.cm2223.g21905158.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import pt.ulusofona.deisi.cm2223.g21905158.R
import pt.ulusofona.deisi.cm2223.g21905158.databinding.FragmentMoviesBinding
import java.util.*
import kotlin.collections.HashMap


class MoviesFragment : Fragment() {
    private var selectedFragmentIndex = 0
    private lateinit var fragments: List<Fragment>
    private lateinit var binding: FragmentMoviesBinding
    private val icons = hashMapOf<Int, Int>(
        0 to R.drawable.baseline_map_24, //TODO: substituir estes por icones
        1 to R.drawable.baseline_list_24
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater)

        fragments = listOf(MovieListFragment(), MovieMapFragment())

        binding.fab.setOnClickListener {
            if (selectedFragmentIndex == 0) {
                setSelectedFragment(1)
            }
            else if (selectedFragmentIndex == 1) {
                setSelectedFragment(0)
            }
            changeFragment()
        }

        if (savedInstanceState != null) {
            selectedFragmentIndex = savedInstanceState.getInt("selectedFragmentIndex")
        } else {
            setSelectedFragment(0)
        }
        changeFragment()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedSubFragmentIndex", selectedFragmentIndex)
    }

    private fun setSelectedFragment(index: Int) {
        selectedFragmentIndex = index
        binding.fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), icons[index]!!))
    }

    private fun changeFragment() {
        val fragment = fragments[selectedFragmentIndex]
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(binding.movieContainer.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}