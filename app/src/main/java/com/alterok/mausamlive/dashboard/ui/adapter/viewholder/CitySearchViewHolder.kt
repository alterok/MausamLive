package com.alterok.mausamlive.dashboard.ui.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alterok.mausamlive.core.ui.model.LocalityUIModel

class CitySearchViewHolder(itemView: View, onItemSelected: (LocalityUIModel) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private var localityData:LocalityUIModel? = null

    init {
        itemView.setOnClickListener {
            localityData?.let { it1 -> onItemSelected(it1) }
        }
    }

    fun bind(locality: LocalityUIModel) {
        localityData = locality
        (itemView as? TextView)?.text = locality.localityName.plus(", ").plus(locality.city)
    }
}
