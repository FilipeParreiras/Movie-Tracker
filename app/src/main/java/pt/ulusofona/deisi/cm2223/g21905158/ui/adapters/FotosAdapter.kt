package pt.ulusofona.deisi.cm2223.g21905158.ui.adapters
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okio.Path.Companion.toPath
import pt.ulusofona.deisi.cm2223.g21905158.databinding.FotosLayoutBinding

class FotosAdapter(private val context: Context, private val uris: List<String>) :
    RecyclerView.Adapter<FotosAdapter.ViewHolder>() {
    private lateinit var binding: FotosLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FotosLayoutBinding.inflate(LayoutInflater.from(context))
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*val uri = uris[position].toUri()
        println("URI: " + uri)
        holder.imageView.setImageURI(uri)*/

        val uri = uris[position].toUri()
        Glide.with(context)
            .load(uri)
            .into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return uris.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = this@FotosAdapter.binding.foto
    }
}