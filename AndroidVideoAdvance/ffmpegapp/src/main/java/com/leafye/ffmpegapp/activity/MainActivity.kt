package com.leafye.ffmpegapp.activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.leafye.ffmpegapp.R
import com.leafye.ffmpegapp.databinding.ActivityMainBinding
import com.leafye.ffmpegapp.vm.MainViewModel
import com.leafye.ffmpegapp.vm.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    private val vm by lazy {
        ViewModelProviders.of(this, MainViewModelFactory(application))
            .get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                vm = this@MainActivity.vm
                Log.d(TAG, "vm:$vm")
            }
        obse()
        verifyStoragePermissions()
    }

    private fun obse() {
        vm.enterAudioHandleEvent.observe(this, Observer<Int> {
            AudioHandleActivity.startActivity(this)
        })
    }

    private fun verifyStoragePermissions() {
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
}
