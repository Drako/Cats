package guru.drako.trainings.cats

import android.os.Bundle
import android.text.InputType
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import kotlinx.coroutines.runBlocking

class SettingsFragment : PreferenceFragmentCompat() {
  // private val catApi = CatApi()

  private fun List<BreedInfo>.extract(extractor: (BreedInfo) -> String): Array<String> {
    return (sequenceOf("") + asSequence().map(extractor)).toList().toTypedArray()
  }

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preferences, rootKey)

    // val breeds = runBlocking {
    //    catApi.getCatBreeds()
    // }

    requireNotNull(findPreference<DropDownPreference>(Preferences.Breed))
      .apply {
        entries = CatApi.cachedBreeds.extract(BreedInfo::name)
        entryValues = CatApi.cachedBreeds.extract(BreedInfo::id)
      }

    requireNotNull(findPreference<EditTextPreference>(Preferences.ImageCount))
      .setOnBindEditTextListener { countPref ->
        countPref.setSingleLine()
        countPref.inputType = InputType.TYPE_CLASS_NUMBER
      }
  }
}
