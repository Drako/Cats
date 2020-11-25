package guru.drako.trainings.cats

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {
  private fun List<BreedInfo>.extract(extractor: (BreedInfo) -> String): Array<String> {
    return (sequenceOf("") + asSequence().map(extractor)).toList().toTypedArray()
  }

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preferences, rootKey)

    val breedPref = requireNotNull(findPreference<DropDownPreference>(Preferences.Breed))
    breedPref.entries = CatApi.cachedBreeds.extract(BreedInfo::name)
    breedPref.entryValues = CatApi.cachedBreeds.extract(BreedInfo::id)
  }
}
