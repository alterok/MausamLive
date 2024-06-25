package com.alterok.mausamlive.dashboard.ui.util

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager

private const val TAG = "DisplayUtil"
object DisplayUtil {
    fun getDisplayResolution(context:Context) = kotlin.run {
        context.resources.displayMetrics.let {
            it.widthPixels to it.heightPixels
        }
    }

    fun getRealDisplaySize(context: Context): Pair<Int, Int> {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = context.display
            display?.getRealMetrics(displayMetrics)
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        }

        val realWidth = displayMetrics.widthPixels
        val realHeight = displayMetrics.heightPixels

        return Pair(realWidth, realHeight).apply {
            Log.d(TAG, "getDisplayResolution: $this")
        }
    }
}