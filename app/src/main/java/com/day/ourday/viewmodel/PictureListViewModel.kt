package com.day.ourday.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Create by LimerenceT on 2019/12/26
 */
class PictureListViewModel: ViewModel() {
    val pictureList = MutableLiveData<List<String>>()
}