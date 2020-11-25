package guru.drako.trainings.cats

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Toast.LENGTH_SHORT
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream


object Sharing {
  private val picasso: Picasso = Picasso.get()

  class BitmapTarget(private val context: Context) : Target {
    private fun getLocalBitmapUri(bitmap: Bitmap): Uri {
      val imagePath = File(context.filesDir, "images").also { it.mkdirs() }
      val file = File(imagePath, "${context.getString(R.string.cat)}.jpg")
      FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
      }
      return FileProvider.getUriForFile(context, "guru.drako.fileprovider", file)
    }

    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
      val fileUri = getLocalBitmapUri(bitmap)

      val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, fileUri)
        flags = flags or Intent.FLAG_GRANT_READ_URI_PERMISSION
        type = "image/jpeg"
      }

      val chooserIntent = Intent.createChooser(shareIntent, context.resources.getText(R.string.share))

      context.packageManager.queryIntentActivities(chooserIntent, PackageManager.MATCH_DEFAULT_ONLY)
        .forEach { resolveInfo ->
          val packageName = resolveInfo.activityInfo.packageName
          context
            .grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

      startActivity(context, chooserIntent, null)
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
      logError("Loading bitmap failed.", e)
      context.showToast(R.string.sharing_failed, LENGTH_SHORT)
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
    }
  }

  fun shareImage(context: Context, imageUrl: String) {
    picasso.load(imageUrl).into(BitmapTarget(context))
  }
}
