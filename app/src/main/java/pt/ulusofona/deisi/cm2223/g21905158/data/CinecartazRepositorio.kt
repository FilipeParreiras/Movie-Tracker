package pt.ulusofona.deisi.cm2223.g21905158.data

import android.content.Context
import android.util.Log
import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g21905158.data.local.entities.VisualizacaoCompleta
import pt.ulusofona.deisi.cm2223.g21905158.data.remote.ConnectivityUtil
import pt.ulusofona.deisi.cm2223.g21905158.models.Gestor
import pt.ulusofona.deisi.cm2223.g21905158.models.Cinema
import pt.ulusofona.deisi.cm2223.g21905158.models.Filme
import pt.ulusofona.deisi.cm2223.g21905158.models.FilmeDetalhe
import pt.ulusofona.deisi.cm2223.g21905158.models.Metricas
import pt.ulusofona.deisi.cm2223.g21905158.models.Visualizacao
import java.lang.IllegalStateException
import java.util.Dictionary

class CinecartazRepositorio private constructor(val local: Gestor, val remote: Gestor, val context: Context): Gestor() {

    companion object {
        private var instance: CinecartazRepositorio? = null

        fun init(local: Gestor, remote: Gestor, context: Context) {
            synchronized(this) {
                if (instance == null) {
                    instance = CinecartazRepositorio(local, remote, context)
                }
            }
        }

        fun getInstance(): CinecartazRepositorio {
            if (instance == null) {
                throw IllegalStateException("singleton not initialized")
            }
            return instance as CinecartazRepositorio
        }
    }

    override fun adicionarVisualizacao(visualizacao: Visualizacao, onFinished: () -> Unit) {
        Log.i("APP", "Adding visualization to database...")
        local.adicionarVisualizacao(visualizacao) {
            onFinished()
        }
    }

    override fun listarVisualizacoes(onFinished: (Result<List<Visualizacao>>) -> Unit) {
        Log.i("APP", "Searching cinemas in database...")
        local.listarVisualizacoes { result ->
            onFinished(result)
        }
    }

    override fun listarUltimasVisualizacoes(onFinished: (Result<List<Visualizacao>>) -> Unit) {
        Log.i("APP", "Searching visualization to database...")
        local.listarUltimasVisualizacoes { result ->
            onFinished(result)
        }
    }

    override fun calculaMetricas(onFinished: (Result<Metricas>) -> Unit) {
       local.calculaMetricas  { result ->
           if (result.isSuccess) {
               val metricas = result.getOrNull()!!
               Log.i("APP", "Got metrics from the server")
               onFinished(Result.success(metricas))
           } else {
               Log.w("APP", "Error getting metrics from server...")
               onFinished(result)  // propagate the remote failure
           }
       }
    }

    override fun adicionarDetalheFilme(filmeDetalhe: FilmeDetalhe, onFinished: () -> Unit) {
        Log.i("APP", "Adding movie detail to database...")
        local.adicionarDetalheFilme(filmeDetalhe) {
            onFinished()
        }
    }

    override fun pesquisarFilmes(termo: String, onFinished: (Result<List<Filme>>) -> Unit) {
        if (ConnectivityUtil.isOnline(context)) {
            Log.i("APP", "App is online. Getting movies from the server...")
            remote.pesquisarFilmes(termo) { result ->
                if (result.isSuccess) {
                    val filmes = result.getOrNull()!!
                    Log.i("APP", "Got ${filmes.size} movies from the server")
                    onFinished(Result.success(filmes))
                } else {
                    Log.w("APP", "Error getting movies from server...")
                    onFinished(result)  // propagate the remote failure
                }
            }
        } else {
            Log.i("APP", "App is offline. Cannot get movies...")
        }
    }

    override fun detalheFilme(titulo: String, onFinished: (Result<FilmeDetalhe>) -> Unit) {
        if (ConnectivityUtil.isOnline(context)) {
            Log.i("APP", "App is online. Getting movies from the server...")
            remote.detalheFilme(titulo) { result ->
                if (result.isSuccess) {
                    val filme = result.getOrNull()!!
                    Log.i("APP", "Got ${filme.title} from the server")
                    onFinished(Result.success(filme))
                } else {
                    Log.w("APP", "Error getting movies from server...")
                    onFinished(result)  // propagate the remote failure
                }
            }
        } else {
            Log.i("APP", "App is offline. Cannot get movies...")
        }
    }

    override fun adicionarCinemas(jsonObject: JSONObject, onFinished: () -> Unit) {
        Log.i("APP", "Adding cinemas to database...")
        local.adicionarCinemas(jsonObject) {
            onFinished()
        }
    }

    override fun pesquisarCinemas(termo: String, onFinished: (Result<List<Cinema>>) -> Unit) {
        Log.i("APP", "Searching cinemas in database...")
        local.pesquisarCinemas(termo) { result ->
            onFinished(result)
        }
    }

    override fun obterCinema(nome: String, onFinished: (Result<Cinema?>) -> Unit) {
        Log.i("APP", "Getting cinema from database...")
        local.obterCinema(nome) { result ->
            onFinished(result)
        }
    }
}