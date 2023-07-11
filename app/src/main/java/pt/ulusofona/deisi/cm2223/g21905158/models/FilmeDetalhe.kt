package pt.ulusofona.deisi.cm2223.g21905158.models

import java.util.Date

class FilmeDetalhe(
    val imdbId: String,
    val title: String,
    var year: Int? = null,
    var rated: String? = null,
    var released: Date? = null,
    var runtime: Duracao? = null,
    var genre: String? = null,
    var director: String? = null,
    var writer: String? = null,
    var actors: String? = null,
    var plot: String? = null,
    var language: String? = null,
    var country: String? = null,
    var awards: String? = null,
    var poster: String? = null,
    var tomatometer: Double? = null,
    var metascore: Double? = null,
    var imdbRating: Double? = null,
    var imdbVotes: Int? = null,
    var dvd: Date? = null,
    var boxOffice: String? = null,
    var production: String? = null,
    var website: String? = null
) : java.io.Serializable