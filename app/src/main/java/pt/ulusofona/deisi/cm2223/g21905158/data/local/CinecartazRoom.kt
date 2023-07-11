package pt.ulusofona.deisi.cm2223.g21905158.data.local

import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g21905158.data.local.entities.VisualizacaoCompleta
import pt.ulusofona.deisi.cm2223.g21905158.models.Cinema
import pt.ulusofona.deisi.cm2223.g21905158.models.Duracao
import pt.ulusofona.deisi.cm2223.g21905158.models.Filme
import pt.ulusofona.deisi.cm2223.g21905158.models.FilmeDetalhe
import pt.ulusofona.deisi.cm2223.g21905158.models.Gestor
import pt.ulusofona.deisi.cm2223.g21905158.models.Metricas
import pt.ulusofona.deisi.cm2223.g21905158.models.Visualizacao
import java.util.Date
import java.util.Dictionary
import pt.ulusofona.deisi.cm2223.g21905158.data.local.entities.Cinema as CinemaDB
import pt.ulusofona.deisi.cm2223.g21905158.data.local.entities.Visualizacao as VisualizacaoDB
import pt.ulusofona.deisi.cm2223.g21905158.data.local.entities.Filme as FilmeDetalheDB
import pt.ulusofona.deisi.cm2223.g21905158.data.local.entities.Imagem as ImagemDB

