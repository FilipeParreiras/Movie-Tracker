package pt.ulusofona.deisi.cm2223.g21905158.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class VisualizacaoCompleta (
    @Embedded val visualizacao: Visualizacao,
    @Relation(
        parentColumn = "filmeId",
        entityColumn = "imdbId"
    )
    val filme: Filme,
    @Relation(
        parentColumn = "cinemaId",
        entityColumn = "id"
    )
    val cinema: Cinema,
    @Relation(
        parentColumn = "id",
        entityColumn = "visualizacaoId"
    )
    val imagens: List<Imagem>
)