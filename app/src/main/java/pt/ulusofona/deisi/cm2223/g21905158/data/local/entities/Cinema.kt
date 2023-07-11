package pt.ulusofona.deisi.cm2223.g21905158.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cinemas")
data class Cinema(
    @PrimaryKey
    val id: Int,
    val nome: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val morada: String,
    val codPostal: String,
    val localidade: String
)