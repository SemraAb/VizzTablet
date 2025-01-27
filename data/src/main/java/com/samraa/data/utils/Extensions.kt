package com.samraa.data.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun File.toMultipartBodyPart(partName: String): MultipartBody.Part {
    val requestBody = this.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(partName, this.name, requestBody)
}

fun Uri.toFile(context: Context): File {
    val inputStream = context.contentResolver.openInputStream(this)
    val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
    tempFile.outputStream().use { outputStream ->
        inputStream?.copyTo(outputStream)
    }
    return tempFile
}
