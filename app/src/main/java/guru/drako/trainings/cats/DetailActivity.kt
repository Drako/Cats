package guru.drako.trainings.cats

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
  @Parcelize
  class State(
    val imageUrls: Array<String>,
    val currentImage: Int
  ) : Parcelable

  companion object {
    private const val KEY_STATE = "DetailActivity.State"

    fun start(context: Context, imageUrls: Array<String>, currentImage: Int) {
      ContextCompat.startActivity(context, Intent(context, this::class.java.declaringClass).apply {
        putExtra(KEY_STATE, State(imageUrls, currentImage))
      }, null)
    }
  }

  private lateinit var state: State

  private fun setCurrentImage(index: Int) {
    require(index in state.imageUrls.indices)
    state = State(currentImage = index, imageUrls = state.imageUrls)
    displayImage()
  }

  private fun displayImage() {
    Picasso.get().load(state.imageUrls[state.currentImage]).into(detailImageView)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)

    state = savedInstanceState?.getParcelable(KEY_STATE)
      ?: intent.getParcelableExtra(KEY_STATE)
      ?: throw IllegalStateException("missing state")

    displayImage()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putParcelable(KEY_STATE, state)

    super.onSaveInstanceState(outState)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> onBackPressed()
    }
    return true
  }
}
