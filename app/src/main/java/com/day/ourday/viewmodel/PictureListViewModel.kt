package com.day.ourday.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * Create by LimerenceT on 2019/12/26
 */
class PictureListViewModel(application: Application) : AndroidViewModel(application) {
    val pictureList:MutableLiveData<List<String>> = MutableLiveData()
    private val myApplication =application
    init {
        refreshData()
    }

    fun refreshData() {
        val toList = myApplication.applicationContext.filesDir.list()?.toList()
        pictureList.value=toList
    }

}