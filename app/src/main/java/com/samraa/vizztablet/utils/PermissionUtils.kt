package com.samraa.vizztablet.utils

import android.content.Context
import android.os.Build
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

object PermissionUtils {

    fun requestReadExternalStoragePermission(context: Context, callback: PermissionCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestSinglePermission(
                context,
                android.Manifest.permission.READ_MEDIA_IMAGES,
                callback
            )
        } else {
            requestSinglePermission(
                context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                callback
            )
        }
    }

    private fun requestSinglePermission(
        context: Context,
        permission: String,
        callback: PermissionCallback
    ) {
        Dexter.withContext(context)
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                    callback.onPermissionRequest(granted = true)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    callback.onPermissionRequest(granted = false)
                }
            }).check()
    }
}


interface PermissionCallback {
    fun onPermissionRequest(granted: Boolean)
}