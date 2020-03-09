package com.leafye.ffmpegapp.activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.leafye.ffmpegapp.BR
import com.leafye.base.BaseActivity
import com.leafye.ffmpegapp.R
import com.leafye.ffmpegapp.databinding.ActivityMainBinding
import com.leafye.ffmpegapp.vm.MainViewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    override fun initVariableId(): Int {
        return BR.vm
    }

    override fun initContentView(savedInstanceState: Bundle?): Int = R.layout.activity_main

    override fun setupObservers() {

    }

    override fun activityPrepared() {
        verifyStoragePermissions()
    }

    private fun verifyStoragePermissions() {
        ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
            .takeIf { it != PackageManager.PERMISSION_GRANTED }
            ?.let {
                requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), 1)
            }
    }
}
