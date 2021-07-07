package com.novasa.activityresultprovider.imageinput

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.math.max

class ImageProcessor {

    private fun getCompressedImage(context: Context, uri: Uri): File? = context.contentResolver.openFileDescriptor(uri, "r", null)?.use { f: ParcelFileDescriptor ->
        downScaleAndCompressImage(context, FileInputStream(f.fileDescriptor), 1280, 75, null)
    }

    private fun compressImage(context: Context, inputFile: File): File = downScaleAndCompressImage(context, FileInputStream(inputFile), 1280, 75, ExifInterface(inputFile.absolutePath))

    private fun downScaleAndCompressImage(context: Context, inputStream: InputStream, maxSize: Int, compression: Int, exifIn: ExifInterface?): File {
        val outputFile = createImageFile(context)

        inputStream.buffered().use { input ->
            FileOutputStream(outputFile).use { output ->

                input.mark(input.available())

                val opts = BitmapFactory.Options()
                opts.inJustDecodeBounds = true

                BitmapFactory.decodeStream(input, null, opts)

                var sample = 1
                var size = max(opts.outWidth, opts.outHeight)

                SupportLog.d("Original Image size: ${opts.outWidth}, ${opts.outHeight}, max size: $maxSize, compression: $compression")

                while (size > maxSize) {
                    sample *= 2
                    SupportLog.d("Sampling down from size $size to ${size / 2}")
                    size /= 2
                }

                opts.inJustDecodeBounds = false
                opts.inSampleSize = sample

                input.reset()

                val bmp = BitmapFactory.decodeStream(input, null, opts) ?: throw SupportException("Could not decode image")
                bmp.compress(Bitmap.CompressFormat.JPEG, compression, output)

                exifIn?.let {
                    val exifOut = ExifInterface(outputFile.absolutePath)
                    exifOut.setAttribute(ExifInterface.TAG_ORIENTATION, it.getAttribute(ExifInterface.TAG_ORIENTATION))
                    exifOut.saveAttributes()
                }
            }
        }

        return outputFile
    }
}