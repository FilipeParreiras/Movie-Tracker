package pt.ulusofona.deisi.cm2223.g21905158.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import pt.ulusofona.deisi.cm2223.g21905158.R
import pt.ulusofona.deisi.cm2223.g21905158.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var selectedItemId = 0

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.setOnItemSelectedListener { item ->
            selectedItemId = item.itemId
            when (selectedItemId) {
                R.id.dashbord_button -> {
                    val dashboardFragment = DashboardFragment()
                    changeFragment(dashboardFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.movies_button -> {
                    val moviesFragment = MoviesFragment()
                    changeFragment(moviesFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.create_button -> {
                    val createFragment = CreateFragment()
                    changeFragment(createFragment)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

        // preservar a rotação
        if (savedInstanceState != null) {
            selectedItemId = savedInstanceState.getInt("selectedItemId", R.id.dashbord_button)
            binding.navView.selectedItemId = selectedItemId
        }
        // inicializar o dashboard na primeira vez
        else {
            tabDashboard()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedItemId", binding.navView.selectedItemId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        selectedItemId = savedInstanceState.getInt("selectedItemId", R.id.dashbord_button)
        binding.navView.selectedItemId = selectedItemId
    }


    private fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun tabDashboard() {
        binding.navView.selectedItemId = R.id.dashbord_button
    }

    fun tabMovies() {
        binding.navView.selectedItemId = R.id.movies_button
    }

    fun tabCreate() {
        binding.navView.selectedItemId = R.id.create_button
    }
}