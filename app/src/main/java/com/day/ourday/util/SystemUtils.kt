package com.day.ourday.util

import android.content.Context

/**
 * Create by LimerenceT on 2019/12/30
 */
object SystemUtils {
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        with(context.resources) {
            return getDimensionPixelSize(getIdentifier("status_bar_height", "dimen", "android"))
        }
    }
}