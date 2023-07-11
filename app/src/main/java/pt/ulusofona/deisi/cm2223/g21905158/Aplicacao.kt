package pt.ulusofona.deisi.cm2223.g21905158

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g21905158.data.CinecartazRepositorio
import pt.ulusofona.deisi.cm2223.g21905158.data.local.CinecartazDB
import pt.ulusofona.deisi.cm2223.g21905158.data.local.CinecartazRoom
import pt.ulusofona.deisi.cm2223.g21905158.data.remote.ServicoOmdb
import java.io.BufferedReader

class Aplicacao : Application() {

    override fun onCreate() {
        super.onCreate()

        CinecartazRepositorio.init(
            local = CinecartazRoom(CinecartazDB.getInstance(this)),
            remote = ServicoOmdb(OkHttpClient()),
            context = this
        )

        CoroutineScope(Dispatchers.IO).launch {
            CinecartazRepositorio.getInstance().adicionarCinemas(
                JSONObject(resources.openRawResource(R.raw.cinemas).bufferedReader().use(BufferedReader::readText))
            ) {}
        }


        Log.i("APP", "Initialized repository")
    }
}