class CinecartazRoom(private val db: CinecartazDB): Gestor() {
    override fun adicionarVisualizacao(visualizacao: Visualizacao, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val id = db.visualizacaoDAO().inserir(
                VisualizacaoDB(
                    filmeId = visualizacao.filme.imdbId,
                    cinemaId = visualizacao.cinema.id,
                    avaliacao = visualizacao.avaliacao,
                    grauIdade = visualizacao.grauIdade,
                    data = visualizacao.data.time,
                    observacoes = visualizacao.observacoes
                )
            )
            db.imagemDAO().inserir(visualizacao.fotos.map {
                ImagemDB(
                    visualizacaoId = id,
                    filePath = it
                )
            })
            onFinished()
        }
    }

    override fun listarVisualizacoes(onFinished: (Result<List<Visualizacao>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            onFinished(Result.success(
                db.visualizacaoDAO().listar().map {
                    v -> Visualizacao(
                        filme = FilmeDetalhe(
                            imdbId = v.filme.imdbId,
                            title = v.filme.title,
                            year = v.filme.year,
                            rated = v.filme.rated,
                            released = v.filme.released?.let { Date(it) },
                            runtime = v.filme.runtime?.let { Duracao.parse(it) },
                            genre = v.filme.genre,
                            director = v.filme.director,
                            writer = v.filme.writer,
                            actors = v.filme.actors,
                            plot = v.filme.plot,
                            language = v.filme.language,
                            country = v.filme.country,
                            awards = v.filme.awards,
                            poster = v.filme.poster,
                            tomatometer = v.filme.tomatometer,
                            metascore = v.filme.metascore,
                            imdbRating = v.filme.imdbRating,
                            imdbVotes = v.filme.imdbVotes,
                            dvd = v.filme.dvd?.let { Date(it) },
                            boxOffice = v.filme.boxOffice,
                            production = v.filme.production,
                            website = v.filme.website
                        ),
                        cinema = Cinema(
                            id = v.cinema.id,
                            nome = v.cinema.nome,
                            latitude = v.cinema.latitude,
                            longitude = v.cinema.longitude,
                            morada = v.cinema.morada,
                            codPostal = v.cinema.codPostal,
                            localidade = v.cinema.localidade
                        ),
                        avaliacao = v.visualizacao.avaliacao,
                        data = Date(v.visualizacao.data),
                        fotos = v.imagens.map { i -> i.filePath },
                        grauIdade = v.visualizacao.grauIdade,
                        observacoes = v.visualizacao.observacoes
                    )
                })
            )
        }
    }

    override fun listarUltimasVisualizacoes(onFinished: (Result<List<Visualizacao>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            onFinished(Result.success(
                db.visualizacaoDAO().listarUltimas().map {
                    v ->  Visualizacao(
                    filme = FilmeDetalhe(
                        imdbId = v.filme.imdbId,
                        title = v.filme.title,
                        year = v.filme.year,
                        rated = v.filme.rated,
                        released = v.filme.released?.let { Date(it) },
                        runtime = v.filme.runtime?.let { Duracao.parse(it) },
                        genre = v.filme.genre,
                        director = v.filme.director,
                        writer = v.filme.writer,
                        actors = v.filme.actors,
                        plot = v.filme.plot,
                        language = v.filme.language,
                        country = v.filme.country,
                        awards = v.filme.awards,
                        poster = v.filme.poster,
                        tomatometer = v.filme.tomatometer,
                        metascore = v.filme.metascore,
                        imdbRating = v.filme.imdbRating,
                        imdbVotes = v.filme.imdbVotes,
                        dvd = v.filme.dvd?.let { Date(it) },
                        boxOffice = v.filme.boxOffice,
                        production = v.filme.production,
                        website = v.filme.website
                    ),
                    cinema = Cinema(
                        id = v.cinema.id,
                        nome = v.cinema.nome,
                        latitude = v.cinema.latitude,
                        longitude = v.cinema.longitude,
                        morada = v.cinema.morada,
                        codPostal = v.cinema.codPostal,
                        localidade = v.cinema.localidade
                    ),
                    avaliacao = v.visualizacao.avaliacao,
                    data = Date(v.visualizacao.data),
                    fotos = v.imagens.map { i -> i.filePath },
                    grauIdade = v.visualizacao.grauIdade,
                    observacoes = v.visualizacao.observacoes
                    )
                }
            ))
        }
    }

    override fun calculaMetricas(onFinished: (Result<Metricas>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            onFinished(Result.success(
                Metricas(
                    totalFilmes = db.visualizacaoDAO().totalFilmesVistos(),
                    totalCinemas = db.visualizacaoDAO().totalCinemasVisitados(),
                    cinemaMaisVisitado = db.visualizacaoDAO().cinemaMaisVisitado() ?: "N/A",
                )
            ))
        }
    }

    override fun adicionarDetalheFilme(filmeDetalhe: FilmeDetalhe, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            db.filmeDAO().inserir(
                FilmeDetalheDB(
                    imdbId = filmeDetalhe.imdbId,
                    title = filmeDetalhe.title,
                    year = filmeDetalhe.year,
                    rated = filmeDetalhe.rated,
                    released = filmeDetalhe.released?.time,
                    runtime = filmeDetalhe.runtime?.toString(),
                    genre = filmeDetalhe.genre,
                    director = filmeDetalhe.director,
                    writer = filmeDetalhe.writer,
                    actors = filmeDetalhe.actors,
                    plot = filmeDetalhe.plot,
                    language = filmeDetalhe.language,
                    country = filmeDetalhe.country,
                    awards = filmeDetalhe.awards,
                    poster = filmeDetalhe.poster,
                    tomatometer = filmeDetalhe.tomatometer,
                    metascore = filmeDetalhe.metascore,
                    imdbRating = filmeDetalhe.imdbRating,
                    imdbVotes = filmeDetalhe.imdbVotes,
                    dvd = filmeDetalhe.dvd?.time,
                    boxOffice = filmeDetalhe.boxOffice,
                    production = filmeDetalhe.production,
                    website = filmeDetalhe.website
                )
            )
            onFinished()
        }
    }

    override fun pesquisarFilmes(termo: String, onFinished: (Result<List<Filme>>) -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun detalheFilme(titulo: String, onFinished: (Result<FilmeDetalhe>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun adicionarCinemas(jsonObject: JSONObject, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val cinemasJson = JSONArray(jsonObject["cinemas"].toString())
            val cinemas = (0 until cinemasJson.length()).map { cinemasJson.getJSONObject(it) }

            db.cinemaDAO().inserir(cinemas.map {
                CinemaDB(
                    id = it["cinema_id"].toString().toInt(),
                    nome = it["cinema_name"].toString(),
                    latitude = it["latitude"].toString().toDouble(),
                    longitude = it["longitude"].toString().toDouble(),
                    morada = it["address"].toString(),
                    codPostal = it["postcode"].toString(),
                    localidade = it["county"].toString()

                )
            })
            onFinished()
        }
    }

    override fun pesquisarCinemas(termo: String, onFinished: (Result<List<Cinema>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val cinemasDB = db.cinemaDAO().pesquisar(termo)
            val cinemas = cinemasDB.map { c -> Cinema(c.id, c.nome, c.latitude, c.longitude, c.morada, c.codPostal, c.morada) }
            onFinished(Result.success(cinemas))
        }
    }

    override fun obterCinema(nome: String, onFinished: (Result<Cinema?>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val cinemaDB = db.cinemaDAO().selecionar(nome)
            if (cinemaDB != null) {
                onFinished(Result.success(Cinema(
                    cinemaDB.id,
                    cinemaDB.nome,
                    cinemaDB.latitude,
                    cinemaDB.longitude,
                    cinemaDB.morada,
                    cinemaDB.codPostal,
                    cinemaDB.localidade
                )))
            }
            else {
                onFinished(Result.success(null))
            }
        }
    }
}