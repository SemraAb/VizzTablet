package com.samraa.data.models.dto

import androidx.annotation.StringRes
import com.samraa.data.R
import com.samraa.data.utils.ResponseStatus

class ErrorDialogDto(
    var title: String? = "",
    var message: String? = "",
    @StringRes var titleRes: Int = 0,
    @StringRes var messageRes: Int = 0,
    var errorStatus: ResponseStatus?,
    var cancelable: Boolean = true,
    var showDialog: Boolean = true,
    var showNoInternetDialog: Boolean = true,
    var dialogCanceled: (() -> Unit)? = null,
)