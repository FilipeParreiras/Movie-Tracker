package pt.ulusofona.deisi.cm2223.g21905158.activities

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import pt.ulusofona.deisi.cm2223.g21905158.R
import pt.ulusofona.deisi.cm2223.g21905158.databinding.ActivityDetailBinding
import pt.ulusofona.deisi.cm2223.g21905158.databinding.MovieGenreItemBinding
import pt.ulusofona.deisi.cm2223.g21905158.models.Visualizacao
import pt.ulusofona.deisi.cm2223.g21905158.ui.adapters.DetailAdapter
import java.util.*


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var visualizacao: Visualizacao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMovieDetails()

        val imageView = findViewById<ImageView>(binding.imageView.id)
        val imageUrl = visualizacao.filme.poster

        val requestOptions: RequestOptions = RequestOptions()
            .placeholder(R.color.boxBackground)
            .error(R.color.boxBackground)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        Glide.with(this)
            .load(imageUrl)
            .apply(requestOptions)
            .into(imageView)



        val adapter = DetailAdapter(supportFragmentManager, lifecycle, visualizacao)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.geral)
                1 -> tab.text = getString(R.string.visualizacao)
            }

        }.attach()

    }



    private fun loadMovieDetails() {
        visualizacao = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent?.getSerializableExtra("visualizacao", Visualizacao::class.java)!!
        else
            intent?.getSerializableExtra("visualizacao") as Visualizacao

        binding.movieTitle.text = visualizacao.filme.title
        binding.yearTextview.text = visualizacao.filme.year.toString()
        binding.ratingTextview.text = visualizacao.filme.rated
        binding.durationTextview.text = visualizacao.filme.runtime.toString()

        if (visualizacao.filme.genre != null) {
            visualizacao.filme.genre!!.split(", ").forEach { g ->
                val genreView = MovieGenreItemBinding.inflate(layoutInflater).root
                genreView.text = g
                binding.genreContainer.addView(genreView)
            }
        }
    }
}