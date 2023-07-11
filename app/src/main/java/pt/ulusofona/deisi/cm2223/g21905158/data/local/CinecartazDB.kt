package pt.ulusofona.deisi.cm2223.g21905158.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ulusofona.deisi.cm2223.g21905158.data.local.dao.*
import pt.ulusofona.deisi.cm2223.g21905158.data.local.entities.*

@Database(entities = [Cinema::class, Filme::class, Imagem::class, Visualizacao::class], version = 1, exportSchema = false)
abstract class CinecartazDB: RoomDatabase() {

    abstract fun cinemaDAO(): CinemaDao
    abstract fun filmeDAO(): FilmeDao
    abstract fun imagemDAO(): ImagemDao
    abstract fun visualizacaoDAO(): VisualizacaoDao

    companion object {
        private var instance: CinecartazDB? = null

        fun getInstance(applicationContext: Context): CinecartazDB {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        applicationContext,
                        CinecartazDB::class.java,
                        "cinecartaz_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                }
                return instance as CinecartazDB
            }
        }
    }
}