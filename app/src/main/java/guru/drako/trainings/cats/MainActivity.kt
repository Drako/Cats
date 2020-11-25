package guru.drako.trainings.cats

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

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

    launch(Dispatchers.IO) {
      catApi.getCatBreeds()
    }

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

    val breedId = prefs.getString(Preferences.Breed, null)
    val categoryId = prefs.getString(Preferences.Category, null)?.toIntOrNull()
    val imageCount = prefs.getString(Preferences.ImageCount, null)?.toIntOrNull() ?: 6

    launch {
      catImageAdapter.imageUrls =
        catApi.getCatInfos(limit = imageCount, breedId = breedId, categoryId = categoryId)
          .map(CatInfo::url)
      swiper.isRefreshing = false
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    cancel()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    super.onCreateOptionsMenu(menu)
    menuInflater.inflate(R.menu.main_top_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.settings -> {
        // logInfo("Clicked on settings menu item.")
        // showToast(R.string.settings, LENGTH_SHORT)
        startActivity(Intent(this, SettingsActivity::class.java))
      }
    }
    return super.onOptionsItemSelected(item)
  }
}
