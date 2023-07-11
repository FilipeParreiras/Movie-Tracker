package pt.ulusofona.deisi.cm2223.g21905158.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filmes")
data class Filme(
    @PrimaryKey
    val imdbId: String,

    val title: String,
    val year: Int?,
    val rated: String?,
    val released: Long?,
    val runtime: String?,
    val genre: String?,
    val director: String?,
    val writer: String?,
    val actors: String?,
    val plot: String?,
    val language: String?,
    val country: String?,
    val awards: String?,
    val poster: String?,
    val tomatometer: Double?,
    val metascore: Double?,
    val imdbRating: Double?,
    val imdbVotes: Int?,
    val dvd: Long?,
    val boxOffice: String?,
    val production: String?,
    val website: String?
)