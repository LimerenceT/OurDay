package com.day.ourday.data.entity

import androidx.annotation.IntDef

/**
 * Create by LimerenceT on 2019/12/29
 */


class Event(@Type val type: Int) {
    companion object {
        const val START: Int = 0
        const val CHANGE: Int = 1
    }

    @IntDef(CHANGE,START)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Type
}