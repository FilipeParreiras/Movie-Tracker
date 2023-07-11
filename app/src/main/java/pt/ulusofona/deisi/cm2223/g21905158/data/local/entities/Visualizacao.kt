package pt.ulusofona.deisi.cm2223.g21905158.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "visualizacoes",
    foreignKeys = [
        ForeignKey(
            entity = Cinema::class,
            parentColumns = ["id"],
            childColumns = ["cinemaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Filme::class,
            parentColumns = ["imdbId"],
            childColumns = ["filmeId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["cinemaId"]),
        Index(value = ["filmeId"]),
    ]
)
data class Visualizacao(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val filmeId: String,
    val cinemaId: Int,
    val avaliacao: Int,
    val grauIdade: String,
    val data: Long,

    val observacoes: String?
)