package pt.ulusofona.deisi.cm2223.g21905158.data.remote

import android.widget.Toast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g21905158.OMDB_API_BASE_URL
import pt.ulusofona.deisi.cm2223.g21905158.OMDB_API_KEY
import pt.ulusofona.deisi.cm2223.g21905158.models.Cinema
import pt.ulusofona.deisi.cm2223.g21905158.models.Duracao
import pt.ulusofona.deisi.cm2223.g21905158.models.Filme
import pt.ulusofona.deisi.cm2223.g21905158.models.FilmeDetalhe
import pt.ulusofona.deisi.cm2223.g21905158.models.Gestor
import pt.ulusofona.deisi.cm2223.g21905158.models.Metricas
import pt.ulusofona.deisi.cm2223.g21905158.models.Visualizacao
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Dictionary
import java.util.Locale

class ServicoOmdb(val cliente: OkHttpClient) : Gestor() {

    override fun adicionarVisualizacao(visualizacao: Visualizacao, onFinished: () -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun listarVisualizacoes(onFinished: (Result<List<Visualizacao>>) -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun listarUltimasVisualizacoes(onFinished: (Result<List<Visualizacao>>) -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun calculaMetricas(onFinished: (Result<Metricas>) -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun adicionarDetalheFilme(filmeDetalhe: FilmeDetalhe, onFinished: () -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun pesquisarFilmes(termo: String, onFinished: (Result<List<Filme>>) -> Unit) {
        // Construir url com base no url da omdbapi
        val urlBuilder = OMDB_API_BASE_URL.toHttpUrlOrNull()?.newBuilder()

        // Add the parameters to the builder
        urlBuilder?.addQueryParameter("s", termo)
        urlBuilder?.addQueryParameter("type", "movie")
        urlBuilder?.addQueryParameter("apikey", OMDB_API_KEY)

        val request: Request = Request.Builder()
            .url(urlBuilder!!.build())
            .build()

        cliente.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFinished(Result.failure(e))
            }
            override fun onResponse(call: Call, response: Response) {
                response.apply {
                    if (!response.isSuccessful) {
                        onFinished(Result.failure(IOException("Unexpected code $response")))
                    } else {
                        val body = response.body?.string()
                        if (body != null) {
                            val jsonBody = JSONObject(body)

                            // Ver se é uma resposta válida
                            if (jsonBody["Response"].toString().toBoolean()) {
                                val resultado = jsonBody["Search"] as JSONArray

                                val filmes = mutableListOf<Filme>()

                                for (i in 0 until resultado.length()) {
                                    val filmeInfo = resultado[i] as JSONObject



                                    filmes.add(
                                        Filme(
                                            imdbId = filmeInfo["imdbID"].toString(),
                                            title = filmeInfo["Title"].toString(),
                                            year = filmeInfo["Year"].toString().toInt(),
                                            poster = filmeInfo["Poster"].toString(),
                                        )
                                    )
                                }
                                onFinished(Result.success(filmes))
                            }
                            else {
                                onFinished(Result.success(emptyList()))
                            }
                        }
                    }

                }
            }
        })
    }

    override fun detalheFilme(titulo: String, onFinished: (Result<FilmeDetalhe>) -> Unit) {
        // Construir url com base no url da omdbapi
        val urlBuilder = OMDB_API_BASE_URL.toHttpUrlOrNull()?.newBuilder()

        // Add the parameters to the builder
        urlBuilder?.addQueryParameter("t", titulo)
        urlBuilder?.addQueryParameter("type", "movie")
        urlBuilder?.addQueryParameter("apikey", OMDB_API_KEY)

        val request: Request = Request.Builder()
            .url(urlBuilder!!.build())
            .build()

        cliente.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFinished(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                response.apply {
                    if (!response.isSuccessful) {
                        onFinished(Result.failure(IOException("Unexpected code $response")))
                    } else {
                        val body = response.body?.string()
                        if (body != null) {
                            val resultado = JSONObject(body)

                            val ratingsJson = JSONArray(resultado["Ratings"].toString())
                            val ratings = (0 until ratingsJson.length()).map { ratingsJson.getJSONObject(it) }

                            val df = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

                            val filmeDetalhe = FilmeDetalhe(
                                imdbId = resultado["imdbID"].toString(),
                                title = resultado["Title"].toString()
                            )

                            if (resultado.has("Year") && resultado["Year"] != "N/A") {
                                filmeDetalhe.year = resultado["Year"].toString().toInt()
                            }
                            if (resultado.has("Rated") && resultado["Rated"] != "N/A") {
                                filmeDetalhe.rated = resultado["Rated"].toString()
                            }
                            if (resultado.has("Released") && resultado["Released"] != "N/A") {
                                filmeDetalhe.released = df.parse(resultado["Released"].toString())
                            }
                            if (resultado.has("Runtime") && resultado["Runtime"] != "N/A") {
                                filmeDetalhe.runtime = Duracao.parse(resultado["Runtime"].toString())
                            }
                            if (resultado.has("Genre") && resultado["Genre"] != "N/A") {
                                filmeDetalhe.genre = resultado["Genre"].toString()
                            }
                            if (resultado.has("Director") && resultado["Director"] != "N/A") {
                                filmeDetalhe.director = resultado["Director"].toString()
                            }
                            if (resultado.has("Writer") && resultado["Writer"] != "N/A") {
                                filmeDetalhe.writer = resultado["Writer"].toString()
                            }
                            if (resultado.has("Actors") && resultado["Actors"] != "N/A") {
                                filmeDetalhe.actors = resultado["Actors"].toString()
                            }
                            if (resultado.has("Plot") && resultado["Plot"] != "N/A") {
                                filmeDetalhe.plot = resultado["Plot"].toString()
                            }
                            if (resultado.has("Language") && resultado["Language"] != "N/A") {
                                filmeDetalhe.language = resultado["Language"].toString()
                            }
                            if (resultado.has("Country") && resultado["Country"] != "N/A") {
                                filmeDetalhe.country = resultado["Country"].toString()
                            }
                            if (resultado.has("Awards") && resultado["Awards"] != "N/A") {
                                filmeDetalhe.awards = resultado["Awards"].toString()
                            }
                            if (resultado.has("Poster") && resultado["Poster"] != "N/A") {
                                filmeDetalhe.poster = resultado["Poster"].toString()
                            }
                            if (resultado.has("Source")) {
                                filmeDetalhe.tomatometer = ratings.find { r -> r["Source"] == "Rotten Tomatoes" }!!["Value"].toString().replace("%", "").toDouble()
                                filmeDetalhe.metascore = ratings.find { r -> r["Source"] == "Metacritic" }!!["Value"].toString().replace("/100", "").toDouble()
                                filmeDetalhe.imdbRating = ratings.find { r -> r["Source"] == "Internet Movie Database" }!!["Value"].toString().replace("/10", "").toDouble()
                            }
                            if (resultado.has("imdbVotes") && resultado["imdbVotes"] != "N/A") {
                                filmeDetalhe.imdbVotes = resultado["imdbVotes"]
                                    .toString()
                                    .replace(",", "")
                                    .toInt()
                            }
                            if (resultado.has("DVD") && resultado["DVD"] != "N/A") {
                                filmeDetalhe.dvd = df.parse(resultado["DVD"].toString())
                            }
                            if (resultado.has("BoxOffice") && resultado["BoxOffice"] != "N/A") {
                                filmeDetalhe.boxOffice = resultado["BoxOffice"].toString()
                            }
                            if (resultado.has("Production") && resultado["Production"] != "N/A") {
                                filmeDetalhe.production = resultado["Production"].toString()
                            }
                            if (resultado.has("Website") && resultado["Website"] != "N/A") {
                                filmeDetalhe.website = resultado["Website"].toString()
                            }

                            onFinished(Result.success(filmeDetalhe))
                        }
                    }

                }
            }
        })
    }

    override fun adicionarCinemas(jsonObject: JSONObject, onFinished: () -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun pesquisarCinemas(termo: String, onFinished: (Result<List<Cinema>>) -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun obterCinema(nome: String, onFinished: (Result<Cinema?>) -> Unit) {
        throw Exception("Illegal operation")
    }

}