package pt.ulusofona.deisi.cm2223.g21905158.models

class Duracao(val minutos: Int) : java.io.Serializable {
    companion object {
        fun parse(duracao: String) : Duracao  {
            val regex = Regex("(\\d+)\\s*min")
            val resultados = regex.find(duracao)

            val minutos = resultados?.let { resultado ->
                val (minutos) = resultado.destructured
                minutos.toIntOrNull() ?: 0
            } ?: throw IllegalArgumentException("Invalid duration string: $duracao")

            return Duracao(minutos)
        }
    }

    override fun toString(): String {
        return "$minutos min"
    }
}