package com.alterok.mausamlive.core.util

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

inline fun Context.showToast(messageBlock : () -> Pair<String, Int>){
    val msgResult = messageBlock()
    Toast.makeText(this, msgResult.first, msgResult.second).show()
}

/**
 * Extension function to show toast inside a fragment, pair message string with [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG]
 *
 * ```
 * showToast {
 *    "Check Internet Connection!" to Toast.LENGTH_LONG
 * }
 * ```
 */

inline fun Fragment.showToast(messageBlock : () -> Pair<String, Int>){
    val msgResult = messageBlock()
    Toast.makeText(requireContext(), msgResult.first, msgResult.second).show()
}