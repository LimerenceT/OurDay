package com.day.ourday.util

import com.day.ourday.OurDayApplication

/**
 * Create by LimerenceT on 2019/12/27
 */
fun getFullPath(fileName: String): String {
    return OurDayApplication.getInstance().filesDir.path + "/" + fileName
}