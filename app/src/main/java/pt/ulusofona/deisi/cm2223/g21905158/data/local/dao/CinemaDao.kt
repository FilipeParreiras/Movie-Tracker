package pt.ulusofona.deisi.cm2223.g21905158.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.ulusofona.deisi.cm2223.g21905158.data.local.entities.Cinema

@Dao
interface CinemaDao {

    @Query("SELECT * FROM cinemas WHERE id = :id")
    suspend fun selecionar(id: Int): Cinema?

    @Query("SELECT * FROM cinemas WHERE LOWER(nome) = LOWER(:nome)")
    suspend fun selecionar(nome: String): Cinema?

    @Query("SELECT * FROM cinemas WHERE nome LIKE '%' || LOWER(:termo) || '%'")
    suspend fun pesquisar(termo: String): List<Cinema>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(cinemas: List<Cinema>)
}