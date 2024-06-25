package com.alterok.mausamlive.core.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.abs

class SnapBottomSheetBehaviour<V : View>(context: Context, attrs: AttributeSet?) :
    BottomSheetBehavior<V>(context, attrs) {

    private var snapPoints = intArrayOf(900, 1900)

    fun setSnapPoints(points: IntArray) {
        snapPoints = points
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        val handled = super.onLayoutChild(parent, child, layoutDirection)
        if (state == STATE_EXPANDED || state == STATE_HALF_EXPANDED) {
            child.y = snapPoints[1].toFloat()
        } else if (state == STATE_COLLAPSED) {
            child.y = snapPoints[0].toFloat()
        }
        return handled
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, type: Int) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        snapToClosestPosition(child)
    }

    private fun snapToClosestPosition(child: View) {
        val childY = child.y
        val closestPoint = snapPoints.minByOrNull { abs(it - childY) } ?: return
        child.animate().y(closestPoint.toFloat()).setDuration(300).start()
    }
}