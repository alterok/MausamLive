package com.alterok.mausamlive.core.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import com.alterok.kotlin_essential_extensions.castOrNull
import com.alterok.kotlin_essential_extensions.ifNull
import com.google.android.material.bottomsheet.BottomSheetBehavior

private const val TAG = "CustomBottomSheetBehavi"

class RefreshableBottomSheetBehavior<V : ViewGroup>(context: Context, attrs: AttributeSet) :
    BottomSheetBehavior<V>(context, attrs) {

    private var nestedScrollView: NestedScrollView? = null

    private fun initScrollView(child: V) {
        nestedScrollView.ifNull {
            nestedScrollView = child.children.find { it is NestedScrollView }.castOrNull()
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int,
    ): Boolean {
        initScrollView(child)
        return !(nestedScrollView != null && nestedScrollView?.canScrollVertically(-1) == true).apply {
            Log.i(TAG, "onStartNestedScroll: BS : $this")
        }
    }
}