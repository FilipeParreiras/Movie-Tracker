package pt.ulusofona.deisi.cm2223.g21905158.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.ulusofona.deisi.cm2223.g21905158.data.local.entities.Filme

@Dao
interface FilmeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(filme: Filme)

    @Query("SELECT * FROM filmes WHERE imdbId = :imdbId")
    suspend fun selecionar(imdbId: String): Filme?

    @Query("DELETE FROM filmes")
    suspend fun limpar()
}