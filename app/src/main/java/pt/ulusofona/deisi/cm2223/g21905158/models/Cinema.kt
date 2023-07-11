package pt.ulusofona.deisi.cm2223.g21905158.models

data class Cinema(
    val id: Int,
    val nome: String,
    val latitude: Double,
    val longitude: Double,
    val morada: String,
    val codPostal: String,
    val localidade: String
) : java.io.Serializable
