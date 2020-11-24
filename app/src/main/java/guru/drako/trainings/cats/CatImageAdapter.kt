package guru.drako.trainings.cats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates.observable

class CatImageAdapter(private val isListItem: Boolean) : RecyclerView.Adapter<CatImageAdapter.ViewHolder>() {
  var imageUrls: List<String> by observable(listOf()) { _, _, _ ->
    notifyDataSetChanged()
  }

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(url: String) {
      val imageView = itemView.findViewById<ImageView>(R.id.imageView)
      Picasso.get().load(url).into(imageView)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater
      .from(parent.context)
      .inflate(if (isListItem) R.layout.image_list_item else R.layout.image_fragment, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(imageUrls[position])
    if (isListItem) {

      holder.itemView.setOnClickListener { view ->
        PagingDetailActivity.start(context = view.context, imageUrls = imageUrls.toTypedArray(), currentImage = position)
      }
    }
  }

  override fun getItemCount(): Int {
    return imageUrls.size
  }
}
