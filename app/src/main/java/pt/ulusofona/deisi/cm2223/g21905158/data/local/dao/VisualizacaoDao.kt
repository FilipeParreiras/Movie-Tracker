package pt.ulusofona.deisi.cm2223.g21905158.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import pt.ulusofona.deisi.cm2223.g21905158.data.local.entities.Visualizacao
import pt.ulusofona.deisi.cm2223.g21905158.data.local.entities.VisualizacaoCompleta

@Dao
interface VisualizacaoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(visualizacao: Visualizacao): Long

    @Transaction
    @Query("SELECT * FROM visualizacoes")
    suspend fun listar(): List<VisualizacaoCompleta>

    @Query("SELECT * FROM visualizacoes ORDER BY data LIMIT 3")
    suspend fun listarUltimas(): List<VisualizacaoCompleta>

    @Transaction
    @Query("SELECT * FROM visualizacoes WHERE id = :id")
    suspend fun selecionar(id: Int): VisualizacaoCompleta?

    @Query("SELECT COUNT(DISTINCT cinemaId) FROM visualizacoes")
    suspend fun totalCinemasVisitados(): Int

    @Query("SELECT COUNT(DISTINCT filmeId) FROM visualizacoes")
    suspend fun totalFilmesVistos(): Int

    @Query("SELECT nome AS visitCount FROM visualizacoes JOIN cinemas ON visualizacoes.cinemaId = cinemas.id GROUP BY cinemaId ORDER BY COUNT(*) DESC LIMIT 1")
    suspend fun cinemaMaisVisitado(): String?
}