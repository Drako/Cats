package guru.drako.trainings.cats

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class SettingsActivity : AppCompatActivity(), CoroutineScope by (MainScope() + CoroutineName("SettingsActivity")) {
  override fun onDestroy() {
    super.onDestroy()
    cancel()
  }
}
