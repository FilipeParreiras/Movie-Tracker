package pt.ulusofona.deisi.cm2223.g21905158.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "imagens",
    foreignKeys = [
        ForeignKey(
            entity = Visualizacao::class,
            parentColumns = ["id"],
            childColumns = ["visualizacaoId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["visualizacaoId"])
    ]
)
data class Imagem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val visualizacaoId: Long,
    val filePath: String
)