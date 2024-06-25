package com.alterok.mausamlive.dashboard.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.alterok.mausamlive.core.ui.model.LocalityUIModel
import com.alterok.mausamlive.dashboard.ui.adapter.viewholder.CitySearchViewHolder
import com.alterok.mausamlive.dashboard.ui.util.LocalityDiffUtilCallback

class CitySearchAdapter(private val onItemSelected: (LocalityUIModel) -> Unit) :
    ListAdapter<LocalityUIModel, CitySearchViewHolder>(LocalityDiffUtilCallback) {
    private val searchableData: MutableList<LocalityUIModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitySearchViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false).let {
                CitySearchViewHolder(it, onItemSelected)
            }
    }

    override fun onBindViewHolder(holder: CitySearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setSearchableData(fullData: List<LocalityUIModel>) {
        searchableData.clear()
        searchableData.addAll(fullData)

        submitList(searchableData)
    }

    fun onQueryChange(query: String) {
        if (query.isNotEmpty()) {
            searchableData.filter { it.localityName.plus(it.city).contains(query, true) }.apply {
                submitList(this)
            }
        } else {
            submitList(searchableData)
        }
    }
}