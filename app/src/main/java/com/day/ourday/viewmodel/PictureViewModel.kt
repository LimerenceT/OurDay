package com.day.ourday.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.day.ourday.data.entity.Event

/**
 * Create by LimerenceT on 2019/12/24
 */
class PictureViewModel(application: Application) : AndroidViewModel(application) {
    val mainBgPictureName: MutableLiveData<String> = MutableLiveData()
    val oldPictureName = MutableLiveData<String>()
    val bgChangeEvent = MutableLiveData<Event>()

    init {
        bgChangeEvent.value = Event(Event.START)
    }
}