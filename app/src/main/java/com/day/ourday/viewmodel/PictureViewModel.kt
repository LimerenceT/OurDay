package com.day.ourday.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.day.ourday.R

/**
 * Create by LimerenceT on 2019/12/24
 */
class PictureViewModel(application: Application) : AndroidViewModel(application) {
    val mainBg: MutableLiveData<Drawable> = MutableLiveData()

    init {
        val sharedPreferences = application.getSharedPreferences("bg", Context.MODE_PRIVATE)
        val bgp = sharedPreferences.getString("bgp", "")
        if (bgp.isNullOrEmpty()) {
            mainBg.value = application.resources.getDrawable(R.drawable.tang)
        } else {
            mainBg.value = Drawable.createFromPath(bgp)
        }
    }

}