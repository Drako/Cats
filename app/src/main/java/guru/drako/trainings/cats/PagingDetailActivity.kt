package guru.drako.trainings.cats

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_paging_detail.*
import java.lang.Exception

class PagingDetailActivity : AppCompatActivity() {
  @Parcelize
  class State(
    val imageUrls: Array<String>,
    val currentImage: Int
  ) : Parcelable

  companion object {
    private const val KEY_STATE = "PagingDetailActivity.State"

    fun start(context: Context, imageUrls: Array<String>, currentImage: Int) {
      ContextCompat.startActivity(context, Intent(context, this::class.java.declaringClass).apply {
        putExtra(KEY_STATE, State(imageUrls, currentImage))
      }, null)
    }
  }

  private val adapter = CatImageAdapter(isListItem = false)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_paging_detail)

    val state: State = savedInstanceState?.getParcelable(KEY_STATE)
      ?: intent.getParcelableExtra(KEY_STATE)
      ?: throw IllegalStateException("missing state")

    pager.adapter = adapter.apply { imageUrls = state.imageUrls.toList() }
    pager.setCurrentItem(state.currentImage, false)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putParcelable(
      KEY_STATE, State(
        imageUrls = adapter.imageUrls.toTypedArray(),
        currentImage = pager.currentItem
      )
    )

    super.onSaveInstanceState(outState)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> onBackPressed()
      R.id.share -> Sharing.shareImage(context = this, imageUri = adapter.imageUrls[pager.currentItem])
    }
    return true
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    super.onCreateOptionsMenu(menu)
    menuInflater.inflate(R.menu.top_detail_menu, menu)
    return true
  }
}
