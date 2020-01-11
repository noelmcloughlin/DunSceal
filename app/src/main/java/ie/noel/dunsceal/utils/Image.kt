package ie.noel.dunsceal.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import ie.noel.dunsceal.R
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.*

object Image {

    fun showImagePicker(parent: Activity, id: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val chooser = Intent.createChooser(intent, R.string.select_profile_image.toString())
        parent.startActivityForResult(chooser, id)
    }

    fun readImage(activity: Activity, resultCode: Int, data: Intent?): Bitmap? {
        var bitmap: Bitmap? = null
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, data.data)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return bitmap
    }

    fun readImageFromPath(context: Context, path: String): Bitmap? {
        var bitmap: Bitmap? = null
        val uri = Uri.parse(path)
        if (uri != null) {
            try {
                val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
                val fileDescriptor = parcelFileDescriptor?.fileDescriptor
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                parcelFileDescriptor?.close()
            } catch (e: Exception) {
            }
        }
        return bitmap
    }

    fun readImageUri(resultCode: Int, data: Intent?): Uri? {
        var uri: Uri? = null
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            try { uri = data.data }
            catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return uri
    }

    fun convertImageToBytes(imageView: ImageView): ByteArray {
        // Get the data from an ImageView as bytes

        var bitmap: Bitmap =
            if (imageView is AdaptiveIconDrawable || imageView is AppCompatImageView)
                imageView.drawable.toBitmap()
            else
                (imageView.drawable as BitmapDrawable).toBitmap()

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}