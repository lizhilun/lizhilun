package com.lizl.demo.passwordbox.custom.function

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

fun Uri.getFileName(context: Context): String?
{
    val cursor = context.contentResolver.query(this, null, null, null, null, null)

    var fileName: String? = null
    cursor?.use {
        if (it.moveToFirst())
        {
            fileName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    }
    return fileName
}