package pt.ulusofona.deisi.cm2223.g21905158.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.ulusofona.deisi.cm2223.g21905158.data.local.entities.Imagem

@Dao
interface ImagemDao {

    @Query("SELECT * FROM imagens WHERE id = :id")
    suspend fun selecionar(id: Int): Imagem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(imagens: List<Imagem>)
}