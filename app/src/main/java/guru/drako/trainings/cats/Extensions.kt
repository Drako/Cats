package guru.drako.trainings.cats

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes

inline fun <reified C> simpleName() = C::class.java.simpleName

inline fun <reified TagType> TagType.logInfo(message: String, tr: Throwable? = null) {
  Log.i(simpleName<TagType>(), message, tr)
}

fun Context.showToast(message: String, duration: Int) {
  Toast.makeText(this, message, duration).show()
}

fun Context.showToast(@StringRes messageId: Int, duration: Int) {
  Toast.makeText(this, messageId, duration).show()
}
