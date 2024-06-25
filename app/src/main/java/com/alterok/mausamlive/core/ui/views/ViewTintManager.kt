package com.alterok.mausamlive.core.ui.views

import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.ColorUtils
import com.alterok.mausamlive.core.util.TintScheme
import com.google.android.material.card.MaterialCardView

class ViewTintManager(
    private val cardView: MaterialCardView,
    private vararg val views: View,
) {

    fun tintWith(tintScheme: TintScheme) {
        cardView.strokeColor = tintScheme.strokeColor
        cardView.backgroundTintList = ColorUtils.setAlphaComponent(tintScheme.backgroundColor, 64)
            .let { ColorStateList.valueOf(it) }
        views.forEach {
            when (it) {
                is TextView -> it.setTextColor(tintScheme.foregroundColor)
                is AppCompatImageView -> it.imageTintList = ColorStateList.valueOf(tintScheme.foregroundColor)
            }
        }
    }
}