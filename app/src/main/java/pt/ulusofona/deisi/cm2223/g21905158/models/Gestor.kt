package pt.ulusofona.deisi.cm2223.g21905158.models

import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g21905158.data.local.entities.VisualizacaoCompleta
import java.util.Dictionary

abstract class Gestor {
    abstract fun adicionarVisualizacao(visualizacao: Visualizacao, onFinished: () -> Unit)
    abstract fun listarVisualizacoes(onFinished: (Result<List<Visualizacao>>) -> Unit)
    abstract fun listarUltimasVisualizacoes(onFinished: (Result<List<Visualizacao>>) -> Unit)

    abstract fun calculaMetricas(onFinished: (Result<Metricas>) -> Unit)

    abstract fun adicionarDetalheFilme(filmeDetalhe: FilmeDetalhe, onFinished: () -> Unit)
    abstract fun pesquisarFilmes(termo: String, onFinished: (Result<List<Filme>>) -> Unit)
    abstract fun detalheFilme(titulo: String, onFinished: (Result<FilmeDetalhe>) -> Unit)

    abstract fun adicionarCinemas(jsonObject: JSONObject, onFinished: () -> Unit)
    abstract fun pesquisarCinemas(termo: String, onFinished: (Result<List<Cinema>>) -> Unit)
    abstract fun obterCinema(nome: String, onFinished: (Result<Cinema?>) -> Unit)
}