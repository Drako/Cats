package guru.drako.trainings.cats

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment.DIRECTORY_PICTURES
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream


object Sharing {
  private val picasso: Picasso = Picasso.get()

  private class BitmapTarget(private val context: Context) : Target {
    private fun getLocalBitmapUri(bitmap: Bitmap): Uri {

      val imagePath = File(context.filesDir, "images").also { it.mkdirs() }
      val file = File(imagePath, "${context.getString(R.string.cat)}.jpg")
      FileOutputStream(file).use {
        bitmap.compress(CompressFormat.JPEG, 90, it)
      }
      return FileProvider.getUriForFile(context, "guru.drako.fileprovider", file)
    }

    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
      val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap))
        type = "image/jpeg"
      }
      startActivity(context, Intent.createChooser(shareIntent, context.resources.getText(R.string.share)), null)
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
      logError("Loading bitmap failed for sharing", e)
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
    }
  }

  fun shareImage(context: Context, imageUri: String) {
    picasso.load(imageUri).into(BitmapTarget(context))
  }
}
