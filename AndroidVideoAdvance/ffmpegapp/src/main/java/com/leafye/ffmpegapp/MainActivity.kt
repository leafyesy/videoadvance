package com.leafye.ffmpegapp

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val yeFFmpeg by lazy {
        YeFFmpeg()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initClick()
        verifyStoragePermissions()
    }

    fun verifyStoragePermissions() {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            this,
            WRITE_EXTERNAL_STORAGE
        )
        val permissionArr = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            requestPermissions(
                this, permissionArr,
                1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun initClick() {
        btnCodec.setOnClickListener {
            val storagePath = Environment.getExternalStorageDirectory().absolutePath
            val path1 = "$storagePath/input.mp4"
            val path2 = "$storagePath/2.mp4"
            Log.i(TAG, "input path:$path1    output path:$path2")
            yeFFmpeg.decode(path1, path2)
        }
        btnFilter.setOnClickListener {
            val avfilterinfo = yeFFmpeg.avfilterinfo()
            Log.i(TAG, "filter:$avfilterinfo")
        }
        btnFormat.setOnClickListener {
            val avformatinfo = yeFFmpeg.avformatinfo()
            Log.i(TAG, "format:$avformatinfo")
        }
        btnProtocol.setOnClickListener {
            val urlprotocolinfo = yeFFmpeg.urlprotocolinfo()
            Log.i(TAG, "protocol:$urlprotocolinfo")
        }
    }
}
