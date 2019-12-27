package com.day.ourday.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * Create by LimerenceT on 2019/12/24
 */
class PictureViewModel(application: Application) : AndroidViewModel(application) {
    val mainBgPicturePath: MutableLiveData<String> = MutableLiveData()
    val pictureOldPath = MutableLiveData<String>()

    init {
        val sharedPreferences = application.getSharedPreferences("bg", Context.MODE_PRIVATE)
        val bgp = sharedPreferences.getString("bgp", "")
        if (bgp.isNullOrEmpty()) {
            mainBgPicturePath.value = null
        } else {
            mainBgPicturePath.value = bgp
        }
    }

}