package com.example.tokumemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageButton
import java.io.IOException
import java.io.InputStream
import java.net.URL

class GetImage(private val image: ImageButton) :
    AsyncTask<String, Void, Bitmap>() {
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: String): Bitmap? {
        val image: Bitmap
        val imageUrl = URL(params[0])
        val imageIs: InputStream = imageUrl.openStream()
        image = BitmapFactory.decodeStream(imageIs)
        return image
    }
    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Bitmap) {
        // 取得した画像をImageViewに設定します。
        image.setImageBitmap(result)
    }
}