package com.novasa.imageinputprovider

import android.content.Context
import android.graphics.Bitmap
import androidx.activity.result.ActivityResultCaller
import com.novasa.activityresultprovider.ActivityResultProvider

class CameraImageInputSource: ActivityResultProvider<Unit, Bitmap> {

    private val SP_URI = "camera_input_uri"

    class Config(val title: String, val permissionRationale: String, val fileProviderAuthority: String, val maxWidth: Int)

    override fun onCreate(context: Context, caller: ActivityResultCaller, receiver: ActivityResultProvider.Receiver<Bitmap>) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun present(context: Context, input: Unit) {
        TODO("Not yet implemented")
    }
}