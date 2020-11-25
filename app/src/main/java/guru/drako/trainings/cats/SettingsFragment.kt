package guru.drako.trainings.cats

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import kotlinx.coroutines.*

class SettingsFragment : PreferenceFragmentCompat(),
  CoroutineScope by (MainScope() + CoroutineName("SettingsActivity")) {

  companion object {
    private const val KEY_BREED_IDS = "SettingsFragment.BreedIds"
    private const val KEY_BREED_NAMES = "SettingsFragment.BreedNames"
    private const val KEY_BREED = "SettingsFragment.Breed"
    private const val KEY_CATEGORY_IDS = "SettingsFragment.CategoryIds"
    private const val KEY_CATEGORY_NAMES = "SettingsFragment.CategoryNames"
    private const val KEY_CATEGORY = "SettingsFragment.Category"
  }

  private val catApi = CatApi()

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preferences, rootKey)

    val breed = requireNotNull(findPreference<DropDownPreference>("preference_breed"))
    val category = requireNotNull(findPreference<DropDownPreference>("preference_category"))

    savedInstanceState?.also { bundle ->
      breed.entries = bundle.getStringArray(KEY_BREED_NAMES)
      breed.entryValues = bundle.getStringArray(KEY_BREED_IDS)
      breed.value = bundle.getString(KEY_BREED)

      category.entries = bundle.getStringArray(KEY_CATEGORY_NAMES)
      category.entryValues = bundle.getStringArray(KEY_CATEGORY_IDS)
      category.value = bundle.getString(KEY_CATEGORY)
    }?: run {
      val currentBreed = preferenceManager.sharedPreferences.getString("preference_breed", null) ?: ""
      val currentCategory = preferenceManager.sharedPreferences.getString("preference_category", null) ?: ""

      launch(Dispatchers.IO) {
        val breeds = catApi.getCatBreeds()
        withContext(Dispatchers.Main) {
          breed.entries = (listOf("") + breeds.map(BreedInfo::name)).toTypedArray()
          breed.entryValues = (listOf("") + breeds.map(BreedInfo::id)).toTypedArray()
          breed.value = currentBreed
        }
      }

      launch(Dispatchers.IO) {
        val categories = catApi.getCategories()
        withContext(Dispatchers.Main) {
          category.entries = (listOf("") + categories.map(CategoryInfo::name)).toTypedArray()
          category.entryValues = (listOf("") + categories.map { (id) -> "$id" }).toTypedArray()
          category.value = currentCategory
        }
      }
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    val breed = requireNotNull(findPreference<DropDownPreference>("preference_breed"))
    val category = requireNotNull(findPreference<DropDownPreference>("preference_category"))

    outState.putStringArray(KEY_BREED_NAMES, breed.entries.map { "$it" }.toTypedArray())
    outState.putStringArray(KEY_BREED_IDS, breed.entryValues.map { "$it" }.toTypedArray())
    outState.putString(KEY_BREED, breed.value)
    outState.putStringArray(KEY_CATEGORY_NAMES, category.entries.map { "$it" }.toTypedArray())
    outState.putStringArray(KEY_CATEGORY_IDS, category.entryValues.map { "$it" }.toTypedArray())
    outState.putString(KEY_CATEGORY, category.value)

    super.onSaveInstanceState(outState)
  }

  override fun onDestroy() {
    super.onDestroy()
    cancel()
  }
}
