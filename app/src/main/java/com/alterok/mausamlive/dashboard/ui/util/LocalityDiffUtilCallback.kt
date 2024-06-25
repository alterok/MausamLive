package com.alterok.mausamlive.dashboard.ui.util

import androidx.recyclerview.widget.DiffUtil
import com.alterok.mausamlive.core.ui.model.LocalityUIModel

object LocalityDiffUtilCallback : DiffUtil.ItemCallback<LocalityUIModel>() {
    override fun areItemsTheSame(oldItem: LocalityUIModel, newItem: LocalityUIModel): Boolean {
        return oldItem.localityId == newItem.localityId
    }

    override fun areContentsTheSame(oldItem: LocalityUIModel, newItem: LocalityUIModel): Boolean {
        return oldItem.localityId == newItem.localityId && oldItem.city == newItem.city
    }
}