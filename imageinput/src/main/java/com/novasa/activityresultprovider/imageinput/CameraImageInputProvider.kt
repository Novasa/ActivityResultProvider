package com.novasa.activityresultprovider.imageinput

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.novasa.activityresultprovider.ActivityResultProvider
import com.novasa.activityresultprovider.ContractActivityResultProvider
import com.novasa.activityresultprovider.permissions.RequestPermissionProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraImageInputProvider(
    private val config: Config,
    private val permissionProvider: RequestPermissionProvider,
    private val sharedPreferences: SharedPreferences
) : ContractActivityResultProvider<Unit, Bitmap?, Uri, Boolean>() {

    companion object {
        private const val SP_KEY_CAMERA_FILE_PATH = "camera_input_file_path"
    }

    class Config(val title: String, val permissionRationale: String, val fileProviderAuthority: String, val maxWidth: Int)

    override fun onCreate(context: Context, caller: ActivityResultCaller, receiver: ActivityResultProvider.Receiver<Bitmap?>) {
        super.onCreate(context, caller, receiver)

        permissionProvider.onCreate(context, caller, object : ActivityResultProvider.Receiver<Boolean> {

            override fun onActivityResultReceived(output: Boolean) {
                if (output) {
                    super@CameraImageInputProvider.present(context, Unit)
                }
            }

            override fun onActivityResultError() {
                receiver.onActivityResultError()
            }
        })
    }

    override fun present(context: Context, input: Unit) {
        permissionProvider.present(context, Manifest.permission.CAMERA)
    }

    override fun createContract(): ActivityResultContract<Uri, Boolean> = ActivityResultContracts.TakePicture()

    override fun createContractInput(context: Context, input: Unit): Uri {
        return createUri(context)
    }

    override fun parseOutput(context: Context, contractOutput: Boolean): Bitmap? {
        if (contractOutput) {

        }

        return null
    }

    private fun createUri(context: Context): Uri {

        val photoFile = createImageFile(context)

        saveCameraFilePath(photoFile.absolutePath)

        return FileProvider.getUriForFile(context, config.fileProviderAuthority, photoFile)
    }

    private fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun saveCameraFilePath(path: String) = sharedPreferences.edit()
        .putString(SP_KEY_CAMERA_FILE_PATH, path)
        .apply()

    private fun getCameraFilePath(): String? = sharedPreferences.getString(SP_KEY_CAMERA_FILE_PATH, null)

    private fun clearCameraFilePath() = sharedPreferences.edit()
        .remove(SP_KEY_CAMERA_FILE_PATH)
        .apply()
}