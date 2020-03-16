package com.leafye.ffmpegapp.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.utils
 * @ClassName:      ContentUtils
 * @Description:     java类作用描述
 * @Author:         leafye
 * @CreateDate:     2020/3/16 11:02
 * @UpdateUser:
 * @UpdateDate:     2020/3/16 11:02
 * @UpdateRemark:
 */
class ContentUtils {

    companion object {

        fun getPath(context: Context, uri: Uri): String {
            if (DocumentsContract.isDocumentUri(context, uri)) {//DocumentProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    val type = split[0]
                    if ("primary".equals(type, true)) {
                        return Environment.getExternalStorageDirectory().absolutePath + "/" + split[1]
                    }
                } else if (isDownloadsDocument(uri)) {//downloadsProvider
                    val docId = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        docId.toLong()
                    )
                    return getDataColumn(context, contentUri, null, null) ?: ""
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    val type = split[0]
                    val contentUri = when (type) {
                        "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        else -> null
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    contentUri?.apply {
                        return getDataColumn(context, this, selection, selectionArgs) ?: ""
                    }
                }
            } else if ("content".equals(uri.scheme, true)) {//media store
                return getDataColumn(context, uri, null, null) ?: ""
            } else if ("file".equals(uri.scheme, true)) {
                return uri.path ?: ""
            }
            return ""
        }

        private fun getDataColumn(
            context: Context,
            uri: Uri,
            selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)
            try {
                cursor =
                    context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                cursor?.apply {
                    if (cursor.moveToFirst())
                        return getString(getColumnIndexOrThrow(column))
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        private fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

    }
}