package guru.drako.trainings.cats

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
  private val catImageAdapter = CatImageAdapter(isListItem = true)
  private val catApi = CatApi()

  private val IMAGE_URLS_KEY = "ImageUrls"

  private var RecyclerView.spanCount: Int
    get() = (layoutManager as GridLayoutManager).spanCount
    set(value) {
      (layoutManager as GridLayoutManager).spanCount = value
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    catList.adapter = catImageAdapter

    when (resources.configuration.orientation) {
      Configuration.ORIENTATION_LANDSCAPE -> catList.spanCount = 3
      else -> catList.spanCount = 2
    }

    savedInstanceState?.getStringArray(IMAGE_URLS_KEY)?.toList()
      ?.also { catImageAdapter.imageUrls = it }
      ?: loadImages()

    swiper.setOnRefreshListener {
      loadImages()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putStringArray(IMAGE_URLS_KEY, catImageAdapter.imageUrls.toTypedArray())

    super.onSaveInstanceState(outState)
  }

  private fun loadImages() {
    val prefs = PreferenceManager.getDefaultSharedPreferences(this)

    val breedId = prefs.getString("preference_breed", null)?.takeUnless { it.isBlank() }
    val categoryId = prefs.getString("preference_category", null)?.takeUnless { it.isBlank() }

    launch {
      catImageAdapter.imageUrls =
        catApi.getCatInfos(breedId = breedId, categoryId = categoryId?.toInt()).map(CatInfo::url)
      swiper.isRefreshing = false
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    cancel()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    super.onCreateOptionsMenu(menu)
    menuInflater.inflate(R.menu.top_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
    }
    return super.onOptionsItemSelected(item)
  }
}
